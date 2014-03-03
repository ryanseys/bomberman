import java.io.*;
import java.net.*;

import javax.swing.JFrame;

import org.json.*;

public class Client {

	public static void main(String[] args) throws Exception {
		final int SERVER_PORT = 5000;

		/*** TEMP READER FOR TESTING ***/
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

		DatagramSocket clientSocket = new DatagramSocket();

		// For now localhost, prompt later for external host
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData;
		byte[] receiveData;
		
		System.out.println("Client Started");
		
//		JFrame frame = new ClientView();
//	    frame.setVisible(true);
		boolean gameOn = true;
		while(gameOn){
			sendData = new byte[2048];
			receiveData = new byte[2048];
			String msg = userInput.readLine();
			sendData = msg.getBytes();
			// Send message to the Server
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, SERVER_PORT);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			// Get message from the Server
			clientSocket.receive(receivePacket);
			String serverMsg = new String(receivePacket.getData());

			JSONObject resp = new JSONObject(serverMsg);
			JSONObject game = null;
			if(resp.getString("type").equals("game_over"))
				gameOn = false;
			System.out.println("Resp from SERVER:" + resp.toString());
			if(resp.keySet().contains("game")){
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
		//clientSocket.close();
	}

}
