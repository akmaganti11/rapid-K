package kafka.rapid.vo;

import org.apache.kafka.common.header.Headers;

public class ConsumerEvent {

	public ConsumerEvent(String topic, int partition, long offset, String key, String value, long timestamp) {
		super();
		this.topic = topic;
		this.partition = partition;
		this.offset = offset;
		this.key = key;
		this.value = value;
		this.timestamp = timestamp;
	}

	private String topic;
	private int partition;
	private long offset;
	private long timestamp;
	private Headers headers;
	private String key;
	private String value;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Headers getHeaders() {
		return headers;
	}

	public void setHeaders(Headers headers) {
		this.headers = headers;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ConsumerEvent [topic=" + topic + ", partition=" + partition + ", offset=" + offset + ", timestamp="
				+ timestamp + ", headers=" + headers + ", key=" + key + ", value=" + value + "]";
	}

}
