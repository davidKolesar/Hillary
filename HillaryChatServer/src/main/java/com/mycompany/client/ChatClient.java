package com.mycompany.client;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author David Kolesar -- 29NOV2018
 *
 *         Gateway to Hillary server 1. Server sends 'SUBMIT_USERNAME' request
 *         2. User responds with usernamename 3. If username is not unique,
 *         server responds with 'SUBMIT_USERNAME' prefix 4. If username is
 *         unique, server responds with 'USERNAME_ACCEPTED.' 5. If username is
 *         accepted, client is able to send messages to other users connected to
 *         the server
 * 
 */
public class ChatClient {
	//chat protocol definitions
	private static final String SUBMIT_NAME_REQUEST = "SUBMIT_USERNAME";
	private static final String RECEIVE_NAME_CONFIRMATION = "USERNAME_UNIQUE";
	private static final String SUBMIT_MESSAGE_PREFIX = "MESSAGE";

	private String userName = "New User";
	private BufferedReader reader;
	private PrintWriter writer;
	private JFrame jFrame = new JFrame(userName);
	private JTextField jTextField = new JTextField(60);
	private JTextArea messageArea = new JTextArea(10, 60);
	
	/**
	 * Constructor builds JFrame such that textfield is not editable until
	 * client receives the RECEIVE_NAME_CONFIRMATION from the server.
	 */
	public ChatClient() {
		
	}
}
