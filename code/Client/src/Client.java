import java.io.*;
import java.net.*;

import javax.swing.JFrame;

public class Client {

	public static void main(String[] args) throws Exception {
		final int SERVER_PORT = 5000;

		/*** TEMP READER FOR TESTING ***/
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

		DatagramSocket clientSocket = new DatagramSocket();

		// For now localhost, prompt later for external host
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		
		System.out.println("Client Started");
		
		JFrame frame = new ClientView();
	    frame.setVisible(true);

		while(true){
			String msg = userInput.readLine();
			sendData = msg.getBytes();
			// Send message to the Server
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, SERVER_PORT);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			// Get message from the Server
			clientSocket.receive(receivePacket);
			String serverMsg = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + serverMsg);

		}
		//clientSocket.close();
	}

}
