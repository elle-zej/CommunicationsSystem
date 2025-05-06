import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {
	
	User user1 = new User("Jezelle", "EMP0", "Jeze3", "house", Role.Employee);

	@Test
	void testUser() {
		assertNotNull(user1);
	}
	@Test
	void testGetFullname() {
		assertEquals("Jezelle", user1.getFullName());
	}
	
	@Test
	void testGetEmployeeID() {
		assertEquals("EMP0", user1.getEmployeeID());
	}
	
	@Test
	void testGetUsername() {
		assertEquals("Jeze3", user1.getUsername());
	}
	
	@Test
	void testGetRole() {
		assertEquals(Role.Employee, user1.getRole());
	}
	
}
