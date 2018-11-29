package com.mycompany.server;

import java.io.PrintWriter;
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
	//Each user is assigned a unique printWriter
	private static HashSet<PrintWriter> printWriters = new HashSet<PrintWriter>();

}
