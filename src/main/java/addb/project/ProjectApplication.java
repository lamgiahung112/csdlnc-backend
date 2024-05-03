package addb.project;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjectApplication {
	@Bean
    public Configuration config() {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("fs.defaultFS", "hdfs://localhost:9000/");
        return conf;
    }
    
    @Bean 
    public HBaseAdmin admin() {
        try {
			return new HBaseAdmin(config());
		} catch (Exception e) {
			return null;
		}
    }
	public static void main(String[] args) {
	
		SpringApplication.run(ProjectApplication.class, args);
	}

}
