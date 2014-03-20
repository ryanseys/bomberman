package com.bomberman.client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JLabel;

import org.json.JSONArray;
import org.json.JSONObject;

public class Client {
	private MessageQueue receivedMsgs;
	private ClientReceiver cr;
	private DatagramSocket dsocket;
	private int playerid = 0;
	private int powerups;
	private int bombs;
	private int lives = 1;
	private boolean isGameOn = false;
	private boolean gameOver = false;
	private boolean isDebug = false;
	private JSONObject game = null; // State of the game
	private InetAddress server_ip;
	private int server_port;

	public Client(String IPAddress, int port) throws SocketException, UnknownHostException, InterruptedException {
		this.receivedMsgs = new MessageQueue();
		dsocket = new DatagramSocket();
		cr = new ClientReceiver(receivedMsgs, dsocket);
		this.server_ip = InetAddress.getByName(IPAddress);
		this.server_port = port;
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

	public void flushMessages() {
		receivedMsgs.clear();
	}

	public void endGame() {
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
	public void send(String s) {
		DatagramPacket data = new DatagramPacket(s.getBytes(), s.length(), server_ip, server_port);
		try {
			dsocket.send(data);
		} catch (IOException e) {
			e.printStackTrace();
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

	public String receiveNoBroadcasts() {
		boolean isBroadcast = true;
		String msg = "";
		JSONObject resp;
		while(isBroadcast) {
			msg = receive();
			resp = new JSONObject(msg);
			if(!resp.get("type").equals("broadcast")) {
				isBroadcast = false;
			}
		}
		return msg;
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
		cr.join();
	}

	public void resetServer() {
		JSONObject resetMsg = new JSONObject();
		resetMsg.put("command", "reset");
		send(resetMsg.toString());
		receive(); // wait until it replies
	}

	/**
	 * Set the state of the game
	 * @param s String of the game state
	 */
	public void setState(String s) {
		JSONObject resp = new JSONObject(s);

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
			this.game = resp.getJSONObject("game");
		}
		if(resp.keySet().contains("players")) {
				this.powerups = resp.getJSONObject("players").getJSONObject("" + playerid).getInt("powerups");
				this.bombs = resp.getJSONObject("players").getJSONObject("" + playerid).getInt("bombs");
				this.lives = resp.getJSONObject("players").getJSONObject("" + playerid).getInt("lives");
		}
	}

	public String getGameBoardTypeLetter(int type) {
		switch(type) {
		case 6:
			return "B";
		case 10:
			return "F";
		default:
			return (new Integer(type)).toString();
		}
	}

	public String stringifyBoard(JSONArray board) {
		String result = "";
		for(int col = 0; col < board.length(); col++) {
			for(int row = 0; row < board.length(); row++) {
				result += "[" + getGameBoardTypeLetter(board.getJSONArray(row).getInt(col)) + "]";
			}
			result += "\n";
		}
		return result;
	}

	String[] actions = {"up", "down", "right", "left", "deploy"};

	public void move(Action d) {
		JSONObject moveMsg = new JSONObject();
		moveMsg.put("command", "move");
		moveMsg.put("direction", actions[d.ordinal()]);
		moveMsg.put("pid", playerid);
		send(moveMsg.toString());
	}

	public void deployBomb() {
		System.out.println("Player " + playerid +" deploying bomb!");
		JSONObject bombMsg = new JSONObject();
		bombMsg.put("command", "button");
		bombMsg.put("button", actions[Action.BOMB.ordinal()]);
		bombMsg.put("pid", playerid);
		send(bombMsg.toString());
	}

	/**
	 * Get the state of the game
	 * @return String representation of the game state
	 */
	public String getGameBoard() {
		String board = (this.game == null) ? "" : stringifyBoard(this.game.getJSONArray("board"));
		return board;
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

	public int getPowerups() {
		return powerups;
	}

	public int getBombs() {
		return bombs;
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
		JSONObject game = new JSONObject();
		game.put("game", new JSONObject(board));
		game.put("command", "load");
		send(game.toString());
	}

	public String getBoardToSave(){
		JSONObject board = new JSONObject();
		board.put("game",this.game);
		board.put("command", "load");
		return board.toString();
	}
	/**
	 * @return the lives
	 */
	public int getLives() {
		return this.lives;
	}
}
