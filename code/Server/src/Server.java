import java.net.*;

public class Server {
	public static void main(String[] args) throws Exception {
		Game game = new Game();

		byte[] recData = new byte[1024];
		DatagramSocket serverSocket = new DatagramSocket(3000);
			
		while(!game.isOver()){
			DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
			serverSocket.receive(recPacket);
			(new Controller(recPacket, game)).start();
		}	
	}
}
