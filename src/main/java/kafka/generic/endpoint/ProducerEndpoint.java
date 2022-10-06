package kafka.generic.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kafka.generic.producer.Producer;
import kafka.generic.producer.UserProducer;
import kafka.generic.vo.User;

@RestController("/producer")
public class ProducerEndpoint {

	@Autowired
	private Producer producer;
	
	@Autowired
	private UserProducer userProducer;

	@PostMapping("/produce")
	public void produceEvent(@RequestParam String topic, @RequestParam String event) {
		producer.produceEvent(topic, event);
	}
	
	@PostMapping("/produce/user")
	public void produceEvent(@RequestParam String topic, @RequestBody User user) {
		userProducer.produceUserEvent(topic, user);
	}
}
