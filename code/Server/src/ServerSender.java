import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
