package kafka.generic.topic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TopicManager {
	Logger logger = LoggerFactory.getLogger(getClass());

	private Set<String> allTopics = new HashSet<>();

	@Autowired
	private Admin adminClient;

	
	public TopicManager(Admin adminClient) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
		String topic = "topic-1";
		Properties properties = new Properties();
		InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("kafka.properties");
		properties.load(resourceAsStream);

//		adminClient = AdminClient.create(properties);

		ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
		listTopicsOptions.listInternal(true);

		Set<String> topicNames = adminClient.listTopics(listTopicsOptions).names().get();

		if (!topicNames.contains(topic)) {
			NewTopic newTopic = new NewTopic(topic, 1, (short) 1); // new NewTopic(topicName, numPartitions,
			// replicationFactor)

			List<NewTopic> newTopics = new ArrayList<NewTopic>();
			newTopics.add(newTopic);

			adminClient.createTopics(newTopics);
			logger.debug("New Topics Created: {}", topic);
			allTopics.add(topic);
		} else {
			logger.debug("Topics Exists!!");
			allTopics.addAll(topicNames);
		}

		logger.info("TOPICS: " + topicNames);
		adminClient.close();

	}

	public void createTopics(final Set<String> topics) throws InterruptedException, ExecutionException {
		String regex = "^[a-zA-Z0-9\\-]+$";
		Pattern pattern = Pattern.compile(regex);
		
		List<String> validTopicsList = topics.stream().filter(t -> t.matches(regex)).collect(Collectors.toList());
		if(validTopicsList.isEmpty()) {
			throw new IllegalArgumentException("INVALID TOPIC NAMES!");
		} 
		
		
		ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
		listTopicsOptions.listInternal(true);
		Set<String> topicNames = adminClient.listTopics(listTopicsOptions).names().get();

		validTopicsList.forEach(topic -> {
			if (!topicNames.contains(topic)) {
				NewTopic newTopic = new NewTopic(topic, 1, (short) 1); // new NewTopic(topicName, numPartitions,
				// replicationFactor)

				List<NewTopic> newTopics = new ArrayList<NewTopic>();
				newTopics.add(newTopic);

				adminClient.createTopics(newTopics);	
				logger.debug("New Topics Created: {}", topic);
				allTopics.add(topic);
				
			} else {
				logger.debug("Topics Exists!!");
				allTopics.addAll(topicNames);
			}
		});
	}

	public DeleteTopicsResult deleteTopics(final Set<String> topics) throws InterruptedException, ExecutionException {
		Set<String> topicNames = adminClient.listTopics().names().get();
		Set<String> topicsToDelete = topics.stream().filter(topic -> topicNames.contains(topic))
				.collect(Collectors.toSet());
		DeleteTopicsResult deletedTopicsResult = adminClient.deleteTopics(topicsToDelete);
		topics.removeAll(topicsToDelete);
		if (!topics.isEmpty()) {
			logger.debug("Topics doesnot exists to Delete: {}" + topics);
		}

		return deletedTopicsResult;
	}

	public DescribeTopicsResult describeTopics(final Set<String> topics) {
		return adminClient.describeTopics(topics);
	}

	public Set<String> getAllTopics() throws InterruptedException, ExecutionException {
		return adminClient.listTopics().names().get();
	}

	public Set<String> getTopics() {
		return allTopics;
	}

	public void addTopic(String topic) {
		this.allTopics.add(topic);
	}

	public void addTopics(Set<String> topics) {
		this.allTopics.addAll(topics);
	}

//	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		Properties properties = new Properties();
//		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "PLAINTEXT://192.168.1.5:9092");
//		AdminClient kafkaAdminClient = KafkaAdminClient.create(properties);
//		CreateTopicsResult result = kafkaAdminClient.createTopics(Stream.of("foo", "bar", "baz")
//				.map(name -> new NewTopic(name, 3, (short) 1)).collect(Collectors.toList()));
//		result.all().get();
//	}
}
