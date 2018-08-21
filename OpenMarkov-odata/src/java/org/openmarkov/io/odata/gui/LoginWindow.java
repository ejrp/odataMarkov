package org.openmarkov.io.odata.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.openmarkov.io.odata.gui.editor.SchemaEditor;



public class LoginWindow extends JDialog implements ActionListener {

	private static final long serialVersionUID = 6260315508881992801L;
	JButton SUBMIT;
	JPanel panel;
	JLabel label1, label2,label3;
	JTextField userText, passText,uri;
	JCheckBox authenticationEnabled;
	String serviceUrl = "http://services.odata.org/V4/OData/OData.svc/";
	String username = "";
	String password = "";
	private boolean succeeded = false;
	private SchemaEditor elementExplorer = null;
	private WindowListener windowListener = null;
	private final JFrame frameParent;

	public WindowListener getWindowListener() {
		return windowListener;
	}

	public void setWindowListener(WindowListener aWindowListener) {
		this.windowListener = aWindowListener;
	}
	
	public LoginWindow () {
		this(null);
	}

	public LoginWindow(JFrame aParent) {
		super (aParent);
		frameParent = aParent;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		label3 = new JLabel();
		label3.setText("Uri:");
		uri = new JTextField(30);
		uri.setText(serviceUrl);
		
		label1 = new JLabel();
		label1.setText("Username:");
		userText = new JTextField(15);
		userText.setText(username);

		label2 = new JLabel();
		label2.setText("Password:");
		passText = new JPasswordField(15);
		passText.setText(password);
		// this.setLayout(new BorderLayout());
		authenticationEnabled = new JCheckBox("Basic authentication" ,true);
		authenticationEnabled.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent event) {
		        JCheckBox cb = (JCheckBox) event.getSource();
		        if (cb.isSelected()) {
		            userText.setEnabled(true);
		            passText.setEnabled(true);
		        } else {
		            userText.setEnabled(false);
		            passText.setEnabled(false);
		        }
		    }
		});
		
		

		SUBMIT = new JButton("SUBMIT");

		panel = new JPanel(new GridLayout(5, 1));
		panel.add(label3);
		panel.add(uri);		
		panel.add(authenticationEnabled);
		panel.add(new JLabel());
		panel.add(label1);
		panel.add(userText);
		panel.add(label2);
		panel.add(passText);
		panel.add(SUBMIT);
		add(panel, BorderLayout.CENTER);
		SUBMIT.addActionListener(this);
		setTitle("Odata Service Authentication");
	}

	public void actionPerformed(ActionEvent ae) {
		String user = userText.getText();
		String pass = passText.getText();
		String url = uri.getText();
		try {
			OdataClientHandler odataHandler = OdataClientHandler.getClientHandler(authenticationEnabled.isSelected(),url, user, pass);
			setVisible(false);
			elementExplorer = GraphHandler.displayXchema(odataHandler,getWindowListener(),getFrameParent());
			succeeded = true;
			dispose();

		} 
		catch (Exception e) {
			System.out.printf("%s :service connection failed",e.getMessage());
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			succeeded = false;
			dispose();
		}
	}
    public JFrame getFrameParent() {
		return frameParent;
	}

	public SchemaEditor getElementExplorer() {
		return elementExplorer;
	}

	public boolean isSucceeded() {
        return succeeded;
    }
}


