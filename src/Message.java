import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Message implements Serializable {
	//check line below
	private static final long serialVersionUID = 1L;
	
	private final User sender;
	private final List<User> receiver;
	private final String content;
	private final String timestamp;
	
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
