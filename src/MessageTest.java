import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MessageTest {
	User user = new User("Remy", "EmpTest1", "ymer", "pass1", Role.Employee);
	List<String> recipients = new ArrayList<>();
	Message testMessage = new Message(user, recipients, "Testing123", Status.request);
	
	@Test
	void testMessage() {
		assertNotNull(testMessage);
	}
	
	@Test
	void testGetContent() {
		assertEquals( "Testing123", testMessage.getContent());
	}
	
	@Test
	void testGetMembers() {
		List<String> members = new ArrayList<>(Arrays.asList("DJANGO", "EMILE" , "REMY"));
		List<String> testRecipients = new ArrayList<>(Arrays.asList("Django", "Emile"));
		Message testMessage = new Message(user, testRecipients, "Testing123", Status.request);
	
		assertEquals(members, testMessage.getMembers());
	}
	
	@Test
	void testGetSender(){
		assertEquals(user, testMessage.getSender());
	}
	
	@Test
	void testGetStatus() {
		assertEquals(Status.request, testMessage.getStatus());
	}
}
