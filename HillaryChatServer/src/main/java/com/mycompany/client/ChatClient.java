package com.mycompany.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mycompany.client.view.ClientView;

/**
 * @author David Kolesar -- 29NOV2018
 *
 *         Gateway to Hillary server 
 *         1. Server sends 'SUBMIT_USERNAME' request
 *         2. User responds with usernamename 
 *         3. If username is not unique, server responds with 'SUBMIT_USERNAME' prefix 
 *         4. If username is unique, server responds with 'USERNAME_ACCEPTED.' 
 *         5. If username is accepted, client is able to send messages to other users connected to
 *         the server
 * 
 */
public class ChatClient {
	private static final Logger LOGGER = Logger.getLogger(ChatClient.class.getName());
	// chat protocol definitions
	private static final String SUBMIT_NAME_REQUEST = "SUBMIT_USERNAME";
	private static final String RECEIVE_NAME_CONFIRMATION = "USERNAME_UNIQUE";
	private static final String SUBMIT_MESSAGE_PREFIX = "MESSAGE";
	private static final int PORT = 9001;

	private ClientView view = new ClientView();
	private String userName = "New User";
	private PrintWriter writer;
	private BufferedReader reader;
	public JFrame jFrame = new JFrame(userName);
	public JTextField jTextField = new JTextField(60);
	private JTextArea messageArea = new JTextArea(10, 60);

	/**
	 * Constructor builds JFrame such that textfield is not editable until client
	 * receives the RECEIVE_NAME_CONFIRMATION from the server.
	 */
	public ChatClient() {
		jTextField.setEditable(false);
		messageArea.setEditable(false);
		jFrame.getContentPane().add(jTextField, "North");
		jFrame.getContentPane().add(new JScrollPane(messageArea), "Center");
		jFrame.pack();

		// listens for enter
		jTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// sends message
				writer.println(jTextField.getText());
				// clears out old messages from text field
				jTextField.setText("");
			}
		});
	}

	// Connects to Hillary Chat Server
	private void run() {
		// Make connection and initialize streams
		String serverIP = view.getServerAddress();
		Socket socket = null;

		// try to instantiate new socket
		try {
			socket = new Socket(serverIP, PORT);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Client failed to instantiate new socket.", e);
		}

		// try to read input stream from socket
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,
					"Client failed to read input stream from user socket (Client likely failed to instantiate new socket).",
					e);
		}

		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,
					"Client failed to write output stream from user socket (Client likely failed to instantiate new socket).",
					e);
		}

		// Handles server messages via protocol
		while (true) {
			String line = null;
			try {
				line = reader.readLine();
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Client failed to read message from Server.", e);
				e.printStackTrace();
			}
			if (line.startsWith(SUBMIT_NAME_REQUEST)) {
				userName = view.getName();
				writer.println(userName);
				jFrame.setTitle(userName);
			} else if (line.startsWith(RECEIVE_NAME_CONFIRMATION)) {
				jTextField.setEditable(true);
			} else if (line.startsWith(SUBMIT_MESSAGE_PREFIX)) {
				messageArea.append(line.substring(8) + "\n");
			}
		}
	}

	// Runs the application as JFrame that will close on exit
	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.jFrame.setVisible(true);
		client.run();
	}
}