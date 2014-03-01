import java.util.LinkedList;


public class MessageQueue {
	private LinkedList<String> messageQueue;
	
	public MessageQueue() {
		messageQueue = new LinkedList<String>();
	}
	public synchronized void addCommand(String command){
		messageQueue.add(command);
	}
	public synchronized String popCommand(){
		return messageQueue.pop();
	}
	public synchronized boolean isEmpty(){
		return messageQueue.isEmpty();
	}
}
