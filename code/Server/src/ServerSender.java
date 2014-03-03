import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


public class ServerSender {
	private DatagramSocket serverSocket;
	public ServerSender(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
	}		
	public void broadcastMessage(ArrayList<Client> clients, String message){	
		System.out.println("Broadcast Message: " + message);
		for (Client client : clients) {
			sendClientMsg(client, message);
		}
	}
	public void sendClientMsg(Client client, String message){
		sendMsg(message, client.getIPaddr(), client.getPort());
	}
	public void sendMsg(String message, InetAddress IP, int port){
		byte[] msg = message.getBytes();
		 DatagramPacket packet = new DatagramPacket(msg, msg.length, IP, port);
		try {
			serverSocket.send(packet);
		} catch (IOException e) {
			System.out.println("Error broadcasting message");
			return;
		}
	}
}
