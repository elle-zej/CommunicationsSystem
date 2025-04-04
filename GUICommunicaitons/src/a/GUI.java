package a;

import javax.swing.JFrame;

public class GUI {
	public static void main(String[] args) {
		loginWindow();
		//defaultWindow();
	}
	
	//create a login window
	private static void loginWindow() {
		//create the frame
	    JFrame frame = new JFrame("Login");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //we have a frame now
	    frame.setSize(350, 550);      
	    frame.setLocationRelativeTo(null);  // Center on screen
	    //u could create a frame that is not visible
	    frame.setVisible(true);	// make visible
	    
	    //adding the 
	}
	//create a window
	private static void defaultWindow() {
		//create the frame
	    JFrame frame = new JFrame("SEND");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //we have a frame now
	    frame.setSize(700, 700);      
	    frame.setLocationRelativeTo(null);  // Center on screen
	    //u could create a frame that is not visible
	    frame.setVisible(true);	// make visible
	   }
}
