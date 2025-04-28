import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
	//defining attributes
	private User user;
	private GUI UI;
	
	Client(User user){
		this.user = user;
	}
	
	public User getUser() {return this.user;}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		InetAddress localhost = InetAddress.getLocalHost();
		String IP = localhost.getHostAddress().trim();
        Socket socket = new Socket(IP, 1200);
        
       //get object input and also output objects
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        User user = null;
        
        //login prompt
        boolean loggingIn = true;
        while(loggingIn) {
	        System.out.println("Enter username: ");
	        String username = sc.nextLine().trim();
	        System.out.println("Enter password: ");
	        String password = sc.nextLine().trim();
	        user = new User(username, password);
	        out.writeObject(user);
	        
	        //read in if success message or not
	        Message serverMessage = (Message) in.readObject();
	        System.out.println(serverMessage.getContent());
	        
	        if (serverMessage.getStatus().equals(Status.success)) {
	        	loggingIn = false;
	        }
        }
        
        User completeUser = (User) in.readObject();
        //get user's role
        Role role = completeUser.getRole();
        
        //if role is Employee
        if(role.equals(Role.Employee)) {
            while(true) {
            	System.out.println("Enter choice:"
            			+ "\n1. Send Message"
            			+ "\n2. View Conversations");
            	int choice = sc.nextInt();
            	//consume \n
            	sc.nextLine();
            	
            	switch(choice) {
            	case 1: onSendMessage(completeUser,sc, out, in); break;
            	case 2: onViewConversations(completeUser, sc, out, in); break;
            	}
            	
            	
            }
            
        }
        //if role is ITUser
        else {
	        while(true) {
	        	System.out.println("Enter choice:"
	        			+ "1. Send Message"
	        			+ "2. View Conversations"
	        			+ "3. View ALL Conversations");
	        	int choice = sc.nextInt();
	        	//consume \n
	        	sc.nextLine();
	        	
	        	switch(choice) {
	        	case 1: onSendMessage(completeUser, sc, out, in); break;
	        	case 2: onViewConversations(completeUser, sc, out, in); break;
	        	case 3: onViewAllConversations(completeUser, sc, out, in); break;
				}
			}
		}

	}
	
	private static void onSendMessage(User user, Scanner sc, ObjectOutputStream out, ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		List<String> members = new ArrayList<String>();
		//send request to server
		Message sendMessageRequest = new Message(user, "sendMessageRequest", Status.request);
		out.writeObject(sendMessageRequest);
		out.flush();
		System.out.println("Enter recipients separated by commas: ");
		//ASCII?
		String input = sc.nextLine();
		input = input.toUpperCase();
		//handle incorrect input
		String[] recipients = input.split(",");
		//create list of recipients
		for(int i =0; i< recipients.length; i++) {
			members.add(recipients[i]);
		}
		
		System.out.println("Enter message: ");
		String msg = sc.nextLine();
		//send message to server
		Message message = new Message(user, members, msg, Status.request);
		out.writeObject(message);
		out.flush();
		//server sends message confirmation
		Message serverMessage = (Message) in.readObject();
		//GUI pop-up
		if(serverMessage.getStatus().equals(Status.fail)){
			System.out.println(serverMessage.getContent());
		}
		
		System.out.println("Enter 1 to return to main menu: ");
		
		int choice = sc.nextInt(); sc.nextLine(); 
		while(true) {
			if(choice == 1) {
				return;
			}
			else {
				System.out.println("Enter 1 to exit");
				choice = sc.nextInt(); sc.nextLine(); 
			}
		}
	}

	private static void onViewConversations(User user, Scanner sc, ObjectOutputStream out, ObjectInputStream in) 
			throws IOException, ClassNotFoundException {
		//Send request to viewConversations
		Message viewConversationsRequest = new Message(user, "viewConversationsRequest", Status.request);
		out.writeObject(viewConversationsRequest);
		out.flush();
		//server sends message confirmation
		Message serverMessage = (Message) in.readObject();
		if(serverMessage.getStatus().equals(Status.fail)) {
			System.out.println(serverMessage.getContent());
		}
		//take in all conversations
		@SuppressWarnings("unchecked")
		List<Conversation> conversations = (List<Conversation>) in.readObject();
		
		System.out.println("Chats: ");
		for(int i =0; i < conversations.size(); i++) {
			System.out.println(i + conversations.get(i).getMembersString());
		}
		
		int conversationChoice = sc.nextInt(); sc.nextLine();		
		//print all elements of the conversation
		System.out.println("Conversation " + conversations.get(conversationChoice).getConversationIDString() + "\n");
		System.out.println(conversations.get(conversationChoice).getMessagesString() + "\n\n");
		System.out.println("Enter: \n "
				+ "1. to send message \n"
				+ "2. return to main menu");
		//check user input
		int choice;
		while(true) {
			if (sc.hasNextInt()) {
				choice = sc.nextInt();
				//consume next line character
				sc.nextLine();
				if (choice == 1 || choice ==2) {
					break;
				}
				else {
					System.out.println("Error: Enter a number 1 or 2: ");
				}
			}
			else {
				System.out.println("Error: Enter a number 1 or 2: ");
			}
		}
		
		while(true) {
			if(choice == 1) {
				System.out.println("Enter message: ");
				String msg = sc.nextLine().trim();
				List<String> receivers = conversations.get(conversationChoice).getRecipients(user);
				Message message = new Message(user, receivers, msg, Status.request);
				out.writeObject(message);
				out.flush();
				//server sends message confirmation
				Message serverConfirmationMessage = (Message) in.readObject();
				//GUI pop-up
				if(serverConfirmationMessage.getStatus().equals(Status.fail)){
					System.out.println(serverConfirmationMessage.getContent());
				}
				}
			if(choice == 2){return;}
			else {
				System.out.println("Enter 1 or 2");
				choice = sc.nextInt();
				//consume next line character
				sc.nextLine();
			}
		}
		
	}
	
	public static void onViewAllConversations(User user, Scanner sc, ObjectOutputStream out, ObjectInputStream in)
		throws IOException, ClassNotFoundException {
		//Send request to viewConversations
		Message viewConversationsRequest = new Message(user, "viewAllConversationsRequest", Status.request);
		out.writeObject(viewConversationsRequest);
		out.flush();
		//server sends message confirmation
		Message serverMessage = (Message) in.readObject();
		if(serverMessage.getStatus().equals(Status.fail)) {
			System.out.println(serverMessage.getContent());
		}
		
		@SuppressWarnings("unchecked")
		List<Conversation> conversations = (List<Conversation>) in.readObject();
		
		System.out.println("Chats: ");
		for(int i =0; i < conversations.size(); i++) {
			System.out.println(i + conversations.get(i).getMembersString());
		}
		
		int conversationChoice = sc.nextInt(); sc.nextLine();
		
		System.out.println("Conversation " + conversations.get(conversationChoice).getConversationIDString() + "\n");
		System.out.println(conversations.get(conversationChoice).getMessagesString() + "\n\n");
		
		System.out.println("Enter 1 to return to main menu: ");
		
		int choice = sc.nextInt(); sc.nextLine(); 
		while(true) {
			if(choice == 1) {
				return;
			}
			else {
				System.out.println("Enter 1 to exit");
				choice = sc.nextInt(); sc.nextLine(); 
			}
		}
		
	}
		
	
	//get user unique ID?
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



