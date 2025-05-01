import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.LoginContext;
import javax.swing.plaf.synth.SynthOptionPaneUI;

public class Client {
	// defining attributes
	private User user;
	private GUI UI;

	Client(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	private static class MessageListener implements Runnable {

		private User user;
		private ObjectOutputStream out;
		private ObjectInputStream in;

		public MessageListener(User user, ObjectOutputStream out, ObjectInputStream in) {
			this.user = user;
			this.out = out;
			this.in = in;
		}

		@Override
		public void run() {
			try {
				boolean done = false;
				while (!done) {
					Message receivedMessage = (Message) in.readObject();
					System.out.println("\n[Message from " + receivedMessage.getSender().getFullName() + "]: "
							+ receivedMessage.getContent());

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}
	//----------------------------------Driver----------------------------------------//
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		Client client = new Client(null);
		client.runClientLoop();

	}
	//----------------------------------------------------------------------------------
	
	public void login(ObjectOutputStream out, ObjectInputStream in) {
		Scanner sc = new Scanner(System.in);
		boolean loggingIn = true;
		while (loggingIn) {
			System.out.println("Enter username: ");
			String username = sc.nextLine().trim();
			System.out.println("Enter password: ");
			String password = sc.nextLine().trim();
			user = new User(username, password);
			try {
				out.writeObject(user);
				// read in if success message or not
				Message serverMessage = (Message) in.readObject();
				System.out.println(serverMessage.getContent());
				if (serverMessage.getStatus().equals(Status.success)) {
					loggingIn = false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void runClientLoop() throws IOException, ClassNotFoundException {

		Scanner sc = new Scanner(System.in);
		InetAddress localhost = InetAddress.getLocalHost();
		String IP = localhost.getHostAddress().trim();

		while (true) {
			Socket socket = new Socket(IP, 1200);
			// get object input and also output objects
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			User user = null;

			// login
			login(out, in);

			User completeUser = (User) in.readObject();
			userSession(completeUser, sc, out, in);

			System.out.println("SuccessFull logout");
			System.out.println("Do you want to login in ? (Y/N): ");
			String choice = sc.nextLine();
			if (choice.toLowerCase().equals("n")) {
				System.out.println("Terminating the application");
				break;
			} else {
				System.out.println("Welcome to the login page again!! \n");
			}
		}
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
					loggedIn = false;
					break;

				}
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
				System.out.println("Choose the online user to chat with: ");
				int i = 1;
				for (User onlineUser : onlineUsers) {
					System.out.println(i + ") " + onlineUser.getFullName());
					i++;
				}

				// (i - 1) done because incremented one more time before out of for loop
				System.out.print("Choose who to chat with ( 1 - " + (i - 1) + " ) : ");
				int choice = sc.nextInt();

				// consume the space after the int
				sc.nextLine();

				System.out.println();

				String recipient = onlineUsers.get(choice - 1).getFullName();

				System.out.println("Starting a chat with " + recipient + ": ");

			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void startChatSessioin(User completeUser, Scanner sc, ObjectOutputStream out, ObjectInputStream in) {

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
		// send message to server
		Message message = new Message(user, members, msg, Status.request);
		out.writeObject(message);
		out.flush();
		// server sends message confirmation
		Message serverMessage = (Message) in.readObject();
		// GUI pop-up
		if (serverMessage.getStatus().equals(Status.fail)) {
			System.out.println(serverMessage.getContent());
		}

		System.out.println("Enter 1 to return to main menu: ");

		int choice = sc.nextInt();
		sc.nextLine();
		while (true) {
			if (choice == 1) {
				return;
			} else {
				System.out.println("Enter 1 to exit");
				choice = sc.nextInt();
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

		// take in all conversations
		// @SuppressWarnings("unchecked")
		List<Conversation> conversations = (List<Conversation>) in.readObject();

		System.out.println("Chats: ");
		for (int i = 0; i < conversations.size(); i++) {
			System.out.println(i + " " + conversations.get(i).getRecipientsString(user));
		}

		int conversationChoice = sc.nextInt();
		sc.nextLine();
		// print all elements of the conversation
		System.out.println("\nConversation " + conversations.get(conversationChoice).getConversationIDString());
		System.out.println(conversations.get(conversationChoice).getMessagesString());
		System.out.println("Enter: \n" + "1 to send message \n" + "2 return to main menu");
		// check user input
		int choice;
		while (true) {
			if (sc.hasNextInt()) {
				choice = sc.nextInt();
				// consume next line character
				sc.nextLine();
				if (choice == 1 || choice == 2) {
					break;
				} else {
					System.out.println("Error: Enter a number 1 or 2: ");
				}
			} else {
				System.out.println("Error: Enter a number 1 or 2: ");
			}
		}

		// server sends message confirmation
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
			} else if (choice == 2) {
				return;
			} else {
				System.out.println("Enter 1 or 2");
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

		@SuppressWarnings("unchecked")
		List<Conversation> conversations = (List<Conversation>) in.readObject();

		System.out.println("Chats: ");
		for (int i = 0; i < conversations.size(); i++) {
			System.out.println(i + " " + conversations.get(i).getMembersString());
		}

		int conversationChoice = sc.nextInt();
		sc.nextLine();

		System.out.println("Conversation " + conversations.get(conversationChoice).getConversationIDString() + "\n");
		System.out.println(conversations.get(conversationChoice).getMessagesString());

		System.out.println("Enter 1 to return to main menu: ");

		int choice = sc.nextInt();
		sc.nextLine();
		while (true) {
			if (choice == 1) {
				return;
			} else {
				System.out.println("Enter 1 to exit");
				choice = sc.nextInt();
				sc.nextLine();
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

		Message serverMessage = (Message) in.readObject();
		// GUI pop-up
		if (serverMessage.getStatus().equals(Status.fail)) {
			System.out.println(serverMessage.getContent());
		}

	}

	// get user unique ID?
//	private static int getID(String name) {
//			  int id = 0;
//			  for(int i=0;i< name.length(); i++){
//			  	char character = name.charAt(i);
//			    int ascii = (int) character;
//			  	id += ascii;
//			 	 }
//			  return id;
//	}
//	
}
