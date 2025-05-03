import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
	private static final long serialVersionUID = -5531887591421027149L;
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
	
	Message(String content, Status status){
		this.sender = null;
		this.receiver = null;
		this.content = content;
		this.status = status;
		this.timestamp = "";
	}
	
	Message(User sender, String content, Status status){
		this.sender = sender;
		this.receiver = new ArrayList<>();
		this.content = content;
		this.timestamp = "";
		this.status = status;
	}
	
	public void setStatus(Status status) {
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
	
	public List<String> getMembers(){
		List<String> members = new ArrayList<String>();
		for (int i =0; i < this.receiver.size(); i++) {
			members.add(this.receiver.get(i).trim().toUpperCase());
		}
		members.add(this.sender.getFullName().trim().toUpperCase());
		return members;
	}
}
