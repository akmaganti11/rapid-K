package kafka.rapid.producer;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.LifecycleProcessor;
import org.springframework.stereotype.Component;

import kafka.rapid.topic.TopicManager;

@Component
public class Producer implements LifecycleProcessor {

	Logger logger = LoggerFactory.getLogger(Producer.class.getName());

	@Autowired
	private TopicManager topicManager;

	Properties props;
	KafkaProducer<String, String> kafkaProducer;

	public Producer() {
		props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaProducer = new KafkaProducer<>(props);
	}

	public void produceMessages() {
		for (int i = 0; i < 100; i++) {
			kafkaProducer.send(new ProducerRecord<String, String>(topicManager.getTopics().iterator().next(),
					Integer.toString(i), Integer.toString(i)));
			logger.info("Event >>" + i);
		}
	}

	public void produceEvent(final String topic, final String event) {
		if (isValidTopic(topic)) {
			String key = UUID.randomUUID().toString();
			kafkaProducer.send(new ProducerRecord<String, String>(topic, key, event));
			kafkaProducer.flush();
			logger.info("Event >> topic:"+ topic  +" key: " + key  +" message: " +event );
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
