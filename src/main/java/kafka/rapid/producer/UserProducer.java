package kafka.rapid.producer;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.LifecycleProcessor;
import org.springframework.stereotype.Component;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializerConfig;
import kafka.rapid.topic.TopicManager;
import kafka.rapid.vo.User;

@Component
public class UserProducer implements LifecycleProcessor {

	Logger logger = LoggerFactory.getLogger(UserProducer.class.getName());

	@Autowired
	private TopicManager topicManager;

	Properties props;
	KafkaProducer<String, User> kafkaProducer;

	public UserProducer() {
		props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "PLAINTEXT://localhost:9092");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		
		// Configure the KafkaJsonSchemaSerializer.
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class.getName());
		// Schema registry location.
		props.put(KafkaJsonSchemaSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
		kafkaProducer = new KafkaProducer<>(props);
	}

	public void produceMessages() {
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			User user = new User(UUID.randomUUID().toString(),UUID.randomUUID().toString(),(short) random.nextInt());
			kafkaProducer.send(new ProducerRecord<String, User>(topicManager.getTopics().iterator().next(),
					Integer.toString(i), user));
			logger.info("Event >>" + user.toString());
		}
	}

	public void produceUserEvent(final String topic, final User user) {
		if (isValidTopic(topic)) {
			String key = UUID.randomUUID().toString();
			ProducerRecord<String, User> producerRecord = new ProducerRecord<>(topic, key, user);

			kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
				if (e == null) {
					System.out.println("Success!");
					System.out.println(recordMetadata.toString());
				} else {
					e.printStackTrace();
				}
			});

			kafkaProducer.flush();
			logger.info("Event >> topic:" + topic + " key: " + key + " message: " + user);
		}
	}

	private boolean isValidTopic(String topic) {
		return topicManager.getTopics().contains(topic);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		kafkaProducer.close();
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub

	}
}
