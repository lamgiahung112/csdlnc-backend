package addb.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import addb.project.model.StockSnapshot;
import addb.project.model.request.GetStocksRequest;
import addb.project.utilities.StockSnapshotMatcher;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataService {
	@Autowired Configuration conf;
	@Autowired HBaseAdmin admin;
	
	public List<StockSnapshot> getSnapshots(GetStocksRequest request) {
		List<StockSnapshot> snapshots = new ArrayList<>();
		try (HTable table = new HTable(conf, "GOOGLE")) {
			Scan scan = new Scan();
			ResultScanner scanner = table.getScanner(scan);
			
			for (Result result : scanner) {
				StockSnapshot snapshot = StockSnapshot.fromResult(result);
				if (snapshots.size() == request.getPageSize()) {
					break;
				}
				// Ignore non-matches
				if (!StockSnapshotMatcher.matches(snapshot, request)) {
					continue;
				}
				snapshots.add(snapshot);
			}
			return snapshots;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void readAndAddSnapshotsFromFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return;
		}
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream())); 
	    		  HTable table = new HTable(conf, "GOOGLE")) {
			int count = 0;
			String line;
	    	 while ((line = reader.readLine()) != null) {
	    		 String[] data = line.split(",");
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
	    		 count++;
	    	 }
	    	 log.info("Added " + count + " snapshots");
	      } catch(Exception e) {
	    	  e.printStackTrace();
	    	  log.info(e.getMessage());
	      }
	}
}
