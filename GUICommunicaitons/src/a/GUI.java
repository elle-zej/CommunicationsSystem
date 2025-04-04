package a;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI {
	public static void main(String[] args) {
		loginWindow();
		//defaultWindow();
	}
	
	private static void loginWindow() {
	    // Create the frame
	    JFrame frame = new JFrame("Login");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(300, 400);
	    frame.setLocationRelativeTo(null); // Center on screen

	    // Main panel
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new GridLayout(3, 1, 0, 5));
	    frame.add(mainPanel);

	    // User ID Panel
	    JPanel userIdPanel = new JPanel();
	    userIdPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    JLabel idLabel = new JLabel("Employee ID: ");
	    idLabel.setPreferredSize(new Dimension(100, 30));
	    JTextField idField = new JTextField(10);
	    idField.setPreferredSize(new Dimension(200, 30));
	    userIdPanel.add(idLabel);
	    userIdPanel.add(idField);
	    mainPanel.add(userIdPanel);
	    
	    // User Password Panel
	    JPanel userPassPanel = new JPanel();
	    userPassPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    JLabel passLabel = new JLabel("Password: ");
	    passLabel.setPreferredSize(new Dimension(100, 30));
	    JTextField passField = new JTextField(10);
	    passField.setPreferredSize(new Dimension(200, 30));
	    userPassPanel.add(passLabel);
	    userPassPanel.add(passField);
	    mainPanel.add(userPassPanel);
	    
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    buttonPanel.setMaximumSize(new Dimension(300, 50));
	    JButton loginButton = new JButton("Login");
	    loginButton.setPreferredSize(new Dimension(150, 40));
	    buttonPanel.add(loginButton);
	    mainPanel.add(loginButton);
	    // for some reason somtimes the frame does not render so to solve it
	    //frame.validate(); // Recalculates layout
	    //frame.repaint();  // Repaints the frame
	    frame.setVisible(true); // Make visible
	}
	//default frame that will be reused 
	private static JFrame defaultWindow(){
		//create the frame
	    JFrame frame = new JFrame("SEND");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //we have a frame now
	    frame.setSize(700, 700);      
	    frame.setLocationRelativeTo(null);  // Center on screen
	    //u could create a frame that is not visible
	    frame.setVisible(true);	// make visible
	    return frame;
	   }
}
