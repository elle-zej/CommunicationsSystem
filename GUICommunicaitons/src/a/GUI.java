package a;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI {
	
	public static void main(String[] args) {
		loginWindow();
		//loginWindow.setVisible(true);
		//defaultWindow();
	}

	private static void loginWindow() {
	    // Create the frame
		//main frame to hold the panel
	    JFrame frame = new JFrame("Login");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(300, 400);
	    frame.setLocationRelativeTo(null); // Center on screen

	    // Main panel to hold the sub panels
	    //grid layout, with 3 rows 1 column
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new GridLayout(3, 1, 0, 1));
	    frame.add(mainPanel);

	    // User ID Panel
	    //Panel for user id 
	    JPanel userIdPanel = new JPanel();
	    userIdPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
	    //label for the id
	    JLabel idLabel = new JLabel("Employee ID: ");
	    //set size for idlabel
	    idLabel.setPreferredSize(new Dimension(100, 30));
	    //the text field to enter user ID
	    JTextField idField = new JTextField(13);
	    idField.setPreferredSize(new Dimension(200, 30));
	    userIdPanel.add(idLabel);
	    userIdPanel.add(idField);
	    mainPanel.add(userIdPanel);
	    
	    // User Password Panel
	    JPanel userPassPanel = new JPanel();
	    userPassPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
	    JLabel passLabel = new JLabel("Password: ");
	    passLabel.setPreferredSize(new Dimension(100, 30));
	    JPasswordField passField = new JPasswordField(13);
	    passField.setPreferredSize(new Dimension(200, 30));
	    userPassPanel.add(passLabel);
	    userPassPanel.add(passField);
	    mainPanel.add(userPassPanel);
	    
	    //Login panel
	    	//for the login button
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    buttonPanel.setMaximumSize(new Dimension(30, 50));
	    JButton loginButton = new JButton("Login");
	    loginButton.setPreferredSize(new Dimension(100, 30));
	    buttonPanel.add(loginButton);
	    mainPanel.add(buttonPanel);
	    
	    loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//when logged in
				//disposes login window
				//directs to the homepage
				frame.dispose();
				homePage();
				
			}
		});
	    
	    frame.setVisible(true); // Make visible
	    
	    //return frame;
	}
	
	private static void homePage() {
		JFrame mainFrame = defaultWindow();
	}
	//default frame that will be reused 
	private static JFrame defaultWindow(){
		//create the frame
	    JFrame frame = new JFrame("SEND");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //we have a frame now
	    frame.setSize(700, 600);      
	    frame.setLocationRelativeTo(null);  // Center on screen
	    //u could create a frame that is not visible
	    frame.setVisible(true);	// make visible
	    return frame;
	   }
}
