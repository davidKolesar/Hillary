package com.mycompany.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * @author David Kolesar
 *
 *         Hillary Server is a private, multithreaded chat server. It employs
 *         the following steps to operate: 1. Client connects to server. 2. User
 *         submits a String to the server (potential username). 3. The server
 *         checks the username against all usernames already registered. 4. If
 *         the username isn't unique, the user is challenged for a new one. 4.
 *         Else, a new handler thread is created. 5. User is assigned a unique
 *         printWriter.
 *
 */
public class ChatServer {
	// handles logging
	private static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());
	private static final int PORT = 9001;
	private static final String SUBMIT_NAME_REQUEST = "SUBMIT_USERNAME";
	private static final String RECEIVE_NAME_CONFIRMATION = "USERNAME_UNIQUE";
	private static final String SUBMIT_MESSAGE_PREFIX = "MESSAGE";
	private static HashSet<String> userNames = new HashSet<String>();
	// Each user is assigned a unique printWriter
	private static HashSet<PrintWriter> printWriters = new HashSet<PrintWriter>();

	/**
	 * Upon appplication launch, this thread listens for new connections, spawns
	 * handlers, and triggers new threads upon successful login
	 */
	public static void main(String[] args) {
		LOGGER.info("Launching application");
		System.out.println("Launching Hillary Server");
		ServerSocket listener = null;
		// Trying to instantiate new listener
		try {
			listener = new ServerSocket(PORT);
		} catch (IOException e) {
			LOGGER.warning("failed to instantate new ServerSocket on port " + PORT);
			e.printStackTrace();
		}
		// Trying to instantiate new Handler
		try {
			while (true) {
				new UserInteractionService(listener.accept()).start();
			}
		} catch (IOException e) {
			LOGGER.warning("failed to spawn new Handler!");
			e.printStackTrace();
		}
		// Finally block closes listener upon disconnect
		finally {
			try {
				listener.close();
			} catch (IOException e) {
				LOGGER.warning("failed to close listener.");
				e.printStackTrace();
			}
		}
	}

	private static class UserInteractionService extends Thread {
		private String userName;
		private BufferedReader reader;
		private PrintWriter writer;
		private Socket socket;

		public UserInteractionService(Socket socket) {
			this.socket = socket;
		}
	}
}
