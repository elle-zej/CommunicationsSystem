import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
	
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(1234);
		while(true) {
			Socket client = ss.accept();
			//create an instance of the client handler
			ClientHandler handler = new ClientHandler(client);
			
			//new thread for the client starts so the server  
			// handle other incoming requests
			new Thread(handler).start();
			
		}
		
	}
}
