package com.mycompany.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David Kolesar
 *
 *         Hillary Server is a private, multithreaded chat server. It employs
 *         the following steps to operate: 
 *         1. Client connects to server. 
 *         2. User submits a String to the server (potential username). 
 *         3. The server checks the username against all usernames already registered. 
 *         4. If the username isn't unique, the user is challenged for a new one. 
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
			LOGGER.warning("failed to instantate new ServerSocket on port " + PORT +" Error : " + e.getMessage());
		}

		// Trying to instantiate new UserInteractionService
		try {
			while (true) {
				new UserInteractionService(listener.accept()).start();
			}
		} catch (IOException e) {
			LOGGER.warning("failed to spawn new Handler. Error : " + e.getMessage());
		}

		// Finally block closes listener upon disconnect
		finally {
			try {
				listener.close();
			} catch (IOException e) {
				LOGGER.warning("failed to close listener. Error : " + e.getMessage());
			}
		}
	}

	/**
	 * UserInteractionService deals with each unique user's connection, name, and
	 * messages
	 *
	 */
	private static class UserInteractionService extends Thread {
		private String userName;
		private BufferedReader reader;
		private PrintWriter writer;
		private Socket socket;

		/**
		 * Boilerplate to handle threading by assigning socket established by listener
		 * via PORT
		 */
		public UserInteractionService(Socket socket) {
			this.socket = socket;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run() Checks for unique username
		 */
		public void run() {
			boolean userNameChallengeSatisfied = false;
			try {
				LOGGER.info("New client is attempting to connect");
				// Creates BufferedReader to display text through the socket to users.
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);

				/*
				 * Requests username from user and challenges them if it already exists (or is
				 * null / empty)
				 */
				if (userNameChallengeSatisfied == false) {
					LOGGER.log(Level.FINE, "Sending unique name request to client");
					writer.println(SUBMIT_NAME_REQUEST);
					userName = reader.readLine();
					if (userName == null || userName.isEmpty()) {
						LOGGER.log(Level.FINE, "Client returned a username that is empty or null.");
					} else {
						
						/*
						 * Locks HashSet of names to enforce that only one thread at a time should be
						 * able to access the method in order to avoid racing condition (duplicate
						 * usernames)
						 */
						
						synchronized (userNames) {
							if (!userNames.contains(userName)) {
								LOGGER.log(Level.FINE, "Client returned unique username");
								userNames.add(userName);
								userNameChallengeSatisfied = true;
							}
						}
					}
				}

				// Add user's unique printWriter to the HashSet
				writer.println(RECEIVE_NAME_CONFIRMATION);
				printWriters.add(writer);

				// Display messages from all users
				while (true) {
					String input = reader.readLine();
					if (input == null || input.isEmpty()) {
						// User cannot send a null or empty message
						return;
					}
					for (PrintWriter writer : printWriters) {
						writer.println(SUBMIT_MESSAGE_PREFIX + " " + userName + ":" + input);
					}
				}
			} catch (IOException e) {
				LOGGER.warning(e.getMessage());
			}

			/*
			 * If a user exits the application, remove their username and printWriter from
			 * their corresponding HashSets and close the user's old socket.
			 */
			finally {
				LOGGER.log(Level.FINE, "Client disconnected from Hillary");
				if (userName != null) {
					userNames.remove(userName);
				}
				if (writer != null) {
					printWriters.remove(writer);
				}
				try {
					socket.close();
				} catch (IOException e) {
					LOGGER.warning(e.getMessage());
				}
			}
		}
	}
}
