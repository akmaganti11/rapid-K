package kafka.rapid.config;

import java.util.HashSet;
import java.util.Set;

public class Topics {

	private Set<String> topicSet = new HashSet<>();

	public Set<String> getTopics() {
		return topicSet;
	}

	public void addTopic(String topic) {
		this.topicSet.add(topic);
	}

	public void addTopics(Set<String> topics) {
		this.topicSet.addAll(topics);
	}

}
