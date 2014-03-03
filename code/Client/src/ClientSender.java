import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class ClientSender extends Thread {
	private MessageQueue sendMsgQ;
	private DatagramSocket dsocket;
	private DatagramPacket data;
	private InetAddress ip;
	private int port;
	
	public ClientSender(MessageQueue sendMsgQ, InetAddress IPAddress, int port, DatagramSocket dsocket) throws SocketException {
		this.sendMsgQ=sendMsgQ;
		this.dsocket = dsocket;
		this.ip = IPAddress;
		this.port = port;
	}
	
	@Override
	public void run() {
		String s;
		while(true) {
			synchronized(sendMsgQ)
			{
				while(sendMsgQ.isEmpty())
				{
					try {
						sendMsgQ.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				s = sendMsgQ.pop();
				data = new DatagramPacket(s.getBytes(), s.length(), this.ip, this.port);
				try {
					dsocket.send(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}	
	}
}
