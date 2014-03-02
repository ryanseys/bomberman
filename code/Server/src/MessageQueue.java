import java.net.DatagramPacket;
import java.util.LinkedList;

public class MessageQueue {
	private LinkedList<DatagramPacket> messageQueue;
	
	public MessageQueue() {
		messageQueue = new LinkedList<DatagramPacket>();
	}
	public synchronized void add(DatagramPacket message){
		messageQueue.add(message);
		this.notify();
	}
	public synchronized DatagramPacket pop(){
		return messageQueue.pop();
	}
	public synchronized boolean isEmpty(){
		return messageQueue.isEmpty();
	}
}
