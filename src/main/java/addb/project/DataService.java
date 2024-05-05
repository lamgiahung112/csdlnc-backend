package addb.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import addb.project.config.DatabaseConfig;
import addb.project.model.Product;
import addb.project.model.request.GetProductRequest;
import addb.project.model.request.UpdateProductRequest;
import addb.project.utilities.ProductMatcher;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataService {
	public List<Product> getSnapshots(GetProductRequest request) {
		List<Product> snapshots = new ArrayList<>();
		try (Table table = DatabaseConfig.CONN().getTable(TableName.valueOf("PRODUCTS"))) {
			Scan scan = new Scan();
			ResultScanner scanner = table.getScanner(scan);
			int count = 0;
			
			for (Result result : scanner) {
				Product snapshot = Product.fromResult(result);
				if (snapshots.size() == request.getPageSize()) {
					break;
				}
				// Ignore non-matches
				if (!ProductMatcher.matches(snapshot, request)) {
					continue;
				}
				if (count < (request.getPage()-1)*request.getPageSize()) {
					count++;
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
	
	public void handleUpdateProduct(MultipartFile file, UpdateProductRequest request) {
		if (file != null && !file.isEmpty()) {
			try (FileInputStream inp = (FileInputStream) file.getInputStream()) {
				String path = "/backend/src/main/resources/public/" + request.getId() + ".jpg";
				File savedFile = new File(path);
				if (savedFile.exists()) {
					savedFile.delete();
				}
				FileOutputStream out = new FileOutputStream(savedFile);
				out.write(inp.readAllBytes());
				out.close();
				inp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try(Table table = DatabaseConfig.CONN().getTable(TableName.valueOf("PRODUCTS"))) {
			 Put put = new Put(Bytes.toBytes(request.getId()));
   		 
	   		 put.addColumn("ID".getBytes(), "gender".getBytes(), Bytes.toBytes(request.getGender()));
	   		 put.addColumn("ID".getBytes(), "masterCategory".getBytes(), Bytes.toBytes(request.getMasterCategory()));
	   		 put.addColumn("ID".getBytes(), "subCategory".getBytes(), Bytes.toBytes(request.getSubCategory()));
	   		 put.addColumn("ID".getBytes(), "articleType".getBytes(), Bytes.toBytes(request.getArticleType()));
	   		 put.addColumn("ID".getBytes(), "baseColour".getBytes(), Bytes.toBytes(request.getBaseColour()));
	   		 put.addColumn("ID".getBytes(), "season".getBytes(), Bytes.toBytes(request.getSeason()));
	   		 put.addColumn("ID".getBytes(), "year".getBytes(), Bytes.toBytes(request.getYear()));
	   		 put.addColumn("ID".getBytes(), "usage".getBytes(), Bytes.toBytes(request.getUsage()));
	   		 put.addColumn("ID".getBytes(), "productDisplayName".getBytes(), Bytes.toBytes(request.getProductDisplayName()));
	   		 
	   		 table.put(put);
	      } catch(Exception e) {
	    	  e.printStackTrace();
	    	  log.info(e.getMessage());
	      }
	}
}
