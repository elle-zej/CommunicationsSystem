import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
	private final User sender;
	private final List<String> receiver;
	private final String content;
	private final String timestamp;
	private Status status;
	
	Message(User sender, List<String> receiver, String content, Status status){
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = LocalDateTime.now().toString();
		this.status = status;
	}
	
	Message(User sender, String content, Status status){
		this.sender = sender;
		this.receiver = new ArrayList<>();
		this.content = content;
		this.timestamp = "";
		this.status = status;
	}
	
	public String getTimestamp() {
		return this.timestamp;
	}
	
	public List<String> getReceiver() {
		return this.receiver;
	}
	
	public User getSender() {
		return this.sender;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public Status getStatus() {
		return this.status;
	}
}
