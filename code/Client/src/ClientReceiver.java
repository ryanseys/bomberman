import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class ClientReceiver extends Thread {
	private MessageQueue recMsgQ;
	private DatagramSocket dsocket;
	private DatagramPacket data;
	byte[] receiveData;
	public ClientReceiver(MessageQueue recMsgQ, DatagramSocket dsocket) throws SocketException {
		this.recMsgQ = recMsgQ;
		this.dsocket = dsocket;
	}

	@Override
	public void run() {
		while(true) {
			receiveData = new byte[1024];
			data = new DatagramPacket(receiveData, receiveData.length);
			try {
				dsocket.receive(data);
			} catch (IOException e) {
				e.printStackTrace();
			}

			synchronized(recMsgQ) {
				recMsgQ.add(new String((data.getData())));
				recMsgQ.notify();
			}
		}

	}

}
