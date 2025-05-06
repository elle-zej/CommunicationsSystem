import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ConversationTest {
	
	@Test
	void testConversation() {
		List<String> testMembers = new ArrayList<>(Arrays.asList("Madison", "Lola", "Charlie"));
		List<String> testConversationHistory = new ArrayList<>();
		int testID = 1;
		
		Conversation conversation = new Conversation(testID, testMembers, testConversationHistory);
		
		assertNotNull(conversation);
	}
	
	@Test
	void testGetMembers() {
		List<String> testMembers = new ArrayList<>(Arrays.asList("Madison", "Lola", "Charlie"));
		List<String> members = new ArrayList<>(Arrays.asList("Madison", "Lola", "Charlie"));
		List<String> testConversationHistory = new ArrayList<>();
		int testID = 1;
		
		Conversation conversation = new Conversation(testID, testMembers, testConversationHistory);
		
		assertEquals(members, conversation.getMembersList());
	}
	
	@Test
	void testGetRecipients(){
		List<String> testRecipients = new ArrayList<>(Arrays.asList("MADISON", "CHARLIE"));
		List<String> testMembers = new ArrayList<>(Arrays.asList("MADISON", "LOLA", "CHARLIE"));
		User user = new User("Lola", "" , "" , "", Role.Employee);
		List<String> testConversationHistory = new ArrayList<>();
		int testID = 1;
		
		Conversation conversation = new Conversation(testID, testMembers, testConversationHistory);
		
		assertEquals(testRecipients, conversation.getRecipients(user));
	}
	
	@Test
	void testGetConversationID() {
		List<String> testConversationHistory = new ArrayList<>();
		int testID = 1;
		List<String> testMembers = new ArrayList<>(Arrays.asList("MADISON", "LOLA", "CHARLIE"));
		Conversation conversation = new Conversation(testID, testMembers, testConversationHistory);
		assertEquals("1", conversation.getConversationIDString());
		
	}
}
