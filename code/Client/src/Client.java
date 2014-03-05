import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Client {
	private MessageQueue toSendMsgs;
	private MessageQueue receivedMsgs;
	private ClientReceiver cr;
	private ClientSender cs;
	private DatagramSocket dsocket;
	private String board; // state of the game
	private int playerid = 0;
	private boolean isGameOn = false;
	private boolean gameOver = false;
	private boolean isDebug = false;

	public Client(String IPAddress, int port) throws SocketException, UnknownHostException, InterruptedException {
		this.toSendMsgs = new MessageQueue();
		this.receivedMsgs = new MessageQueue();
		this.board = "";
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
		JSONObject startMsg = new JSONObject();
		startMsg.put("command", "button");
		startMsg.put("button", "start");
		startMsg.put("pid", playerid);
		send(startMsg.toString());
	}

	public void newGame() {
		gameOver = false;
		startGame();
	}
	public void endGame(){
		if(isGameOn){
			JSONObject endMsg = new JSONObject();
			endMsg.put("pid", this.playerid);
			endMsg.put("command", "button");
			endMsg.put("button", "end");
			send(endMsg.toString());
		}
	}

	/**
	 * Send a message to the server
	 * @param msg String message to send
	 */
	public void send(String msg) {
		if(isDebug) System.out.println("Sending message: " + msg);
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
					return "";
				}
			}
			s = receivedMsgs.pop();
		}
		if(isDebug) System.out.println("Received message: " + s);
		return s;
	}

	/**
	 * Connect to the server as a player. Get a player id.
	 */
	public void connect(String type) {
		JSONObject connMsg = new JSONObject();
		connMsg.put("command", "join");
		connMsg.put("type", type);
		send(connMsg.toString());
	}

	public void quit() throws InterruptedException {
		cr.requestQuit();
		cs.requestQuit();
		cr.join();
		cs.join();
	}

	/**
	 * Set the state of the game
	 * @param s String of the game state
	 */
	public void setState(String s) {
		JSONObject resp = new JSONObject(s);

		JSONObject game = null;
		if(resp.getString("type").equals("game_over")) {
			isGameOn = false;
			gameOver = true;
		}
		else if(resp.getString("type").equals("player_join") && resp.getString("resp").equals("Success")) {
			playerid = resp.getInt("pid");
		}
		else if(resp.getString("type").equals("spectator_join") && resp.getString("resp").equals("Success")) {
			playerid = -1;
		}
		if(resp.keySet().contains("game")) {
			isGameOn = true;
			game = resp.getJSONObject("game");
			this.board = stringifyBoard(game.getJSONArray("board"));
		}
	}

	public String stringifyBoard(JSONArray board) {
		String result = "";
		for(int col = 0; col < board.length(); col++) {
			for(int row = 0; row < board.length(); row++) {
				result += "[" + board.getJSONArray(row).getInt(col) + "]";
			}
			result += "\n";
		}
		return result;
	}

	String[] directions = {"up", "down", "right", "left"};

	public void move(Direction d) {
		JSONObject moveMsg = new JSONObject();
		moveMsg.put("command", "move");
		moveMsg.put("direction", directions[d.ordinal()]);
		moveMsg.put("pid", playerid);
		send(moveMsg.toString());
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

	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * @return the isPlayer
	 */
	public boolean isPlayer() {
		return this.playerid > 0;
	}
	public boolean isSpectator() {
		return this.playerid < 0;
	}
	public boolean isConnected(){
		return this.playerid != 0;
	}

	public void loadGame(String board) {
		send(board);
	}

	public void saveGame() {
		// TODO Auto-generated method stub
		
	}
}