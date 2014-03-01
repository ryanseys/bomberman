import java.net.*;

import org.json.*;

public class Controller extends Thread{
	DatagramPacket recPacket;
	private Game game;
	private JSONObject msg;
	
	
	public Controller(DatagramPacket recPacket, Game game) {
		this.recPacket = recPacket;
		this.game = game;
		this.msg = new JSONObject((new String(recPacket.getData())));
	}
	
	/*
	 * TODO
	 * @see java.lang.Thread#run()
	 * This will parse out the message and run the operations against game 
	 */
	public void run(){
		System.out.println(msg.toString());
		String command = msg.get("command").toString();
		if(command == "join"){
			joinGame(recPacket.getAddress(), recPacket.getPort());
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
	private void joinGame(InetAddress IP, int port){
		game.addPlayer((new Client(IP, port)));
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
