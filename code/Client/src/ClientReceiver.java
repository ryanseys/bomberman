import java.net.DatagramSocket;


public class ClientReceiver extends Thread {
	private MessageQueue recMsgQ;
	private ClientView cView;
	public ClientReceiver(MessageQueue recMsgQ,ClientView cView) {
		// TODO Auto-generated constructor stub
		this.cView=cView;
		this.recMsgQ=recMsgQ;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized(recMsgQ)
		{
			while(recMsgQ.isEmpty())
			{
				
			}
		}
	}	

}
