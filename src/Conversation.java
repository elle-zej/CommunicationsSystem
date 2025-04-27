import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements Serializable{
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
	
	public String getMembersString(){
		List<String> membersString = new ArrayList<String>();
		String members = "";
		for(int i = 0; i< this.members.size(); i++) {
			String name = this.members.get(i).getFullName();
			membersString.add(name);
		}
		
		for(int i = 0; i < membersString.size(); i++) {
			members += membersString.get(i);
			if (i < membersString.size() -1) {
				members += ", ";
			}
		}
		return members;
	}
	
	public List<String> getRecipients(User user){
		String sender = user.getFullName();
		List<String> recipientsString = new ArrayList<String>();
		for(int i = 0; i< this.members.size(); i++) {
			if(!this.members.get(i).getFullName().equals(sender)) {
			String name = this.members.get(i).getFullName();
			recipientsString.add(name);
			}
		}
		return recipientsString;
	}
	
	public String getConversationIDString() {
		return Integer.toString(this.conversationID);
	}
	
	public String getMessagesString(){
		String sendersAndMessages = "";
		for(int i =0; i<conversationHistory.size();i++) {
			sendersAndMessages += 
					conversationHistory.get(i).getTimestamp() + " " +
					conversationHistory.get(i).getSender().getFullName() + ": " +
					conversationHistory.get(i).getContent() + "\n";
		}
		
		return sendersAndMessages;
	}

}
