package com.bomberman.server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
// Server class for instantiating the controller and waiting for messages
// received messages get queued onto the messageQueue
public class Server extends Thread {
	private static final int port = 5000;
	private DatagramSocket serverSocket;
	private ServerSender sender;
	private MessageQueue messages;
	private Controller controller;
	private byte[] recData;

	public Server() throws Exception {
		this(new String[0]);
	}

	public Server(String[] args) throws Exception {
		// Instantiate objects
		serverSocket = new DatagramSocket(port);   // Socket to send and receive messages on
		sender = new ServerSender(serverSocket);     // New sender socket to send messages with
		messages = new MessageQueue();               // Queue to stack messages received on.
		controller = new Controller(sender, messages); // Controller to handle messages
		recData = new byte[2048];

		// Check for command line arguments and interpret them
		if(args.length > 0){
			if(args.length != 2){
				System.out.println("Invalid use of command line arguments");
				System.out.println("Enter game flag, -g, followed by file to load");
				System.out.println("Use: \"$ Java Server.java -g gameBoard.json\"");
			}
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-g")){
					System.out.println("Game board flag");
					if(!args[++i].endsWith(".json")){
						System.out.println("Trying to load: " + args[i]);
						System.out.println("Invalid file format loaded");
					}
					else{
						File f = new File(args[i]);
						if(f.exists() && !f.isDirectory()){
							BufferedReader br = new BufferedReader(new FileReader(args[i]));
							String currLine;
							String gameBoard = "";
							while((currLine = br.readLine()) != null){
								gameBoard += currLine;
							}
							messages.add((new DatagramPacket(gameBoard.getBytes(), gameBoard.getBytes().length)));
							System.out.println("Loaded game board located at: " + args[i]);
							br.close();
						}
						else{
							System.out.println("File does not exist!");
						}
					}
				}
			}
		}
		controller.start();
	}

	@Override
	public void run() {
		System.out.println("Server started listening on port: " + getPort());
		// Listen for a message and add it to the queue if there is one
		while(true) {
			try {
				DatagramPacket dp = receive(); // blocking wait
				System.out.println("Server received a packet!");
				addMessage(dp); // add message to message queue
			} catch (IOException e) {
				// socket closed most likely

				return;
			}
		}
	}

	public MessageQueue getMessageQueue() {
		return messages;
	}

	public boolean isRunning() {
		return !serverSocket.isClosed();
	}

	public InetAddress getAddress() {
		return serverSocket.getInetAddress();
	}

	public int getPort() {
		return port;
	}

	public void addMessage(DatagramPacket dp) {
		messages.add(dp);
	}

	public void kill() {
		serverSocket.close();
	}

	public DatagramPacket receive() throws IOException {
		DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
		serverSocket.receive(recPacket);
		return recPacket;
	}
}
