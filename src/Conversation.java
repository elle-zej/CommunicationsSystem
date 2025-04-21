import java.util.ArrayList;
import java.util.List;

public class Conversation {
	private List<Message> conversationHistory;
	private List<User> members;
	private int conversationID;
	private static int IDCount = 0;
	
	Conversation(List<Message> conversationHistory, List<User> members){
		this.conversationHistory = conversationHistory;
		this.members = members;
		this.conversationID = IDCount++;
	}
	
	public void addMessage(Message message) {
		conversationHistory.add(message);
	}

}
