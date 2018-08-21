package org.openmarkov.io.odata.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/* PasswordDemo.java requires no other files. */
public class LoginFrame extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8375112555869484256L;

	private static String OK = "ok";

	private JFrame controllingFrame; // needed for dialogs
	private JPasswordField passwordField;
	private JTextField usernameField;
	private JTextField urlField;

	public LoginFrame(JFrame f) {
		// Use the default FlowLayout.
		controllingFrame = f;

		// Create everything.
		passwordField = new JPasswordField(30);
		passwordField.setActionCommand(OK);
		passwordField.addActionListener(this);
		
		usernameField = new JTextField(20);
		usernameField.setActionCommand(OK);
		usernameField.addActionListener(this);

		urlField = new JTextField(20);
		urlField.setActionCommand(OK);
		urlField.addActionListener(this);
		
		JLabel labelpass = new JLabel("Password:");
		JLabel labeluser = new JLabel("Username:");
		JLabel labelurl = new JLabel("Url:");
		labeluser.setLabelFor(usernameField);
		labelurl.setLabelFor(urlField);
		labelpass.setLabelFor(passwordField);

		JComponent buttonPane = createButtonPanel();

		// Lay out everything.
		JPanel textPane = new JPanel();
		textPane.setLayout(new BoxLayout(textPane,BoxLayout.LINE_AXIS));
		textPane.add(labelurl);
		textPane.add(urlField);
		textPane.add(labeluser);
		textPane.add(usernameField);
		textPane.add(labelpass);
		textPane.add(passwordField);
		add(textPane);
		add(buttonPane);
	}

	protected JComponent createButtonPanel() {
		JPanel p = new JPanel(new GridLayout(0, 1));
		JButton okButton = new JButton("OK");

		okButton.setActionCommand(OK);

		okButton.addActionListener(this);

		p.add(okButton);

		return p;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (OK.equals(cmd)) { // Process the password.
			char[] inputp=   passwordField.getPassword();
			String inputus=  usernameField.getText();
			String inputurl= urlField.getText();
			
//			try {
//				OdataClient client = new OdataClient(inputurl,inputus,inputp);
//			}
			
//			if (isPasswordCorrect(input)) {
//				JOptionPane.showMessageDialog(controllingFrame, "Success! You typed the right password.");
//			} else {
//				JOptionPane.showMessageDialog(controllingFrame, "Invalid password. Try again.", "Error Message",
//						JOptionPane.ERROR_MESSAGE);
//			}

			// Zero out the possible password, for security.
//			Arrays.fill(input, '0');

			passwordField.selectAll();
			resetFocus();
		}
	}

/*	*//**
	 * Checks the passed-in array against the correct password. After this method
	 * returns, you should invoke eraseArray on the passed-in array.
	 *//*
	private static boolean isPasswordCorrect(char[] input) {
		boolean isCorrect = true;
		char[] correctPassword = { 'b', 'u', 'g', 'a', 'b', 'o', 'o' };

		if (input.length != correctPassword.length) {
			isCorrect = false;
		} else {
			isCorrect = Arrays.equals(input, correctPassword);
		}

		// Zero out the password.
		Arrays.fill(correctPassword, '0');

		return isCorrect;
	}*/

	// Must be called from the event dispatch thread.
	protected void resetFocus() {
		passwordField.requestFocusInWindow();
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Login to URL");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		final LoginFrame newContentPane = new LoginFrame(frame);
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Make sure the focus goes to the right component
		// whenever the frame is initially given the focus.
		frame.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				newContentPane.resetFocus();
			}
		});

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        createAndShowGUI();
            }
        });
    }
}
