import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;


public class ClientMain {
	public static void main(String[] args) throws Exception {
		final int SERVER_PORT = 5000;
		
		Client client = new Client("localhost", SERVER_PORT);
		ClientView cv = new ClientView(client);
		
		System.out.println("Client started...");
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		
		boolean gameOn = true;
		while(gameOn) {
			System.out.print("> ");
			String msg = userInput.readLine();
			
			// send a message
			client.send(msg);
			
			// wait for the response...
			String serverMsg = client.receive();
			
			client.setState(serverMsg); // set the state of the game based on the response
			
			// update view here (uses the state of the client to render)
			cv.render();

			JSONObject resp = new JSONObject(serverMsg);
		
			JSONObject game = null;
			if(resp.getString("type").equals("game_over")) {
				gameOn = false;
			}
			if(resp.keySet().contains("game")) {
				game=resp.getJSONObject("game");
				
				JSONArray boardArray = game.getJSONArray("board");
				for (int i = 0; i < boardArray.length(); i++) {
					JSONArray currArr = boardArray.getJSONArray(i);
					for(int j = 0; j < currArr.length(); j++){
						System.out.print("["+currArr.getInt(j)+"]");		
					}
					System.out.println();
				}
			}
		}
	}
}
