import java.io.*;
import java.net.Authenticator;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.lang.model.element.NestingKind;
import javax.print.attribute.UnmodifiableSetException;
import java.net.InetAddress; 
public class Server {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ServerSocket server = null;
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			String IP = localhost.getHostAddress().trim();
			System.out.println(IP);
			server = new ServerSocket(1200);
			server.setReuseAddress(true);
				
			while(true) {
				Socket client = server.accept();
				
				//create an instance of the client handler
				ClientHandler clientHandler = new ClientHandler(client);
				
				//new thread for the client starts so the server  
				// handle other incoming requests
				new Thread(clientHandler).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
			}
		}
	}
		
}

 private static class ClientHandler implements Runnable {
	private final Socket clientSocket;
	//filename that stores the login credentials
	private static String loginInfoFile = "EmpInfo.txt";
	//file that stores all conversations
	private static String conversationHistory = "ConversationHistory.txt";
	
	// Constructor
	public ClientHandler(Socket socket)
	{
		this.clientSocket = socket;
	}

	public void run(){
		
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        User user = null;
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
			
			while(true) {
			//get user credentials
			Object recievedObject = in.readObject();
			
			//if the received object is an instance of User
			//we're Trying to Login
				if(recievedObject instanceof User) {
					 User userLogin = (User) recievedObject;
					//if authentication is successful
					if(authenticateUser(userLogin)) {
						//create message to indicate login was a success
						Message message = new Message("Login Successful!", Status.success);
						//get all the user info
						user = getUserInfo(userLogin);
						
						out.writeObject(message);
						out.writeObject(user);
						break;
					}
					else {
						Message message = new Message("Login failed", Status.fail);
						out.writeObject(message);
					}
				}
			}
			
		while(true) {
			Message request = (Message) in.readObject();
			String requestMsg = request.getContent();
			//
			switch(requestMsg) {
			case "sendMessageRequest":
				sendMessageHandler(user, out, in);
				break;
			case "viewConversationsRequest": 
				viewConversationsHandler(user);
				break;
			case "viewAllConversationsRequest": 
				System.out.println("hi");
				break;
			default: break;
			}
		}
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
					clientSocket.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//
    private boolean authenticateUser(User user) {
    	
    	try {
    		
    		//open the file with login credentials
    		File loginFile = new File(loginInfoFile);
    		
    		//line scanner to scan each line of the file
    		Scanner lineScanner = new Scanner(loginFile);
    		//iterate until the end of file
    		while(lineScanner.hasNextLine()) {
    			//get the lines in the file
    			String line = lineScanner.nextLine();
    			
    			//word scanner to scan individual words
    			//id and password separated by white space in txt file
    			Scanner wordScanner = new Scanner(line);
    			wordScanner.useDelimiter(",");
    			//first scan the id
    			String id = wordScanner.next().trim();
    			//scan the password
    			String pass = wordScanner.next().trim();
 
    			    			
    			//if id and password combo exist, return true
    			if(user.getUsername().equals(id) && user.getPassword().equals(pass)) {
    				lineScanner.close();
    				wordScanner.close();
    				return true;
    			}
    			wordScanner.close();
    		}
    		
    		lineScanner.close();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
    	//returns false if no such user name password combination found
		//return false;
    	return false;
    }
    
    private User getUserInfo(User user) {
    	User returnedUser = null;
    	try {
    		
    		//open the file with login credentials
    		File loginFile = new File(loginInfoFile);
    		
    		//line scanner to scan each line of the file
    		Scanner lineScanner = new Scanner(loginFile);
    		//iterate until the end of file
    		while(lineScanner.hasNextLine()) {
    			//get the lines in the file
    			String line = lineScanner.nextLine();
    			
    			//word scanner to scan individual words
    			//id and password separated by white space in txt file
    			Scanner wordScanner = new Scanner(line);
    			wordScanner.useDelimiter(",");
    			//first scan the id
    			String id = wordScanner.next().trim();
    			//scan the password
    			String pass = wordScanner.next().trim();
    			
    			
    			//if id and password combo exist, return true
    			if(user.getUsername().equals(id) && user.getPassword().equals(pass)) {
    				
    				String name = wordScanner.next().trim();
        			String roleString = wordScanner.next().trim();
        			
        			Role role = null;
        			
        			if(roleString.equals("EMPLOYEE")){
        				role = Role.Employee;
        			}
        			else {
        				role = Role.ITUser;
        			}
        				
        			String empId = wordScanner.next().trim();
        			
    				lineScanner.close();
    				wordScanner.close();
    				
    				returnedUser = new User(name, empId, id, pass, role);
    				return returnedUser;
    			}
    			wordScanner.close();
    		}
    		lineScanner.close();
    	}
    	catch (Exception e) {
			System.out.println("File not found in get User Info!");
		}
    	//returns false if no such user name password combination found
		return user;
    	
    }
    
    private void sendMessageHandler(User user,  ObjectOutputStream out,  ObjectInputStream in)
    		throws IOException, ClassNotFoundException {
    	Message sentMessage = (Message) in.readObject();
    	//process fields of sent message
    	User sender = sentMessage.getSender();
    	String nameOfSender = sender.getFullName();
    	List<String> receivers = sentMessage.getReceiver();
    	String timestamp = sentMessage.getTimestamp();
    	String msg = sentMessage.getContent();
    	
    	String newMsg = "[" + timestamp + "] " + nameOfSender + ": " + msg + "\n\n";
    	
    	//turn sender and recipients into members
    	List<String> membersOfMessage = sentMessage.getMembers();
    	membersOfMessage.sort(null);
    	//open conversation history file
    	File conversationHistoryFile = new File(conversationHistory);
    	Scanner lineScanner = new Scanner(conversationHistoryFile);
    	//process lines of file
    	
    	List<String> fileLines = new ArrayList<>();
    	while(lineScanner.hasNextLine()) {
    		//get the lines in the file
			String line = lineScanner.nextLine();
			fileLines.add(line);
    	}
    	List<String> membersList = new ArrayList<>();
    	for(int i =0; i<fileLines.size(); i++) {
    		String line = fileLines.get(i);
    		if(line.startsWith("Members: ")){
				String membersLine = line;
				String[] members = membersLine.split(":")[1].trim().split(",");
				Arrays.sort(members);
				membersList = Arrays.asList(members);
    		}
				//chat exists add to it
				if(membersOfMessage.equals(membersList)) {
					//fix below:
					while(i < fileLines.size() && !fileLines.get(i).equals("")) {
						System.out.println(fileLines.get(i));
						i++;
					}
					//when you find end of chat
					if (i< fileLines.size()) {
						fileLines.add(i, newMsg);
						System.out.println("hello");
					}
			    	FileWriter writer = new FileWriter(conversationHistory, false);
			    	for(int j = 0; j < fileLines.size(); j++) {
			    		writer.write(fileLines.get(j) + "\n");
			    	}
			    	
			    	writer.close();
			    	lineScanner.close();
			    	
			    	Message messageSent = new Message("Message sent", Status.success);
			    	out.writeObject(messageSent);
			    	
					return;
					}
				}	
	
    	//if not found
    	Conversation conversation = new Conversation(membersOfMessage);
    	conversation.addMessage(sentMessage);
    	//add to file lines
    	fileLines.add("Conversation " + conversation.getConversationIDString());
    	fileLines.add("Members:" + String.join("," , membersOfMessage));
    	fileLines.add("Chat:");
    	fileLines.add(newMsg);
    	
    	FileWriter writer = new FileWriter(conversationHistory, false);
    	for(int i = 0; i < fileLines.size(); i++) {
    		writer.write(fileLines.get(i) + "\n");
    	}
    	
    	writer.close();
    	lineScanner.close();
    	
    	Message messageSent = new Message("Message sent", Status.success);
    	out.writeObject(messageSent);
    	
    	}
  
    	
    	//check if conversation exists by checking if members match
    	
    	//if conversation exists, add to it
    	//else make new conversation
    }
    
    private static void viewConversationsHandler(User user) {System.out.println("hi");}

    }

