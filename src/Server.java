import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import javax.naming.AuthenticationException;

public class Server {
	
	//filename that stores the login credentials
	private static String loginInfoFile = "loginInfo.txt";
	
	//returns true if the user name password is valid
	//returns false if 
    public static boolean authenticateUser(String userName, String password) {
    	
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
    			if(userName.equals(id) && password.equals(pass)) {
    				return true;
    			}
    			
    		}
    	}
    	catch (Exception e) {
			System.out.println("File not found!");
		}
    	//returns false if no such username password combination found
		return false;
    	
    }
    
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(1234);
		while(true) {
			Socket client = ss.accept();
			//create an instance of the client handler
			ClientHandler clientHandler = new ClientHandler(client);
			
			//new thread for the client starts so the server  
			// handle other incoming requests
			new Thread(clientHandler).start();
		}
	}

 private static class ClientHandler implements Runnable {
	private final Socket clientSocket;

	// Constructor
	public ClientHandler(Socket socket)
	{
		this.clientSocket = socket;
	}

	public void run()
	{
		PrintWriter out = null;
		BufferedReader in = null;
		try {
				
			// get the outputstream of client
			out = new PrintWriter(
				clientSocket.getOutputStream(), true);

			// get the inputstream of client
			in = new BufferedReader(
				new InputStreamReader(
					clientSocket.getInputStream()));

			String line;
			while ((line = in.readLine()) != null) {

				// writing the received message from
				// client
				System.out.printf(
					" Sent from the client: %s\n",
					line);
				out.println(line);
			}
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
}
}
