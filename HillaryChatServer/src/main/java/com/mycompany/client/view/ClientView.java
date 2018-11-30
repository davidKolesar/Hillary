package com.mycompany.client.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientView {
	private String userName = "New User";
	public JFrame jFrame = new JFrame(userName);


	// Initialize Splash Screen
	public String getServerAddress() {
		return JOptionPane.showInputDialog(jFrame, "Enter Hillary Server's IP Address :", "Hilary Chat Server",
				JOptionPane.QUESTION_MESSAGE);
	}

	// Challenge for username
	public String getName() {
		return JOptionPane.showInputDialog(jFrame, "Enter a unique user name :", "Username MUST be unique.",
				JOptionPane.PLAIN_MESSAGE);
	}

	public void setUserName(String newUserName) {
		jFrame.setTitle(newUserName);
	}
}