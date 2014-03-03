import java.net.DatagramSocket;
import java.net.SocketException;


public class TestClient {
	MessageQueue toSendMsgs = new MessageQueue();
	MessageQueue receivedMsgs = new MessageQueue();
	ClientReceiver cr;
	ClientSender cs;
	public TestClient(String IPAddress, int port) throws SocketException {
		DatagramSocket socket = new DatagramSocket();
		cs = new ClientSender(toSendMsgs, socket);
		cr = new ClientReceiver(receivedMsgs, socket);
		cs.start();
		cr.start();

		try {
			cs.join();
			cr.join();
		} catch (InterruptedException e) {
			System.out.println("An unexpected interrupt exception occurred!");
			e.printStackTrace();
		}
	}

	public void runAllTests() throws InterruptedException {
		toSendMsgs.add("up");
		synchronized(receivedMsgs) {
			while(receivedMsgs.isEmpty()) {
				receivedMsgs.wait();
			}
			// check that the message received is correct
			
		}
		System.out.println("TODO: Add tests here.");
	}
}
