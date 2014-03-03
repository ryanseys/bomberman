import java.net.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Client {
	MessageQueue toSendMsgs;
	MessageQueue receivedMsgs;
	ClientReceiver cr;
	ClientSender cs;
	DatagramSocket dsocket;
	String board; // state of the game
	private int playerid;
	boolean isGameOn = false;
	
	public Client(String IPAddress, int port) throws SocketException, UnknownHostException {
		this.toSendMsgs = new MessageQueue();
		this.receivedMsgs = new MessageQueue();
		this.board = "<< initial state >>";
		dsocket = new DatagramSocket();
		
		// sender and receiver must use the same socket!
		cs = new ClientSender(toSendMsgs, InetAddress.getByName(IPAddress), port, dsocket);
		cr = new ClientReceiver(receivedMsgs, dsocket);
		
		// start the threads for receiving and sending
		cs.start();
		cr.start();
	}
	
	public void startGame() {
		isGameOn = true;
		send("{ command: \"button\", pid: " + playerid + ", button:\"start\"}");
	}
	
	public void endGame() {
		isGameOn = false;
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
	}
	
	/**
	 * Set the state of the game
	 * @param s String of the game state
	 */
	public void setState(String s) {
		String new_board = "";
		JSONObject resp = new JSONObject(s);
		
		JSONObject game = null;
		if(resp.getString("type").equals("game_over")) {
			endGame();
		}
		else if(resp.getString("type").equals("player_join") && resp.getString("resp").equals("Success")) {
			playerid = resp.getInt("pid");
		}
		
		if(resp.keySet().contains("game")) {
			game = resp.getJSONObject("game");
			
			JSONArray boardArray = game.getJSONArray("board");
			
			for (int i = 0; i < boardArray.length(); i++) {
				for(int j = 0; j < boardArray.length(); j++) {
					new_board += ("[" + boardArray.getJSONArray(j).getInt(i) + "]") ;		
				}
				new_board += "\n";
			}
		}
		this.board = new_board;
	}
	
	String[] directions = {"up", "down", "right", "left"};
	
	public void move(Direction d) {
		send("{ command: \"move\", direction: \"" + directions[d.ordinal()] + "\", pid: " + playerid + "}");
	}
	
	/**
	 * Get the state of the game
	 * @return String representation of the game state
	 */
	public String getGameBoard() {
		return this.board;
	}
	
	public int getPlayerID() {
		return playerid;
	}
	
	public boolean isGameOn() {
		return isGameOn;
	}
	
}