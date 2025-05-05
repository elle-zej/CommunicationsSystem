import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TestConversation {
	
	@Test
	void testGetMembers() {
		List<String> testMembers = new ArrayList<>();
		testMembers.add("Madison");
		testMembers.add("Lola");
		testMembers.add("Charlie");
		List<String> members = new ArrayList<>(Arrays.asList("Madison", "Lola", "Charlie"));
		List<String> testConversationHistory = new ArrayList<>();
		int testID = 1;
		
		Conversation conversation = new Conversation(testID, testMembers, testConversationHistory);
		
		assertEquals(members, conversation.getMembersList());
	}

}
