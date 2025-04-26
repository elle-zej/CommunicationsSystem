import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
	//check line below
	private static final long serialVersionUID = 1L;
	
	private final User sender;
	private final List<String> receiver;
	private final String content;
	private final String timestamp;
	private Status status;
	
	Message(User sender, List<String> receiver, String content, Status status){
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		LocalDateTime timestamp = LocalDateTime.now();
		this.timestamp = timestamp.toString();
		this.status = status;
	}
	
	public String getTimestamp() {
		return this.timestamp;
	}
	
	public List<String> getReciever() {
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
