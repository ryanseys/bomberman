import java.net.*;

import org.json.*;

public class Controller extends Thread{
	private Game game;
	private CommandQueue commandQueue;
	private JSONObject msg;
	
	public Controller(Game game, CommandQueue commandQueue){
		this.game = game;
		this.commandQueue = commandQueue;
		//this.msg = new JSONObject((new String(recPacket.getData())));
	}
	
	/*
	 * TODO
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
		String message = commandQueue.popCommand();
		System.out.println(message.toString());
		
		JSONObject msg = new JSONObject(message);
		String command = msg.getString("command");
		
		if(command == "join"){
			joinGame(msg.getJSONObject("clientInfo").getString("IP"), msg.getJSONObject("clientInfo").getInt("port"));
		}
		else{
			int playerID = msg.getInt("pID");
			
			if(command == "move"){
				game.playerMoved(playerID, msg.getString("Direction"));
			}
			else if(command == "button"){
				handleButton(msg.getString("button"));
			}
			else{
				System.out.println("Unknown command: " + command);
			}
		}
	}

	/******* Helper functions to handle incoming message*******/
	private void joinGame(String IP, int port){
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(IP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		game.addPlayer((new Client(addr, port)));
	}
	private void handleButton(String buttonPressed){
		if(buttonPressed == "start"){
			
		}
		else if(buttonPressed == "end"){
			
		}
		else if(buttonPressed == "reset"){
			
		}
		else if(buttonPressed == "deploy"){
			
		}
	}

}
