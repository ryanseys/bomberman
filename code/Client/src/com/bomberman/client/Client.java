package com.bomberman.client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Client {
	private MessageQueue receivedMsgs;
	private ClientReceiver cr;
	private DatagramSocket dsocket;
	private int playerid = 0;
	private int powerups, bombs;
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

	/**
	 * Start a new game on the server.
	 * Must have connected first and have a player id.
	 */
	public void startGame() {
		isGameOn = true;
		JSONObject startMsg = new JSONObject();
		startMsg.put("command", "button");
		startMsg.put("button", "start");
		startMsg.put("pid", playerid);
		send(startMsg.toString());
	}

	/**
	 * Create a new game.
	 */
	public void newGame() {
		gameOver = false;
		startGame();
	}

	/**
	 * Flush all the messages from the received messages queue.
	 */
	public void flushMessages() {
		receivedMsgs.clear();
	}

	/**
	 * End the game by sending a message to the server
	 * telling it that we want to end the game.
	 */
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
	 * Receive a message from the server (including broadcasts)
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
	 * Receive messages that are not broadcast messages.
	 *
	 * @return The first message received that isnt a broadcast
	 */
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

	/**
	 * Quit the client receiving of messages by
	 * terminating the client receiver.
	 *
	 * @throws InterruptedException
	 */
	public void quit() throws InterruptedException {
		cr.requestQuit();
		cr.join();
	}

	/**
	 * Request to the server that it reset its state
	 * To be typically used for debugging and testing.
	 */
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
				powerups = resp.getJSONObject("players").getJSONObject("1").getInt("powerups");
				bombs = resp.getJSONObject("players").getJSONObject("1").getInt("bombs");
		}
	}

	/**
	 * Get the board cell letter. 1 is P1,
	 * 2 = P2, B is bomb, F is fire for example
	 * @param type the integer value of the game object type
	 * @return the String representation of that game object type
	 */
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

	/**
	 * Stringify the board so that we can display this to the view.
	 * @param board board to stringify
	 * @return string representation of the board.
	 */
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

	/**
	 * Move the player client in a specific direction.
	 * @param d Action type of the direction you want to go. Options are UP, DOWN, LEFT, RIGHT
	 */
	public void move(Action d) {
		JSONObject moveMsg = new JSONObject();
		moveMsg.put("command", "move");
		moveMsg.put("direction", actions[d.ordinal()]);
		moveMsg.put("pid", playerid);
		send(moveMsg.toString());
	}

	/**
	 * Deploy (drop) a bomb where the client player
	 * is currently standing on the game board.
	 */
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

	/**
	 * Returns the id of the client.
	 * -1 is spectator,
	 * positive number is player
	 * 0 is not connected.
	 * @return int representation of the client id
	 */
	public int getPlayerID() {
		return playerid;
	}

	/**
	 * Returns whether a game is currently in progress (not game over, not lobby)
	 * @return true if game on, false otherwise
	 */
	public boolean isGameOn() {
		return isGameOn;
	}

	/**
	 * Returns whether the game of the client is over.
	 * @return true if game over, false otherwise
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Get the number of powerups that the player has.
	 * @return int of the number of powerups
	 */
	public int getPowerups() {
		return powerups;
	}

	/**
	 * Return the number of bombs that the client (player) has.
	 * @return int of the number of bombs
	 */
	public int getBombs() {
		return bombs;
	}

	/**
	 * Returns whether the client is a player.
	 * @return true if player, false otherwise.
	 */
	public boolean isPlayer() {
		return this.playerid > 0;
	}

	/**
	 * Returns whether the client is a spectator.
	 * @return true if spectator, false otherwise
	 */
	public boolean isSpectator() {
		return this.playerid < 0;
	}

	/**
	 * Returns whether the client is connected
	 * as either a spectator or a player.
	 * @return true if connected, false otherwise.
	 */
	public boolean isConnected() {
		return this.playerid != 0;
	}

	/**
	 * Load a game from a string representation of a board.
	 * @param board String representation of board
	 */
	public void loadGame(String board) {
		JSONObject game = new JSONObject();
		game.put("game", new JSONObject(board));
		game.put("command", "load");
		send(game.toString());
	}

	/**
	 * Get the string representation of the board to save
	 * to a file so that we can load it back in later.
	 * @return String representation of board
	 */
	public String getBoardToSave(){
		JSONObject board = new JSONObject();
		board.put("game",this.game);
		board.put("command", "load");
		return board.toString();
	}
}
