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
	}
	
	/*
	 * @see java.lang.Thread#run()
	 * This will parse out the message and run the operations against game 
	 */
	public void run(){
		synchronized (commandQueue) {
			while(commandQueue.isEmpty()){
				try {
					commandQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		DatagramPacket datagramMsg = commandQueue.pop();
		String message = new String(datagramMsg.getData()); 
		System.out.println(message.toString());
		
		JSONObject msg = new JSONObject(message);
		String command = null;
		try {
			command = msg.getString("command");	
		} catch (Exception e) {
			System.out.println("Improperly formatted request from client, no \"command\" key found");
			return;
		}
		
		if(command == "join"){
			joinGame(datagramMsg.getAddress(), datagramMsg.getPort());
		}
		
		else{
			int playerID = msg.getInt("pID");
			
			if(command == "move"){
				game.playerMoved(playerID, msg.getString("Direction"));
			}
			else if(command == "button"){
				handleButton(playerID, msg.getString("button"));
			}
			else{
				System.out.println("Improperly formatted JSON. Unknown command: " + command);
			}
		}
	}

	/******* Helper functions to handle incoming message*******/
	private void joinGame(InetAddress addr, int port){
	
		Client client = new Client(addr, port);
		boolean status = true;
		if(game.addPlayer()){
			clients[clients.length-1] = client;
		}
		else{
			status = false;
		}
		JSONObject response = new JSONObject();
		response.put("type", "join");
		int id = status ? client.getId() : -1;
		response.put("pid", id);
		sender.sendMsg(response.toString(), addr, port);
	}
	
	private void handleButton(int playerID, String buttonPressed){
		if(buttonPressed == "start"){
			game.startGame();
			sender.broadcastMessage (clients, game.toString());
		}
		else if(buttonPressed == "end"){
			game.endGame();
		}
		else if(buttonPressed == "reset"){
			game.resetPlayer(playerID);
		}
		else if(buttonPressed == "deploy"){
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
}
