package kafka.generic.kickstart;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kafka.generic.consumer.Consumer;
import kafka.generic.producer.Producer;

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
