import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

import org.json.JSONObject;

public class Controller extends Thread{
	private Game game;
	private MessageQueue commandQueue;
	private ServerSender sender;
	private ArrayList<Client> clients;
	private int currClientPid;

	public Controller(ServerSender sender, MessageQueue commandQueue){
		this.game = new Game();
		this.commandQueue = commandQueue;
		this.sender = sender;
		this.clients = new ArrayList<Client>();
		this.currClientPid = 1;
	}

	/*
	 * @see java.lang.Thread#run()
	 * This will parse out the message and run the operations against game
	 */
	@Override
	public void run(){
		while(true){
			DatagramPacket datagramMsg = null;
			if(game.isFinished()){
				gameOver();
			}
			try {
				datagramMsg = commandQueue.pop();
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
			if((command != null) && (msg != null)){
				System.out.println("Controller handling command: \"" + command + "\"");
				if(command.equals("reset")) {
					// reset the game
					this.game = new Game();
					
					// We could take these out and keep the current clients... Maybe as 
					this.clients = new ArrayList<Client>();
					this.currClientPid = 1;
					serverResp(true, datagramMsg.getAddress(), datagramMsg.getPort());
				}
				else if(command.equals("join")){
					joinGame(msg.getString("type"), datagramMsg.getAddress(), datagramMsg.getPort());
				}
				else if(command.equals("load")){
					game.loadBoard(msg.getJSONObject("game"));
					serverResp(true, datagramMsg.getAddress(), datagramMsg.getPort());
				}
				else{
					if(this.game.getNumPlayers() == 0){
						System.out.println("No clients have \"join\"(ed) yet, cannot press buttons");
						serverResp(false, datagramMsg.getAddress(), datagramMsg.getPort());
					}
					else{
						int playerID = -1;
						try{
							playerID = msg.getInt("pid");
						}catch(Exception e){
							System.out.println("Did not pass in playerID");
							serverResp(false, datagramMsg.getAddress(), datagramMsg.getPort());
						}
						if(command.equals("move")){
							if(game.isStarted()){
								game.playerMoved(playerID, msg.getString("direction"));
								broadcastGameState();
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
		}
	}

	private void gameOver() {
		// reset the game saving number of players
		int numPlayers = game.getNumPlayers();
		this.game = new Game();
		game.setNumPlayers(numPlayers);
		
		JSONObject msg = new JSONObject();
		msg.put("type", "game_over");
		sender.broadcastMessage(clients, msg.toString());
	}

	/******* Helper functions to handle incoming message*******/
	private void joinGame(String type, InetAddress addr, int port){
		System.out.println("Client["+type+"] requested to join game");
		Client client = null;
		JSONObject response = new JSONObject();
		boolean status = true;

		if(type.equals("player")){
			if(game.addPlayer()){
				status = true;
				client = new Client(addr, port, this.currClientPid++, true);
				response.put("pid", client.getId());
				clients.add(client);
			}
			else{
				status = false;
			}
		}
		else if(type.equals("spectator")){
			status = true;
			client = new Client(addr, port, false);
			clients.add(client);
		}

		response.put("type", type + "_join");
		//		int id = status ? client.getId() : -1;
		String resp = status ? "Success" : "Failure";
		//		response.put("pid", id);
		response.put("resp", resp);
		sender.sendMsg(response.toString(), addr, port);
		//		sender.sendClientMsg(client, response.toString());
	}

	private void handleButton(int playerID, String buttonPressed){
		Client client = getClient(playerID);
		if(client == null){
			System.out.println("Client with that ID cannot be found");
			return;
		}
		if(buttonPressed.equals("start")){
			if(game.isStarted()){
				System.out.println("Cannot start the game, it has already started");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
			else if(game.isFinished()){
				System.out.println("Cannot start the game, it has already finished");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
			else if(this.game.getNumPlayers() == 0){
				System.out.println("Cannot start the game, client has not joined");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
			else if(this.game.getNumPlayers() > this.currClientPid){
				System.out.println("Cannot start the game, there are more players in the game than clients");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
			else{
				game.startGame();
				broadcastGameState();
			}
		}
		else if(buttonPressed.equals("end")){
			if(game.isStarted()){
				game.endGame();
			}
			else{
				System.out.println("Cannot end game. It has not yet started.");
				serverResp(false, client.getIPaddr(), client.getPort());
			}
		}
		else if(buttonPressed.equals("reset")){
			//TODO - next milestone
			game.resetPlayer(playerID);
		}
		else if(buttonPressed.equals("deploy")){
			//TODO - next milestone
			game.dropBomb(playerID);
		}
		else{
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
	private void broadcastGameState(){
		JSONObject msg = new JSONObject();
		msg.put("game", game.toJSON());
		msg.put("type", "broadcast");
		System.out.println(msg.get("game"));
		sender.broadcastMessage(clients, msg.toString());
	}
	private Client getClient(int id){
		for(Client client : clients){
			if(client.getId() == id) {
				return client;
			}
		}
		return null;
	}

	public boolean isGameFinished() {
		return this.game.isFinished();
	}
}
