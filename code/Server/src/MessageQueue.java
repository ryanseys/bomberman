import java.net.DatagramPacket;
import java.util.LinkedList;

// Synchronized queue for messages to be lined up for the 
// controller to handle
public class MessageQueue {
	private LinkedList<DatagramPacket> messageQueue;
	
	public MessageQueue() {
		messageQueue = new LinkedList<DatagramPacket>();
	}
	// Add a message to the end of the queue, synchronized
	// so that a message isn't added while the controller is reading
	public synchronized void add(DatagramPacket message){
		System.out.println("MessageQueue: adding a message");
		messageQueue.add(message);
		notify();
	}
	// Retrieve the first element of the list if its there, otherwise wait
	// for something to be added to the list.
	public synchronized DatagramPacket pop() throws InterruptedException{
		while(messageQueue.isEmpty()){
			System.out.println("Controller waiting in MessageQueue");
			wait();
		}
		return messageQueue.pop();
	}
}
