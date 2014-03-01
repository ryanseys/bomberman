import java.net.DatagramSocket;


public class ServerSender extends Thread {
	private DatagramSocket serverSocket;
	public ServerSender(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	@Override
	public void run() {
		
		super.run();
	}
	public void sendClientMsg(Client client, String msg){
		
	}
	public void braodcastGameState(Game game){
		
	}

}
