import java.util.ArrayList;


public class GameBroadcaster extends Thread {
	private DoubleBuffer dblBuffer;
	private ServerSender sender;
	private ArrayList<Client> clients;
	
	public GameBroadcaster(ServerSender sender, ArrayList<Client> clients, DoubleBuffer buffer) {
		this.sender = sender;
		this.clients = clients;
		this.dblBuffer = buffer;
	}

	public void run(){
		String state = null;
		while(true){
			state = dblBuffer.broadcasterGetState();
			if(state != null){
				sender.broadcastMessage(clients, state);
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

}
