import java.io.*;
import java.net.*;

//import javax.swing.JFrame;

import org.json.*;

public class Client {
	MessageQueue toSendMsgs;
	MessageQueue receivedMsgs;
	ClientReceiver cr;
	ClientSender cs;
	DatagramSocket dsocket;
	
	
	public Client(InetAddress IPAddress, int port) throws SocketException {
		this.toSendMsgs = new MessageQueue();
		this.receivedMsgs = new MessageQueue();
		dsocket = new DatagramSocket();
		
		// sender and receiver must use the same socket!
		cs = new ClientSender(toSendMsgs, IPAddress, port, dsocket);
		cr = new ClientReceiver(receivedMsgs, dsocket);
		
		// start the threads for receiving and sending
		cs.start();
		cr.start();
//		ClientView cv = new ClientView();
	}
	
	public void send(String msg) {
		System.out.println("Sending message: " + msg);
		synchronized(toSendMsgs) {
			toSendMsgs.add(msg);
			toSendMsgs.notify();
		}
	}
	
	public String receive() {
		String s;
		synchronized(receivedMsgs) {
			while(receivedMsgs.isEmpty()) {
				try {
					receivedMsgs.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			s = receivedMsgs.pop();
		}
		System.out.println("Received message: " + s);
		return s;
	}

	public static void main(String[] args) throws Exception {
		final int SERVER_PORT = 5000;
		
		Client client = new Client(InetAddress.getByName("localhost"), SERVER_PORT);
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
			
			// update view here

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
//		clientSocket.close();
	}

}
//