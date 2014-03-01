import java.net.*;

import org.json.*;

public class Controller extends Thread{
	DatagramPacket recPacket;
	private Game game;
	private JSONObject msg;
	
	
	public Controller(DatagramPacket recPacket, Game game) throws JSONException {
		this.recPacket = recPacket;
		this.game = game;
		this.msg = new JSONObject((new String(recPacket.getData())));
	}
	
	public String getStringFromKey(String s) {
		try {
			return msg.get(s).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public int getIntFromKey(String s) {
		try {
			return msg.getInt(s);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return -1;
		}
	}
	
	/*
	 * TODO
	 * @see java.lang.Thread#run()
	 * This will parse out the message and run the operations against game 
	 */
	public void run(){
		System.out.println(msg.toString());
		String command = getStringFromKey("command");
		if(command == "join"){
			joinGame(recPacket.getAddress(), recPacket.getPort());
		}
		else{
			int playerID = getIntFromKey("pID");
			
			if(command == "move"){
				game.playerMoved(playerID, getStringFromKey("Direction"));
			}
			else if(command == "button"){
				handleButton(getStringFromKey("button"));
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
