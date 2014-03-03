import java.net.*;
import org.json.*;

public class Client {
	MessageQueue toSendMsgs;
	MessageQueue receivedMsgs;
	ClientReceiver cr;
	ClientSender cs;
	DatagramSocket dsocket;
	String state; // state of the game
	
	public Client(String IPAddress, int port) throws SocketException, UnknownHostException {
		this.toSendMsgs = new MessageQueue();
		this.receivedMsgs = new MessageQueue();
		this.state = "<< initial state >>";
		dsocket = new DatagramSocket();
		
		// sender and receiver must use the same socket!
		cs = new ClientSender(toSendMsgs, InetAddress.getByName(IPAddress), port, dsocket);
		cr = new ClientReceiver(receivedMsgs, dsocket);
		
		// start the threads for receiving and sending
		cs.start();
		cr.start();
	}
	
	/**
	 * Send a message to the server
	 * @param msg String message to send
	 */
	public void send(String msg) {
		System.out.println("Sending message: " + msg);
		synchronized(toSendMsgs) {
			toSendMsgs.add(msg);
			toSendMsgs.notify();
		}
	}
	
	/**
	 * Receive a message from the server
	 * @return String message received
	 */
	public String receive() {
		String s;
		synchronized(receivedMsgs) {
			while(receivedMsgs.isEmpty()) {
				try {
					receivedMsgs.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			s = receivedMsgs.pop();
		}
		System.out.println("Received message: " + s);
		return s;
	}
	
	public void connect() {
		send("{\"command\":\"join\", \"type\":\"player\"}");
		setState("Connecting...");
	}
	
	/**
	 * Set the state of the game
	 * @param s String of the game state
	 */
	public void setState(String s) {
		this.state = s;
	}
	
	/**
	 * Get the state of the game
	 * @return String representation of the game state
	 */
	public String getState() {
		return this.state;
	}
}