import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

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
