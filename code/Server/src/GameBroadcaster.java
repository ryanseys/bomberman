import java.util.ArrayList;


public class GameBroadcaster extends Thread {
	private DoubleBuffer dblBuffer;
	private ServerSender sender;
	private ArrayList<Client> clients;
	private boolean shouldQuit = false;

	public GameBroadcaster(ServerSender sender, ArrayList<Client> clients, DoubleBuffer buffer) {
		this.sender = sender;
		this.clients = clients;
		this.dblBuffer = buffer;
	}

	@Override
	public void run(){
		String state = null;
		while(!shouldQuit){
			state = dblBuffer.broadcasterGetState();
			if(state != null){
				sender.broadcastMessage(clients, state);
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void requestQuit() {
		shouldQuit = true;
	}
}
