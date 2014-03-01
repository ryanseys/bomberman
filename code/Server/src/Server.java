import java.net.*;

public class Server {
	private static final int port = 3000;
	public static void main(String[] args) throws Exception {
		Game game = new Game();

		byte[] recData = new byte[1024];
		DatagramSocket serverSocket = new DatagramSocket(port);

		while(!game.isFinished()){
			DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
			serverSocket.receive(recPacket);
			(new Controller(recPacket, game)).start();
		}
	}
}
