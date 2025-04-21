import java.net.Socket;

public class ClientHandler implements Runnable {
	
	private Socket clientSocket = null;
	
	public ClientHandler(Socket client) {
		this.clientSocket = client;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
