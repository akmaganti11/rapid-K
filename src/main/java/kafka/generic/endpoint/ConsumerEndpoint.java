package kafka.generic.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kafka.generic.consumer.Consumer;
import kafka.generic.consumer.UserConsumer;
import kafka.generic.vo.ConsumerEvent;

@RestController("/comsumer")
public class ConsumerEndpoint {

	@Autowired
	private Consumer consumer;
	
	@Autowired
	private UserConsumer userConsumer;

	@GetMapping("/events")
	public List<ConsumerEvent> consumerEvents(@RequestParam final String topics,
			@RequestParam final Integer numOfPolls) {
		return consumer.consumerEvents(topics, numOfPolls);
	}
	
	@GetMapping("/user-events")
	public List<ConsumerEvent> consumerUserEvents(@RequestParam final String topics,
			@RequestParam final Integer numOfPolls) {
		return userConsumer.consumerEvents(topics, numOfPolls);
	}
}
