package kafka.rapid.consumer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kafka.rapid.topic.TopicManager;
import kafka.rapid.vo.ConsumerEvent;

@Component
public class Consumer {
	Logger logger = LoggerFactory.getLogger(Consumer.class.getName());

	@Autowired
	private TopicManager topicManager;

	String bootstrapServers = "127.0.0.1:9092";
	String grp_id = "test";
	String topic;
	// Creating consumer properties
	Properties properties = new Properties();

	KafkaConsumer<String, String> consumer;

	@PostConstruct
	private void loadConsumerProps() {
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, grp_id);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		topic = topicManager.getTopics().iterator().next();
		// creating consumer
		consumer = new KafkaConsumer<String, String>(properties);

	}

	public void consume() {
		// Subscribing
		consumer.subscribe(Arrays.asList(topic));

		int count = 0;
		// polling
		while (count >= 100) {
			count++;
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, String> record : records) {
				logger.info("Key: " + record.key() + ", Value:" + record.value());
				logger.info("Partition:" + record.partition() + ",Offset:" + record.offset());
			}

		}
	}

	public List<ConsumerEvent> consumerEvents(String topic, Integer numOfpolls) {
		List<ConsumerEvent> consumerEvents = new ArrayList<>();
		ConsumerRecords<String, String> events = null;
		int pollCount = 0;

		if (isValidTopic(topic)) {
			consumer.subscribe(Arrays.asList(topic));
			
			Collection<TopicPartition> topicPartitions = consumer.assignment();
			consumer.seekToEnd(topicPartitions);
//			consumer.seekToBeginning(topicPartitions);
			
			while (numOfpolls >= pollCount) {
				events = consumer.poll(Duration.ofMillis(100));

				events.forEach(record -> {
					
					ConsumerEvent event = new ConsumerEvent(record.topic(), record.partition(), record.offset(),  record.key().toString(), record.value().toString(), record.timestamp());
							consumerEvents.add(event);
					logger.info("Key: " + record.key() + ", Value:" + record.value());
					logger.info("Partition:" + record.partition() + ",Offset:" + record.offset()+ "timestamp: "+record.timestamp());

				});
				
				pollCount++;
			}
		}

		return consumerEvents;
	}

	private boolean isValidTopic(String topic) {
		return topicManager.getTopics().contains(topic);
	}
}
