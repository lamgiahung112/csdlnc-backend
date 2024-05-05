package addb.project.config;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class DatabaseConfig {
	private static Configuration config;
	private static HBaseAdmin admin;
	private static Connection connection;
	
	public static Configuration CONFIG() {
		if (config == null) {
			config = HBaseConfiguration.create();
			config.set("hbase.zookeeper.property.clientPort", "2181");
			config.set("hbase.rootdir", "hdfs://namenode:9000/hbase");
	        config.set("hbase.cluster.distributed", "true");
	        config.set("hbase.zookeeper.quorum", "zoo");
	        config.set("hbase.master", "hbase-master:16000");
	        config.set("hbase.master.hostname", "hbase-master");
	        config.set("hbase.master.port", "16000");
	        config.set("hbase.master.info.port", "16010");
	        config.set("hbase.regionserver.port", "16020");
	        config.set("hbase.regionserver.info.port", "16030");
	        config.set("hbase.zookeeper.property.clientPort", "2181");
		}
		return config;
	}
	
	public static Connection CONN() {
		if (connection == null) {
			try {
				connection = ConnectionFactory.createConnection(CONFIG());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	public static HBaseAdmin ADMIN() {
		try {
			if (admin == null) {
				admin = (HBaseAdmin) CONN().getAdmin();
			}
			return admin;
		} catch (Exception e) {
			return null;
		}
	}
}
