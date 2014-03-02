import java.net.DatagramPacket;
import java.util.LinkedList;

public class MessageQueue {
	private LinkedList<DatagramPacket> messageQueue;
	
	public MessageQueue() {
		messageQueue = new LinkedList<DatagramPacket>();
	}
	public synchronized void add(DatagramPacket message){
		System.out.println("MessageQueue: adding a message");
		messageQueue.add(message);
		notify();
	}
	public synchronized DatagramPacket pop() throws InterruptedException{
		while(messageQueue.isEmpty()){
			System.out.println("Controller waiting in MessageQueue");
			wait();
		}
		return messageQueue.pop();
	}
}
