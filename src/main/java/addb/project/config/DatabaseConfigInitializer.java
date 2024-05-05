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
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@org.springframework.context.annotation.Configuration
@Slf4j
public class DatabaseConfigInitializer {
	public void init() {
		try {
			setupTableAndColumn();
//			deleteAllData();
//			addData();
			
			log.info("Database config initilialized!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupTableAndColumn() {
		try {
			// Create table and column family
		      HTableDescriptor table = new HTableDescriptor(TableName.valueOf("PRODUCTS"));
		      HColumnDescriptor column = new HColumnDescriptor("ID");
		      table.addFamily(column);
		      
		      DatabaseConfig.ADMIN().createTable(table);
		      
		      log.info("CREATED table GOOGLE");
		      log.info("CREATED column STOCK_PRICES");
		} catch (Exception e) {
			log.info("Table and column is already setup!");
		}
	}
	
	private void deleteAllData() throws IOException {
		try (Table table = DatabaseConfig.CONN().getTable(TableName.valueOf("GOOGLE"))) {
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
	      String csvFile = "/backend/data/styles.csv";
	      String line = "";
	      String csvSplitter = ",";
	      
	      boolean isFirstLine = true;
	      
	      try(BufferedReader reader = new BufferedReader(new FileReader(csvFile)); 
	    		  Table table = DatabaseConfig.CONN().getTable(TableName.valueOf("PRODUCTS"))) {
	    	 while ((line = reader.readLine()) != null) {
	    		 if (isFirstLine) {
	    			 isFirstLine = false;
	    			 continue;
	    		 }
	    		 String[] data = line.split(csvSplitter);
	    		 String id = data[0];
	    		 String gender = data[1];
	    		 String masterCategory = data[2];
	    		 String subCategory = data[3];
	    		 String articleType = data[4];
	    		 String baseColour = data[5];
	    		 String season = data[6];
	    		 int year = Integer.parseInt(data[7]);
	    		 String usage = data[8];
	    		 String productDisplayName = data[9];
	    		 
	    		 Put put = new Put(Bytes.toBytes(id));
	    		 
	    		 put.addColumn("ID".getBytes(), "gender".getBytes(), Bytes.toBytes(gender));
	    		 put.addColumn("ID".getBytes(), "masterCategory".getBytes(), Bytes.toBytes(masterCategory));
	    		 put.addColumn("ID".getBytes(), "subCategory".getBytes(), Bytes.toBytes(subCategory));
	    		 put.addColumn("ID".getBytes(), "articleType".getBytes(), Bytes.toBytes(articleType));
	    		 put.addColumn("ID".getBytes(), "baseColour".getBytes(), Bytes.toBytes(baseColour));
	    		 put.addColumn("ID".getBytes(), "season".getBytes(), Bytes.toBytes(season));
	    		 put.addColumn("ID".getBytes(), "year".getBytes(), Bytes.toBytes(year));
	    		 put.addColumn("ID".getBytes(), "usage".getBytes(), Bytes.toBytes(usage));
	    		 put.addColumn("ID".getBytes(), "productDisplayName".getBytes(), Bytes.toBytes(productDisplayName));
	    		 
	    		 table.put(put);
	    	 }
	    	 log.info("Added all data");
	      } catch(Exception e) {
	    	  log.info(e.getMessage());
	      }
	}
}
