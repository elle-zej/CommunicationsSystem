import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Server {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ServerSocket server = null;
		try {
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
	private static String loginInfoFile = "loginInfo.txt";
	
	// Constructor
	public ClientHandler(Socket socket)
	{
		this.clientSocket = socket;
	}

	public void run(){
		
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
			
			//try to login
			
			}
		catch (IOException e) {
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
	
	//returns true if the user name password is valid
	//returns false if 
    public static boolean authenticateUser(User user) {
    	
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
    			
    			//first scan the id
    			String id = wordScanner.next();
    			//scan the password
    			String pass = wordScanner.next();
    			
    			//if id and password combo exist, return true
    			if(user.getUsername().equals(id) && user.getPassword().equals(pass)) {
    				lineScanner.close();
    				wordScanner.close();
    				return true;
    			}
    			
    		}
    		lineScanner.close();
    	}
    	catch (Exception e) {
			System.out.println("File not found!");
		}
    	//returns false if no such username password combination found
		return false;
    	
    }

}
}
