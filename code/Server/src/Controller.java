import java.net.*;

import org.json.*;

public class Controller extends Thread{
	private Game game;
	private MessageQueue commandQueue;
	private ServerSender sender;
	private Client[] clients;
	private int numClients;
	
	public Controller(Game game, ServerSender sender, MessageQueue commandQueue){
		this.game = game;
		this.commandQueue = commandQueue;
		this.sender = sender;
		this.clients = new Client[Game.MAX_PLAYERS];
		this.numClients = 0;
	}
	
	/*
	 * @see java.lang.Thread#run()
	 * This will parse out the message and run the operations against game 
	 */
	public void run(){
		while(!game.isFinished()){
			DatagramPacket datagramMsg = null;
			
			
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
				if(msg!=null)
					command = msg.getString("command");	
			} catch (Exception e) {
				System.out.println("Improperly formatted request from client, no \"command\" key found");
				serverResp(false, datagramMsg.getAddress(), datagramMsg.getPort());
			}
			if(command != null && msg != null){
				System.out.println("Controller handling command: \"" + command + "\"");
				if(command.equals("join")){
					joinGame(datagramMsg.getAddress(), datagramMsg.getPort());
				}
				else if(command.equals("load")){
					game.loadBoard(msg.getJSONObject("game"));
					serverResp(true, datagramMsg.getAddress(), datagramMsg.getPort());
				}
				else{
					if(numClients == 0){
						System.out.println("No clients have \"join\"(ed) yet, cannot press buttons");
						serverResp(false, datagramMsg.getAddress(), datagramMsg.getPort());
					}
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
		gameOver();		
	}

	private void gameOver() {
		JSONObject msg = new JSONObject();
		msg.put("type", "game_over");
		sender.broadcastMessage(clients, msg.toString());
	}

	/******* Helper functions to handle incoming message*******/
	private void joinGame(InetAddress addr, int port){
		System.out.println("Client requested to join game");
		Client client = new Client(addr, port);
		boolean status;
		if(game.addPlayer()){
			clients[numClients++] = client;
			status = true;
		}
		else{
			status = false;
		}
		JSONObject response = new JSONObject();
		response.put("type", "join");
		int id = status ? client.getId() : -1;
		response.put("pid", id);
		sender.sendClientMsg(client, response.toString());
	}
	
	private void handleButton(int playerID, String buttonPressed){
		Client client = getClient(playerID);
		if(client == null){
			System.out.println("Client with that ID cannot be found");
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
			else if(numClients == 0){
				System.out.println("Cannot start the game, client has not joined");
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
		for(int i = 0; i < numClients; i++){
			if(clients[i].getId() == id){
				return clients[i];
			}
		}
		return null;
	}
}
