package com.bomberman.server;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

import org.json.JSONObject;

// The controller thread handles all of the messages that the server
// queues up and interprets them and updates the games' model
public class Controller extends Thread{
	private Game game;                   // Current game
	private MessageQueue commandQueue;   // The message queue to be interpreted
	private ServerSender sender;         // The class that handles sending messages
	private ArrayList<Client> clients;   // List of clients connected to the game
	private int currClientPid;           // Running count of player clients' IDs
	private GameBroadcaster broadcaster;

	public Controller(ServerSender sender, MessageQueue commandQueue) {
		this.game = new Game();
		this.commandQueue = commandQueue;
		this.sender = sender;
		this.clients = new ArrayList<Client>();
		this.currClientPid = 1;
	}

	/*
	 * @see java.lang.Thread#run()
	 * This will parse out the messages send to the Server and interpret them
	 * and run the corresponding functions to update the Game state.
	 */
	@Override
	public void run() {
		while(true){
			DatagramPacket datagramMsg = null;
			if(game.isFinished()) {
				gameOver();
			}
			try {
				while(datagramMsg ==  null){
					if(game.isFinished()) {
						gameOver();
					}
					datagramMsg = commandQueue.pop();
				}
			} catch (Exception e) {
				System.out.println("Error in controller retreiving message from command queue");
			}
			String message = new String(datagramMsg.getData());
			JSONObject msg = null;
			try {
				msg = new JSONObject(message);
			} catch (Exception e) {
				System.out.println("Improperly formmated JSON: " + message.toString());
				serverResp(false, datagramMsg.getAddress(), datagramMsg.getPort());
			}

			String command = null;
			try {
				if(msg!=null) {
					command = msg.getString("command");
				}
			} catch (Exception e) {
				System.out.println("Improperly formatted request from client, no \"command\" key found");
				serverResp(false, datagramMsg.getAddress(), datagramMsg.getPort());
			}
			if((command != null) && (msg != null)) {
//				System.out.println("Controller handling command: \"" + command + "\"");
				if(command.equals("reset")) {
					gameOver(); // kill broadcaster
					// reset the game
					this.game = new Game();
					// We could take these out and keep the current clients... Maybe as
					this.clients = new ArrayList<Client>();
					this.currClientPid = 1;
					serverResp(true, datagramMsg.getAddress(), datagramMsg.getPort());
				}
				else if(command.equals("join")) {
					joinGame(msg.getString("type"), datagramMsg.getAddress(), datagramMsg.getPort());
				}
				else if(command.equals("load")) {
					game.loadBoard(msg.getJSONObject("game"));
					serverResp(true, datagramMsg.getAddress(), datagramMsg.getPort());
				}
				else{
					if(this.game.getNumPlayers() == 0) {
						System.out.println("No clients have \"join\"(ed) yet, cannot press buttons");
						serverResp(false, datagramMsg.getAddress(), datagramMsg.getPort());
					}
					else{
						int playerID = -1;
						try {
							playerID = msg.getInt("pid");
						}
						catch(Exception e) {
							System.out.println("Did not pass in playerID");
							serverResp(false, datagramMsg.getAddress(), datagramMsg.getPort());
						}
						if(command.equals("move")) {
							if(game.isStarted()) {
								game.playerMoved(playerID, msg.getString("direction"));
							}
							else{
								System.out.println("Cannot move, game has not started yet");
							}
						}
						else if(command.equals("button")){
							handleButton(playerID, msg.getString("button"));
						}
						else{
							System.out.println("Improperly formatted JSON. Unknown command: " + command);
						}
					}
				}
			}
			if(this.game.getBuffer() != null){
//				this.game.getBuffer().updateGameState();
			}
		}
	}

	// Handles the scenario when a game ends
	// Dumps the state of the old game and creates
	// a new one. The number of players is preserved
	// so that they can all keep playing.
	private void gameOver() {
		JSONObject msg = new JSONObject();
		msg.put("type", "game_over");
		int winner = game.getWinnerId();
		if(winner > 0){
			msg.put("winner", winner);
		}
		sender.broadcastMessage(clients, msg.toString());

		// reset the game saving number of players
		int numPlayers = game.getNumPlayers();
		this.game = new Game();
		game.setNumPlayers(numPlayers);
		broadcaster.requestQuit();

	}

	/******* Helper functions to handle incoming message*******/
	// Handles scenario of a client joining the game.
	private void joinGame(String type, InetAddress addr, int port) {
		System.out.println("Client["+type+"] requested to join game");
		Client client = null;
		JSONObject response = new JSONObject();
		boolean status = true;

		if(type.equals("player")) {
			if(game.addPlayer()) {
				status = true;
				client = new Client(addr, port, this.currClientPid++);
				response.put("pid", client.getId());
				clients.add(client);
			}
			else{
				status = false;
			}
		}
		else if(type.equals("spectator")) {
			status = true;
			client = new Client(addr, port);
			clients.add(client);
		}

		response.put("type", type + "_join");
		String resp = status ? "Success" : "Failure";
		response.put("resp", resp);
		sender.sendMsg(response.toString(), addr, port);
	}

	// Handles if the player pressed a button.
	private void handleButton(int playerID, String buttonPressed){
		Client client = getClient(playerID);
		if(client == null) {
			System.out.println("Client with that ID cannot be found");
			return;
		}
		if(buttonPressed.equals("start")) {
			if(game.isStarted()){
				System.out.println("Cannot start the game, it has already started");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
			else if(game.isFinished()) {
				System.out.println("Cannot start the game, it has already finished");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
			else if(this.game.getNumPlayers() == 0) {
				System.out.println("Cannot start the game, client has not joined");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
			else if(this.game.getNumPlayers() > this.currClientPid) {
				System.out.println("Cannot start the game, there are more players in the game than clients");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
			else {
				game.startGame();
				this.broadcaster = new GameBroadcaster(this.sender, this.clients, game.getBuffer());
				this.broadcaster.start();
			}
		}
		else if(buttonPressed.equals("end")) {
			if(game.isStarted()) {
				gameOver();
			}
			else {
				System.out.println("Cannot end game. It has not yet started.");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
		}
		else if(buttonPressed.equals("reset")) {
			//TODO - next milestone
			game.resetPlayer(playerID);
		}
		else if(buttonPressed.equals("deploy")) {
			if(game != null){
				if(game.isStarted() && !game.isFinished()){
					game.dropBomb(playerID);
				}
			}
		}
		else {
			System.out.println("Invalid JSON object sent from the client.");
		}
	}

	// isSuccess indicates success/failure.
	// IP is address to respond to.
	// port is the port at that address.
	private void serverResp(boolean isSuccess, InetAddress addr, int port){
		JSONObject response = new JSONObject();
		response.put("type", "response");
		String resp = isSuccess?"Success":"Failure";
		response.put("resp", resp);
		sender.sendMsg(response.toString(), addr, port);
	}

	// Get a specific client based on their ID
	// only works for player clients since
	// spectator clients cannot do anything
	private Client getClient(int id) {
		for(Client client : clients) {
			if(client.getId() == id) {
				return client;
			}
		}
		return null;
	}
}
