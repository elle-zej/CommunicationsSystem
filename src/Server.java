import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.net.InetAddress;

public class Server {
	//will store the client name and their output stream so, messages can be redirected to through the correct stream
	private static HashMap<String, ObjectOutputStream> streamsInfo = new HashMap<String, ObjectOutputStream>();
  
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		ServerSocket server = null;
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			String IP = localhost.getHostAddress().trim();
			System.out.println(IP);
			server = new ServerSocket(1200);
			server.setReuseAddress(true);

			while (true) {
				Socket client = server.accept();

				// create an instance of the client handler
				ClientHandler clientHandler = new ClientHandler(client);

				// new thread for the client starts so the server
				// handle other incoming requests
				new Thread(clientHandler).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		// filename that stores the login credentials
		private static String loginInfoFile = "EmpInfo.txt";
		// file that stores all conversations
		private static String conversationHistory = "ConversationHistory.txt";

		// Constructor
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {

			ObjectOutputStream out = null;
			ObjectInputStream in = null;
			User user = null;
			
			try {
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				//loop for logging in
				while (true) {
					// get user credentials
					Object recievedObject = in.readObject();

					// if the received object is an instance of User
					// we're Trying to Login
					if (recievedObject instanceof User) {
						User userLogin = (User) recievedObject;
						// if authentication is successful
						if (authenticateUser(userLogin)) {
							// create message to indicate login was a success
							Message message = new Message("Login Successful!", Status.success);
							// get all the user info
							user = getUserInfo(userLogin);
              
             //since user successfully logged in,
						//add them(username/objectoutputStream) as they are a valid/authenticated member
						streamsInfo.put(user.getFullName(), out);
						//delete this 
						System.out.println("Online on the server: " + streamsInfo.keySet());
						
							// send both objects back to client
							out.writeObject(message);
							out.writeObject(user);
							break;
						} else {
							Message message = new Message("Login failed", Status.fail);
							out.writeObject(message);
						}
					}
				}
				//loop after logging in successfully
				while (true) {
					//read in the client's request
					Message request = (Message) in.readObject();
					String requestMsg = request.getContent();
					//identify which request client is making
					switch (requestMsg) {
					case "sendMessageRequest":
						sendMessageHandler(user, out, in);
						break;
					case "viewConversationsRequest":
						viewConversationsHandler(user, out, in);
						break;
					case "viewAllConversationsRequest":
						System.out.println("hi");
						break;
					default:
						break;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
						clientSocket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private boolean authenticateUser(User user) {

			try {

				// open the file with login credentials
				File loginFile = new File(loginInfoFile);

				// line scanner to scan each line of the file
				Scanner lineScanner = new Scanner(loginFile);
				// iterate until the end of file
				while (lineScanner.hasNextLine()) {
					// get the lines in the file
					String line = lineScanner.nextLine();

					// word scanner to scan individual words
					// id and password separated by white space in txt file
					Scanner wordScanner = new Scanner(line);
					wordScanner.useDelimiter(",");
					// first scan the id
					String id = wordScanner.next().trim();
					// scan the password
					String pass = wordScanner.next().trim();

					// if id and password combo exist, return true
					if (user.getUsername().equals(id) && user.getPassword().equals(pass)) {
						lineScanner.close();
						wordScanner.close();
						return true;
					}
					wordScanner.close();
				}

				lineScanner.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// returns false if no such user name password combination found
			// return false;
			return false;
		}
    
    private void sendChat(Message sentMessage) throws IOException, ClassNotFoundException {
    	List<String> receivers = sentMessage.getReceiver();
    	receivers.forEach((String reciever) -> {
    		//if the receiver is currently online, send the message through the stream
    		if(streamsInfo.containsKey(reciever.toUpperCase())) {
    			//get the output stream of the receiver to send message
    			ObjectOutputStream out = streamsInfo.get(reciever.toUpperCase());
    			try {
					out.writeObject(sentMessage);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	});
    	//returns control to where sendChat() was called
    	return;
    }

		private User getUserInfo(User user) {
			User returnedUser = null;
			try {

				// open the file with login credentials
				File loginFile = new File(loginInfoFile);

				// line scanner to scan each line of the file
				Scanner lineScanner = new Scanner(loginFile);
				// iterate until the end of file
				while (lineScanner.hasNextLine()) {
					// get the lines in the file
					String line = lineScanner.nextLine();

					// word scanner to scan individual words
					// id and password separated by white space in txt file
					Scanner wordScanner = new Scanner(line);
					wordScanner.useDelimiter(",");
					// first scan the id
					String id = wordScanner.next().trim();
					// scan the password
					String pass = wordScanner.next().trim();

					// if id and password combo exist, return true
					if (user.getUsername().equals(id) && user.getPassword().equals(pass)) {

						String name = wordScanner.next().trim();
						String roleString = wordScanner.next().trim();

						Role role = null;

						if (roleString.equals("EMPLOYEE")) {
							role = Role.Employee;
						} else {
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
			} catch (Exception e) {
				System.out.println("File not found in get User Info!");
			}
			// returns false if no such user name password combination found
			return user;

		}

		private void sendMessageHandler(User user, ObjectOutputStream out, ObjectInputStream in)
				throws IOException, ClassNotFoundException {
			//take in message from client
			Message sentMessage = (Message) in.readObject();
      
      
      //if the recievers are online, message sent to them 
    	//sentMessage contains all info about sender and reciever,
    	//so only sentMessage required as the parameter
    	
    	sendChat(sentMessage);
      
			// process fields of sent message
			User sender = sentMessage.getSender();
			String nameOfSender = sender.getFullName();
			String timestamp = sentMessage.getTimestamp();
			String msg = sentMessage.getContent();
			
			//create new message to add to conversation history
			String newMsg = "[" + timestamp + "] " + nameOfSender + ": " + msg;

			// turn sender and recipients into members
			List<String> membersOfMessage = sentMessage.getMembers();
			//sort in alphabetical order
			membersOfMessage.sort(null);
			
			// open conversation history file
			File conversationHistoryFile = new File(conversationHistory);
			Scanner lineScanner = new Scanner(conversationHistoryFile);
			
			// process lines of file
			List<String> fileLines = new ArrayList<>();
			while (lineScanner.hasNextLine()) {
				// get all lines in the file
				String line = lineScanner.nextLine();
				fileLines.add(line);
			}
			
			List<String> membersList = new ArrayList<>();
			//go through each of the lines
			for (int i = 0; i < fileLines.size(); i++) {
				String line = fileLines.get(i);
				//extract members from file
				if (line.startsWith("Members: ")) {
					String membersLine = line;
					//take all members and store in array
					String[] members = membersLine.split(":")[1].trim().split(",");
					//sort the array in alphabetical order
					Arrays.sort(members);
					//turn array into list (for comparison of membersOfMessage sent by client)
					membersList = Arrays.asList(members);
				}
				
				//if members in storage match members sent
				if (membersOfMessage.equals(membersList)) {
					//go through the lines until blank line is found
					while (i < fileLines.size() && !fileLines.get(i).equals("")) {
						i++;
					}
					// when you find blank line, you have found the end of the chat
					if (i < fileLines.size()) {
						//add the new message to that line
						fileLines.add(i, newMsg);
					}
					else {
						fileLines.add(newMsg);
					}
					//write all the changes to the file
					FileWriter writer = new FileWriter(conversationHistory);
					for (int j = 0; j < fileLines.size(); j++) {
						writer.write(fileLines.get(j) + "\n");
					}

					writer.close();
					lineScanner.close();
					//send confirmation that message sent
					Message messageSent = new Message("Message sent", Status.success);
					out.writeObject(messageSent);

					return;
				}
			}

			// if not found
			Conversation conversation = new Conversation(membersOfMessage);
			//conversation.addMessage(sentMessage);
			// add to file lines
			fileLines.add("\nConversation " + conversation.getConversationIDString());
			fileLines.add("Members: " + String.join(",", membersOfMessage));
			fileLines.add("Chat:");
			fileLines.add(newMsg);

			FileWriter writer = new FileWriter(conversationHistory, false);
			for (int i = 0; i < fileLines.size(); i++) {
				writer.write(fileLines.get(i) + "\n");
			}

			writer.close();
			lineScanner.close();

			Message messageSent = new Message("Message sent", Status.success);
			out.writeObject(messageSent);

		}

	private static void viewConversationsHandler(User user, ObjectOutputStream out, ObjectInputStream in) 
			throws IOException, ClassNotFoundException{
		String name = user.getFullName().toUpperCase();
		
		// open conversation history file
		File conversationHistoryFile = new File(conversationHistory);
		Scanner lineScanner = new Scanner(conversationHistoryFile);
		
		// process lines of file
		List<String> fileLines = new ArrayList<>();
		
		while (lineScanner.hasNextLine()) {
			// get all lines in the file
			String line = lineScanner.nextLine();
			fileLines.add(line);
		}
		
		List<Conversation> allConversations = new ArrayList<>();
		List<String> membersList = new ArrayList<>();
		int ID = 0;
		//go through each of the lines
		for (int i = 0; i < fileLines.size(); i++) {
			String line = fileLines.get(i);
			//extract all conversations from file
			if (line.startsWith("Conversation ")) {
				String conversationLine = line;
				//take conversation and store in array
				String conversationID = conversationLine.split(" ")[1];
				//take each conversation and its ID
				ID = Integer.parseInt(conversationID);
				 membersList = new ArrayList<>();
			}
			else if (line.startsWith("Members")) {
				String membersLine = line;
				//take all members and store in array
				String[] members = membersLine.split(": ")[1].trim().split(",");
				//sort the array in alphabetical order
				Arrays.sort(members);
				//turn array into list (for comparison of membersOfMessage sent by client)
				membersList = Arrays.asList(members);
			}
			else if(line.startsWith("Chat:")){
				i++;
				List<String> messages = new ArrayList<>();
				while (i < fileLines.size() && !fileLines.get(i).startsWith("Conversation")) {
					line = fileLines.get(i).trim();
					if(!line.equals("")) {
					messages.add(line);
					}
					i++;
				}
				i--;
				Conversation conversation = new Conversation(ID, new ArrayList<>(membersList), messages);
				allConversations.add(conversation);
			}
		}
		
		System.out.println(allConversations.size());
		
		out.writeObject(allConversations);
		
		}
		
		
		
		
		}
	}

