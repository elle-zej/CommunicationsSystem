import java.io.*;
import java.net.InetAddress;
import java.net.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
	// defining attributes
	private User user;
	// private GUI UI;
	private Socket socket = null;

	// ----------------------------------Driver----------------------------------------//
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Client client = new Client();
		client.runClientLoop();		
	}

	public void runClientLoop() throws IOException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		InetAddress localhost = InetAddress.getLocalHost();
		String IP = localhost.getHostAddress().trim();

		Socket socket = new Socket("134.154.61.104", 1200);
		this.socket = socket;
		// get object input and also output objects
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		// login and after successful login server sends you a user object with complete
		// info
		Boolean loggingIn = true;

		while (loggingIn) {
			GUI.loginWindow(out, in);
			// login(out, in);
			Message serverMessage = (Message) in.readObject();

			if (serverMessage.getStatus().equals(Status.success)) {
				GUI.responseMessage(serverMessage.getContent());
				loggingIn = false;
			} else {
				GUI.responseMessage(serverMessage.getContent());
			}

		}

		User completeUser = (User) in.readObject();

		// open menu after successful login
		userSession(completeUser, sc, out, in);
		GUI.responseMessage("logout successful");
		sc.close();

	}

	// user is provided with options
	public void userSession(User completeUser, Scanner sc, ObjectOutputStream out, ObjectInputStream in)
			throws ClassNotFoundException, IOException {
		// get user's role
		Role role = completeUser.getRole();

		boolean loggedIn = true;

		// logged in so now main menu of the program
		while (loggedIn) {
			// if role is Employee
			if (role.equals(Role.Employee)) {

				System.out.println("Enter choice:" + "\n1. Send Message" + "\n2. View Conversations"
						+ "\n3. View Online" + "\n4. Log Out");
				int choice = sc.nextInt();
				// consume \n
				sc.nextLine();

				switch (choice) {
				case 1:
					onSendMessage(completeUser, sc, out, in);
					break;
				case 2:
					onViewConversations(completeUser, sc, out, in);
					break;
				case 3:
					onViewOnline(completeUser, sc, out, in);
					break;
				case 4:
					onLogOut(completeUser, sc, out, in);
					loggedIn = false;
					return;
				default:
					System.out.println("Enter a choice 1-4");
					break;
				}

			}
			// if role is ITUser
			else {

				System.out.println("Enter choice:" + "\n1. Send Message" + "\n2. View Conversations"
						+ "\n3. View ALL Conversations" + "\n4. View Online" + "\n5. Log Out");

				int choice = sc.nextInt();
				// consume \n
				sc.nextLine();

				switch (choice) {
				case 1:
					onSendMessage(completeUser, sc, out, in);
					break;
				case 2:
					onViewConversations(completeUser, sc, out, in);
					break;
				case 3:
					onViewAllConversations(completeUser, sc, out, in);
					break;
				case 4:
					onViewOnline(completeUser, sc, out, in);
					break;
				case 5:
					onLogOut(completeUser, sc, out, in);
					System.out.println("Successful logout");
					loggedIn = false;
					return;
				default:
					System.out.println("Enter a choice 1-5");
					break;
				}
			}
		}
	}

	public void login(ObjectOutputStream out, ObjectInputStream in) {
		Scanner sc = new Scanner(System.in);
		boolean loggingIn = true;
		while (loggingIn) {
			// prompt for user name and password
			System.out.println("Enter username: ");
			String username = sc.nextLine().trim();
			System.out.println("Enter password: ");
			String password = sc.nextLine().trim();
			// create a user with entered user name and password
			user = new User(username, password);
			try {
				out.writeObject(user);
				// read in if success message or not
				Message serverMessage = (Message) in.readObject();
				System.out.println(serverMessage.getContent() + "\n");
				if (serverMessage.getStatus().equals(Status.success)) {
					loggingIn = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	public static void onViewOnline(User completeUser, Scanner sc, ObjectOutputStream out, ObjectInputStream in) {
		List<User> onlineUsers = new ArrayList<User>();

		// request the server to view online people
		Message viewOnlineRequest = new Message(completeUser, "viewOnlineRequest", Status.request);
		try {
			out.writeObject(viewOnlineRequest);
			out.flush();

			onlineUsers = (List<User>) in.readObject();

			if (onlineUsers.isEmpty()) {
				System.out.println("No Users Online, You are the only one!");
			} else {
				System.out.println("Users online: ");
				int i = 1;
				for (User onlineUser : onlineUsers) {
					System.out.println(i + ") " + onlineUser.getFullName());
					i++;
				}

				// (i - 1) done because incremented one more time before out of for loop
				System.out.print("Choose who to chat with ( 1 - " + (i - 1) + " ) or -1 to exit \n");
				int choice = sc.nextInt();

				// consume the space after the integer
				sc.nextLine();

				if (choice == -1) {
					return;
				}

				String recipient = onlineUsers.get(choice - 1).getFullName();
				System.out.println("Starting a chat with " + recipient + ": ");
				// load the previous conversations
				onViewSpecificConversation(completeUser, recipient, sc, out, in);
				// start chat
				// startChatSession(completeUser, recipient, sc, out, in);

			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void startChatSession(User completeUser, String recipient, Scanner sc, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {

		Message sendMessageRequest = new Message("sendMessageRequest", Status.request);
		out.writeObject(sendMessageRequest);
		out.flush();

		System.out.println("Enter message: ");
		String msg = sc.nextLine();
		// send message to server
		List<String> recipients = new ArrayList<>();
		recipients.add(recipient);
		Message message = new Message(completeUser, recipients, msg, Status.request);
		out.writeObject(message);
		out.flush();

	}

	public static void onLogOut(User user, Scanner sc, ObjectOutputStream out, ObjectInputStream in)
			throws IOException {
		Message sendMessageRequest = new Message(user, "logOutRequest", Status.request);
		out.writeObject(sendMessageRequest);
		out.flush();

	}

	private static void onSendMessage(User user, Scanner sc, ObjectOutputStream out, ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		List<String> members = new ArrayList<String>();
		// send request to server
		Message sendMessageRequest = new Message(user, "sendMessageRequest", Status.request);
		out.writeObject(sendMessageRequest);
		out.flush();

		System.out.println("Enter recipients separated by commas: ");
		// ASCII?
		String input = sc.nextLine();
		input = input.toUpperCase();
		// handle incorrect input
		String[] recipients = input.split(",");
		// create list of recipients
		for (int i = 0; i < recipients.length; i++) {
			members.add(recipients[i]);
		}

		System.out.println("Enter message: ");
		String msg = sc.nextLine();
		// confirm to send message
		System.out.println("Recipient(s): " + members);
		System.out.println("Message: " + msg);
		System.out.println("Enter 1 to send or -1 to exit");

		int choice = sc.nextInt();
		sc.nextLine();

		while (true) {
			if (choice == 1) {
				Message message = new Message(user, members, msg, Status.request);
				out.writeObject(message);
				out.flush();
				System.out.println("Message sent \n");
				return;
			} else if (choice == -1) {
				Message message = new Message(user, members, "MessageAbandoned", Status.fail);
				out.writeObject(message);
				out.flush();
				return;
			} else {
				System.out.println("Enter 1 to send or -1 to exit");
				choice = sc.nextInt();
				sc.nextLine();
			}

		}
	}

	private static void onViewSpecificConversation(User user, String recepient, Scanner sc, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		// Send request to viewConversations
		Message viewConversationsRequest = new Message(user, "viewConversationsRequest", Status.request);
		out.writeObject(viewConversationsRequest);
		out.flush();

		// take in all conversations
		ConversationList conversations = (ConversationList) in.readObject();
		int conversationChoice = -1;

		System.out.println("Messages ");
		// get users each convo
		for (int i = 0; i < conversations.size(); i++) {
			// iterate over each convo of each members
			List<String> members = conversations.get(i).getRecipients(user);
			String messageReceivedByString = members.get(0).toUpperCase();

			if (members.size() == 1 && (messageReceivedByString.equals(recepient.toUpperCase()))) {
				// System.out.println(conversations.get(i).getMessagesString());
				conversationChoice = i;
			}
		}
		if(conversationChoice == -1) {
			System.out.println("You guys have no conversation history!!!");
		}
		else {
			// print all elements of the conversation
			System.out.println("\nConversation " + conversations.get(conversationChoice).getConversationIDString());
			System.out.println(conversations.get(conversationChoice).getMessagesString());
		}
		
		System.out.println("Enter: \n" + "1 to send message \n" + "-1 return to main menu");
		// check user input
		int choice;
		while (true) {
			if (sc.hasNextInt()) {
				choice = sc.nextInt();
				// consume next line character

				// sc.nextLine();
				if (choice == 1 || choice == -1) {
					break;
				} else {
					System.out.println("Error: Enter a number 1 or -1: ");
				}
			} else {
				System.out.println("Error: Enter a number 1 or -1: ");
			}
		}

		// server sends message confirmation
//		Message serverMessage = (Message) in.readObject();
//		if(serverMessage.getStatus().equals(Status.fail)) {
//			System.out.println(serverMessage.getContent());
//		}

		while (true) {
			if (choice == 1) {
				
				String msg = "This was supposed to be a live chat message";
				System.out.println("Auto reply: " + msg);
				
				List<String> receivers = conversations.get(conversationChoice).getRecipients(user);
				for (int i = 0; i < receivers.size(); i++) {
					System.out.println(receivers.get(i));
				}
				Message message = new Message(user, receivers, msg, Status.request);
				sendMessage(message, out, in);
				return;
			} else if (choice == -1) {
				return;
			} else {
				System.out.println("Enter 1 or -1");
				choice = sc.nextInt();
				// consume next line character
				sc.nextLine();
			}
		}

	}

	private static void onViewConversations(User user, Scanner sc, ObjectOutputStream out, ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		// Send request to viewConversations
		Message viewConversationsRequest = new Message(user, "viewConversationsRequest", Status.request);
		out.writeObject(viewConversationsRequest);
		out.flush();
		// server sends message confirmation
//		Message serverMessage = (Message) in.readObject();

		// take in all conversations
		// @SuppressWarnings("unchecked")
		ConversationList conversations = (ConversationList) in.readObject();

		System.out.println("Chats: ");
		for (int i = 0; i < conversations.size(); i++) {
			System.out.println(i + " " + conversations.get(i).getRecipientsString(user));
		}
		System.out.println("Enter number to view chat or -1 to exit:");

		int conversationChoice = sc.nextInt();
		sc.nextLine();

		if (conversationChoice == -1) {
			return;
		}
		// print all elements of the conversation
		System.out.println("\nConversation " + conversations.get(conversationChoice).getConversationIDString());
		System.out.println(conversations.get(conversationChoice).getMessagesString());
		System.out.println("Enter: \n" + "1 to send message \n" + "-1 return to main menu");
		// check user input
		int choice;
		while (true) {
			if (sc.hasNextInt()) {
				choice = sc.nextInt();
				// consume next line character
				sc.nextLine();
				if (choice == 1 || choice == -1) {
					break;
				} else {
					System.out.println("Error: Enter a number 1 or -1: ");
				}
			} else {
				System.out.println("Error: Enter a number 1 or -1: ");
			}
		}

//		 server sends message confirmation
//		Message serverMessage = (Message) in.readObject();
//		if(serverMessage.getStatus().equals(Status.fail)) {
//			System.out.println(serverMessage.getContent());
//		}

		while (true) {
			if (choice == 1) {
				System.out.println("Enter message: ");
				String msg = sc.nextLine().trim();
				List<String> receivers = conversations.get(conversationChoice).getRecipients(user);
				for (int i = 0; i < receivers.size(); i++) {
					System.out.println(receivers.get(i));
				}
				Message message = new Message(user, receivers, msg, Status.request);
				sendMessage(message, out, in);
				return;
			} else if (choice == -1) {
				return;
			} else {
				System.out.println("Enter 1 or -1");
				choice = sc.nextInt();
				// consume next line character
				sc.nextLine();
			}
		}

	}

	public static void onViewAllConversations(User user, Scanner sc, ObjectOutputStream out, ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		// Send request to viewConversations
		Message viewConversationsRequest = new Message(user, "viewAllConversationsRequest", Status.request);
		out.writeObject(viewConversationsRequest);
		out.flush();
		// server sends message confirmation
//		Message serverMessage = (Message) in.readObject();
//		if(serverMessage.getStatus().equals(Status.fail)) {
//			System.out.println(serverMessage.getContent());
//		}

		ConversationList conversations = (ConversationList) in.readObject();

		System.out.println("Chats: ");
		for (int i = 0; i < conversations.size(); i++) {
			System.out.println(i + " " + conversations.get(i).getMembersString());
		}

		System.out.println("Enter number to view chat or -1 to exit:");

		int conversationChoice = sc.nextInt();
		sc.nextLine();

		if (conversationChoice == -1) {
			return;
		}

		System.out.println("Conversation " + conversations.get(conversationChoice).getConversationIDString() + "\n");
		System.out.println(conversations.get(conversationChoice).getMessagesString());

		System.out.println("Enter: \n" + "1 to send message \n" + "-1 return to main menu");
		// check user input
		int choice;
		while (true) {
			if (sc.hasNextInt()) {
				choice = sc.nextInt();
				// consume next line character
				sc.nextLine();
				if (choice == 1 || choice == -1) {
					break;
				} else {
					System.out.println("Error: Enter a number 1 or -1: ");
				}
			} else {
				System.out.println("Error: Enter a number 1 or -1: ");
			}
		}

	}

	public static void sendMessage(Message message, ObjectOutputStream out, ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		Message sendMessageRequest = new Message("sendMessageRequest", Status.request);

		out.writeObject(sendMessageRequest);
		out.flush();

		out.writeObject(message);
		out.flush();

//		Message serverMessage = (Message) in.readObject();
//		// GUI pop-up
//		if (serverMessage.getStatus().equals(Status.fail)) {
//			System.out.println(serverMessage.getContent());
//		}
	}

}
