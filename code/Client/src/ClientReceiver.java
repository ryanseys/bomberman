import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class ClientReceiver extends Thread {
	private static final int size = 1000;
	private MessageQueue recMsgQ;
	private DatagramSocket dsocket;
	private DatagramPacket data;
	public ClientReceiver(MessageQueue recMsgQ,DatagramSocket dsocket) {
		// TODO Auto-generated constructor stub
		this.recMsgQ=recMsgQ;
		this.dsocket=dsocket;
	}

	@Override
	public void run() {
		byte strdata[]=new byte[size];
		// TODO Auto-generated method stub
		data= new DatagramPacket(strdata,size);
		try {
			dsocket.receive(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		synchronized(recMsgQ)
		{
			recMsgQ.add(new String((data.getData())));
		}
		recMsgQ.notify();	
	}	

}
