import java.io.*;
import java.net.Socket;
import java.nio.file.attribute.UserDefinedFileAttributeView;
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
        Socket socket = new Socket("localhost", 1200);
        
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
            			+ "1. Send Message"
            			+ "2. View Conversations");
            	int choice = sc.nextInt();
            	//consume \n
            	sc.nextLine();
            	
            	switch(choice) {
            	case 1: onSendMessage(completeUser, out, in); break;
            	case 2: doViewConversations(completeUser); break;
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
	        	case 1: onSendMessage(completeUser, out, in); break;
	        	case 2: doViewConversations(completeUser); break;
				}
			}
		}
        
        
        

	
	}
	
	private static void onSendMessage(User user, ObjectOutputStream out, ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		List<String> members = new ArrayList<String>();
		
		System.out.println("Enter recipients separated by commas: ");
		//ASCII?
		String input = sc.nextLine();
		input.toUpperCase();
		//handle incorrect input
		String[] recipients = input.split(",");
		//create list of recipients
		for(int i =0; i< recipients.length; i++) {
			members.add(recipients[i]);
		}
		
		System.out.println("Enter message: ");
		String msg = sc.nextLine();
		
		Message message = new Message(user, members, msg, Status.request);
		out.writeObject(message);
		out.flush();
		
		Message serverMessage = (Message) in.readObject();
		//GUI pop-up
		if(serverMessage.getStatus().equals(Status.fail)){
			System.out.println(serverMessage.getContent());
		}
		
		
	}

	private static void doViewConversations(User user) {}
	
	//get user unique ID
	private static int getID(String name) {
			  int id = 0;
			  for(int i=0;i< name.length(); i++){
			  	char character = name.charAt(i);
			    int ascii = (int) character;
			  	id += ascii;
			 	 }
			  return id;
	}
	
}



