package kafka.rapid.kickstart;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kafka.rapid.consumer.Consumer;
import kafka.rapid.producer.Producer;

@Component
public class KickStartKafka {
	@Autowired
	private Producer producer;
	
	@Autowired
	private Consumer consumer;

	@PostConstruct
	public void testKakfaProducerConsumer() {
		producer.produceMessages();
		consumer.consume();
	}
}
