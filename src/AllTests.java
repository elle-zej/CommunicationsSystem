import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ ConversationTest.class, MessageTest.class, UserTest.class })
public class AllTests {

}
