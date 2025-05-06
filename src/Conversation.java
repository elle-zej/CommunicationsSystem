import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements Serializable{
	private static final long serialVersionUID = 8893641342213687609L;
	private List<String> conversationHistory;
	private List<Message> conversationHistoryMessages;
	private List<User> members;
	private List<String> membersString;
	private int conversationID;
	private static int IDCount = 0;
	
	Conversation(List<String> conversationHistory, List<User> members){
		this.conversationHistory = conversationHistory;
		this.members = members;
		this.conversationID = IDCount++;
	}
	
	Conversation(List<String> members){
		this.conversationHistory = new ArrayList<>();
		this.membersString = members;
		this.conversationID = IDCount++;
	}
	
	Conversation(int conversationID, List<String> membersList, List<String> conversationHistory){
		this.conversationHistory = conversationHistory;
		this.membersString = membersList;
		this.conversationID = conversationID;
	}
	
	public void addMessage(Message message) {
		conversationHistoryMessages.add(message);
	}
	
	public String getMembersString(){
		List<String> membersString = new ArrayList<String>();
		String members = "";
		for(int i = 0; i< this.membersString.size(); i++) {
			String name = this.membersString.get(i);
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
		String sender = user.getFullName().toUpperCase();
		List<String> recipientsString = new ArrayList<String>();
		for(int i = 0; i< this.membersString.size(); i++) {
			if(!this.membersString.get(i).equals(sender)) {
			String name = this.membersString.get(i);
			recipientsString.add(name);
			}
		}
		return recipientsString;
	}
	
	public String getRecipientsString (User user) {
		String sender = user.getFullName().toUpperCase();
		String recipients = "";
		List<String> recipientsString = new ArrayList<String>();
		for(int i = 0; i< this.membersString.size(); i++) {
			if(!this.membersString.get(i).equals(sender)) {
			String name = this.membersString.get(i);
			recipientsString.add(name);
			}
		}
		
		for(int i = 0; i < recipientsString.size(); i++) {
			recipients += recipientsString.get(i);
			if (i < recipientsString.size() -1) {
				recipients += ", ";
			}
		}
		
		return recipients;
	}
	
	public List<String> getMembersList(){
		return membersString;
	}
	
	public String getConversationIDString() {
		return Integer.toString(this.conversationID);
	}
	
	public String getMessagesString(){
		String sendersAndMessages = "";
		for(int i =0; i<conversationHistory.size();i++) {
			sendersAndMessages += 
					conversationHistory.get(i) + "\n";
		}
		
		return sendersAndMessages;
	}

}
