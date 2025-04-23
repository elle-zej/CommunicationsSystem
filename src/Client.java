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
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
        Socket socket = new Socket("localhost", 1200);
        
       //get object input and also output objects
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        User user = null;
        //login prompt
        boolean loggingIn = true;
        while(loggingIn) {
	        System.out.println("Enter username: ");
	        String username = sc.nextLine().trim();
	        System.out.println("Enter password");
	        String password = sc.nextLine().trim();
	        user = new User(username, password);
	        objectOutputStream.writeObject(user);
	        
	        //read in if success message or not
	        Message serverMessage = (Message) in.readObject();
	        System.out.println(serverMessage.getContent());
	        
	        if (serverMessage.getStatus().equals(Status.success)) {
	        	loggingIn = false;
	        }
        }
        
        
        Client client = new Client(user);
        
        
        while(true) {
        	System.out.println("Enter choice:"
        			+ "1. Send Message"
        			+ "2. View Conversations");
        	int choice = sc.nextInt();
        	
        	switch(choice) {
        	case 1: client.doSendMessage();
        	case 2: client.doViewConversations();
        	}
        	
        	
        }
        
        
        

	
	}
	
	private void doSendMessage() {
		
		System.out.println("Hi");
	}

	private void doViewConversations() {}
}



