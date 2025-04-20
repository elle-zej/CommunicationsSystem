import java.util.ArrayList;
import java.util.List;

public class Message {
	private User sender;
	private List<User> receiver;
	private String content;
	private String timestamp;
	
	Message(User sender, List<User> receiver, String content){
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = "now";
	}
	
	public String getTimestamp() {
		return this.timestamp;
	}
	
	public List<User> getReciever() {
		return this.receiver;
	}
	
	public User getSender() {
		return this.sender;
	}
	
	public String getContent() {
		return this.content;
	}
	
}
