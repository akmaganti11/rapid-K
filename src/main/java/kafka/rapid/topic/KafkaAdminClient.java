package kafka.rapid.topic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;

public class KafkaAdminClient {

	public AdminClient createAdminClient() throws IOException {
		Properties properties = new Properties();
		InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("kafka.properties");
		properties.load(resourceAsStream);
		return AdminClient.create(properties);
	}

}
