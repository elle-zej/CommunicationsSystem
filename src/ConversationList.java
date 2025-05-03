import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class ConversationList implements Serializable {
	private static final long serialVersionUID = 7928873937822426611L;
	List<Conversation> conversationList;
	
	ConversationList(){
		this.conversationList = new ArrayList<>();
	}
	
	public void addConversation(Conversation conversation) {
		this.conversationList.add(conversation);
	}
	
	public List<Conversation> getConversationList() {
		return this.conversationList;
	}
	
	public int size(){
		return conversationList.size();
	}
	
	public Conversation get(int i) {
		return conversationList.get(i);
	}
}
