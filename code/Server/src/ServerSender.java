import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

// Server sender provides a set of functions for the 
// Server to send messages
public class ServerSender {
	private DatagramSocket serverSocket;  // Socket to send/receive messages on


	public ServerSender(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	// Send the specified message to all of the clients that are passed in
	public void broadcastMessage(ArrayList<Client> clients, String message){
		
		System.out.println("Broadcast Message: " + message);
		for (Client client : clients) {
			sendClientMsg(client, message);
		}
	}
	// Send the specified message to the client that is passed in
	public void sendClientMsg(Client client, String message){
		sendMsg(message, client.getIPaddr(), client.getPort());
	}
	
	// Send the specified message to the IP address and port that
	// were passed in
	public void sendMsg(String message, InetAddress IP, int port){
		if(port < 0){
			return; 
		}
		byte[] msg = message.getBytes();
		System.out.println("Sending message from server: " + message + "to: " + IP + ":" + port);
		DatagramPacket packet = new DatagramPacket(msg, msg.length, IP, port);
		try {
			serverSocket.send(packet);
		} catch (IOException e) {
			System.out.println("Error broadcasting message");
			return;
		}
	}
}
