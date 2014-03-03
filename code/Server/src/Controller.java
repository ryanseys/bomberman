import java.net.*;

import org.json.*;

public class Controller extends Thread{
	private Game game;
	private MessageQueue commandQueue;
	private ServerSender sender;
	private Client[] clients;
	
	public Controller(Game game, ServerSender sender, MessageQueue commandQueue){
		this.game = game;
		this.commandQueue = commandQueue;
		this.sender = sender;
		this.clients = new Client[Game.MAX_PLAYERS];
	}
	
	/*
	 * @see java.lang.Thread#run()
	 * This will parse out the message and run the operations against game 
	 */
	public void run(){
		while(!game.isFinished()){
			DatagramPacket datagramMsg = null;
			JSONObject msg = null;
			
			try {
				datagramMsg = commandQueue.pop();
			} catch (Exception e) {
				System.out.println("Error in controller retreiving message from command queue");
			}
			 
			String message = new String(datagramMsg.getData()); 
			
			try {
				msg = new JSONObject(message);
			} catch (Exception e) {
				System.out.println("Improperly formmated JSON: " + message.toString());
			}
			String command = null;
			try {
				command = msg.getString("command");	
			} catch (Exception e) {
				System.out.println("Improperly formatted request from client, no \"command\" key found");
				return;
			}
			System.out.println("Controller handling command: \"" + command + "\"");
			if(command.equals("join")){
				joinGame(datagramMsg.getAddress(), datagramMsg.getPort());
			}
			else if(command.equals("load")){
				game.loadBoard(msg.getJSONObject("game"));
			}
			else{
				int playerID = msg.getInt("pid");
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
			clients[client.getId()-1] = client;
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
		if(buttonPressed.equals("start")){
			if(game.isStarted()){
				System.out.println("Cannot start the game, it has already started");
			}
			else if(game.isFinished()){
				System.out.println("Cannot start the game, it has already finished");
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
	@SuppressWarnings("unused") // If we want to ack messages later...
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
}
