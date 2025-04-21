import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(1);
		Socket socket = ss.accept();
		
	}
}
