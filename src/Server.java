import java.io.*;
import java.net.Authenticator;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import javax.lang.model.element.NestingKind;
import javax.print.attribute.UnmodifiableSetException;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ServerSocket server = null;
		try {
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

		// Constructor
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {

			ObjectOutputStream out = null;
			ObjectInputStream in = null;
			try {
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());

				Object recievedObject = in.readObject();

				// if the received object is an instance of User
				// we're Trying to Login
				if (recievedObject instanceof User) {
					User user = (User) recievedObject;
					// if authentication is successful
					if (authenticateUser(user)) {
						// create message to indicate login was a success
						Message message = new Message(user, null, null, Status.success);
						// get all the user info
						User returnUser = getUserInfo(user);

						out.writeObject(message);
						out.writeObject(returnUser);
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

		//
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

				}
				lineScanner.close();
			} catch (Exception e) {
				System.out.println("File not found!");
			}
			// returns false if no such user name password combination found
			return false;

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

				}
				lineScanner.close();
			} catch (Exception e) {
				System.out.println("File not found!");
			}
			// returns false if no such user name password combination found
			return user;

		}

	}

}
