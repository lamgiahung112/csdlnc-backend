package addb.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DatabaseConfigRunner implements CommandLineRunner {
	@Autowired
	private DatabaseConfigInitializer initializer;

	@Override
	public void run(String... args) throws Exception {
//		initializer.init();
	}
}
