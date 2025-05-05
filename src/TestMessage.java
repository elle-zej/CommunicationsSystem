import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class TestMessage {
	User user = new User("Remy", "EmpTest1", "ymer", "pass1", Role.Employee);
	List<String> recipients = new ArrayList<>();
	Message testMessage = new Message(user, recipients, "Testing123", Status.request);
	
	@Test
	void testGetContent() {
		assertEquals( "Testing123", testMessage.getContent());
	}

}
