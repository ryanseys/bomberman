import org.json.*;

public class Controller extends Thread{
	private JSONObject msg;
	private Game game;
	
	public Controller(String msg, Game game) {
		this.msg = new JSONObject(msg);
		this.game = game;
	}
	
	/*
	 * TODO
	 * @see java.lang.Thread#run()
	 * This will parse out the message and run the operations against game 
	 */
	public void run(){
		System.out.println(msg.toString());
		
		
	}

	/******* Where we define functions available to client *******/
//	private joinGame(){
//		Client c 
//	}

}
