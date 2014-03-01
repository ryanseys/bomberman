import java.net.*;

public class Server {
	private static final int port = 5000;
	public static void main(String[] args) throws Exception {
		Game game = new Game();

		byte[] recData = new byte[1024];
		DatagramSocket serverSocket = new DatagramSocket(port);
		System.out.println("Server started listening on port: " + port);
		while(!game.isFinished()){
			DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
			serverSocket.receive(recPacket);
			System.out.println("Server received a packet!");
			(new Controller(recPacket, game)).start();
		}
		serverSocket.close();
	}
}
