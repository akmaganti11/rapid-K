package kafka.generic.endpoint;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kafka.generic.topic.TopicManager;

@RestController("/kafka")
public class KafkaEndpoint {

	@Autowired
	private TopicManager topicManager;

	@GetMapping("/topics")
	public Set<String> name() throws InterruptedException, ExecutionException {
		return topicManager.getAllTopics();
	}
	
	@PostMapping("/topics")
	public void createTopic(@RequestParam final Set<String> topics) throws InterruptedException, ExecutionException {
		 topicManager.createTopics(topics);
	}

}
