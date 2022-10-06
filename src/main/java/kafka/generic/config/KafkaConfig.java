package kafka.generic.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.Admin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import kafka.generic.topic.TopicManager;

@Configuration
public class KafkaConfig {

	@Bean
	@Scope("prototype")
	public Admin admin() throws IOException {
		Properties properties = new Properties();
		InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("kafka.properties");
		properties.load(resourceAsStream);
		Admin admin = Admin.create(properties);
		return admin;
	}
	
	@Bean
	public TopicManager topicManager(Admin admin)
			throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
		return new TopicManager(admin);
	}
}
