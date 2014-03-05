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
	private boolean shouldQuit = false;

	public ClientSender(MessageQueue sendMsgQ, InetAddress IPAddress, int port, DatagramSocket dsocket) throws SocketException {
		this.sendMsgQ=sendMsgQ;
		this.dsocket = dsocket;
		this.ip = IPAddress;
		this.port = port;
	}

	@Override
	public void run() {
		String s;
		while(!shouldQuit) {
			synchronized(sendMsgQ)
			{
				while(sendMsgQ.isEmpty() && !shouldQuit)
				{
					try {
						sendMsgQ.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(shouldQuit) {
					return;
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
		System.out.println("Holy shit exiting sender!");
	}

	/**
	 * Used to request that the thread quit itself
	 */
	public void requestQuit() {
		shouldQuit = true;

		// notify my own message queue to wake up and check state
		synchronized(sendMsgQ) {
			sendMsgQ.notify();
		}
	}
}
