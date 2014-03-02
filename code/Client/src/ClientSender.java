import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;


public class ClientSender extends Thread {
	private MessageQueue sendMsgQ;
	private ClientView cView;
	private DatagramSocket dsocket;
	private DatagramPacket data;
	public ClientSender(MessageQueue sendMsgQ,DatagramSocket dsocket,ClientView cView) {
		this.cView=cView;
		this.sendMsgQ=sendMsgQ;
		this.dsocket=dsocket;
		
		// TODO Auto-generated constructor stub
	}
	@Override
	public void run() {
		// TODO Aut-generated method stub
		super.run();
		String s;
		synchronized(sendMsgQ)
		{
			while(sendMsgQ.isEmpty())
			{
				try {
					sendMsgQ.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			s = sendMsgQ.pop();
			data =new DatagramPacket(s.getBytes(),s.length(),dsocket.getInetAddress(),dsocket.getPort()); 
			try {
				dsocket.send(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
