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
