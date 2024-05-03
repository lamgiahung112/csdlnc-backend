package addb.project.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@org.springframework.context.annotation.Configuration
@Slf4j
public class DatabaseConfigInitializer {
	@Autowired
	private Configuration conf;
	@Autowired
	private HBaseAdmin admin;
	
	public void init() {
		try {
			setupTableAndColumn();
			deleteAllData();
			addData();
			
			log.info("Database config initilialized!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupTableAndColumn() {
		try {
			// Create table and column family
		      HTableDescriptor table = new HTableDescriptor("GOOGLE");
		      HColumnDescriptor column = new HColumnDescriptor("STOCK_PRICES");
		      table.addFamily(column);
		      
		      admin.createTable(table);
		      
		      log.info("CREATED table GOOGLE");
		      log.info("CREATED column STOCK_PRICES");
		} catch (Exception e) {
			log.info("Table and column is already setup!");
		}
	}
	
	private void deleteAllData() throws IOException {
		try (HTable table = new HTable(conf, "GOOGLE")) {
			Scan scan = new Scan();
			ResultScanner scanner = table.getScanner(scan);
			
			for (Result result : scanner) {
				Delete delete = new Delete(result.getRow());
				table.delete(delete);
			}
			log.info("Deleted all data");
		}
	}
	
	private void addData() {
		// Read file
	      String csvFile = "C:\\addb\\project\\data\\GOOG.csv";
	      String line = "";
	      String csvSplitter = ",";
	      
	      boolean isFirstLine = true;
	      
	      try(BufferedReader reader = new BufferedReader(new FileReader(csvFile)); 
	    		  HTable table = new HTable(conf, "GOOGLE")) {
	    	 while ((line = reader.readLine()) != null) {
	    		 if (isFirstLine) {
	    			 isFirstLine = false;
	    			 continue;
	    		 }
	    		 String[] data = line.split(csvSplitter);
	    		 String date = data[0];
	    		 double open = Double.parseDouble(data[1]);
	    		 double high = Double.parseDouble(data[2]);
	    		 double low = Double.parseDouble(data[3]);
	    		 double close = Double.parseDouble(data[4]);
	    		 double adjClose = Double.parseDouble(data[5]);
	    		 double volume = Double.parseDouble(data[6]);
	    		 
	    		 Put put = new Put(Bytes.toBytes(date));
	    		 
	    		 put.addColumn("STOCK_PRICES".getBytes(), "open".getBytes(), Bytes.toBytes(open));
	    		 put.addColumn("STOCK_PRICES".getBytes(), "high".getBytes(), Bytes.toBytes(high));
	    		 put.addColumn("STOCK_PRICES".getBytes(), "low".getBytes(), Bytes.toBytes(low));
	    		 put.addColumn("STOCK_PRICES".getBytes(), "close".getBytes(), Bytes.toBytes(close));
	    		 put.addColumn("STOCK_PRICES".getBytes(), "adjClose".getBytes(), Bytes.toBytes(adjClose));
	    		 put.addColumn("STOCK_PRICES".getBytes(), "volume".getBytes(), Bytes.toBytes(volume));
	    		 
	    		 table.put(put);
	    	 }
	    	 log.info("Added all data");
	      } catch(Exception e) {
	    	  log.info(e.getMessage());
	      }
	}
}
