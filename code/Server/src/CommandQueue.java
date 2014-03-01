import java.util.LinkedList;

public class CommandQueue {
	private LinkedList<String> commandQueue;
	
	public CommandQueue() {
		commandQueue = new LinkedList<String>();
	}
	public synchronized void addCommand(String command){
		commandQueue.add(command);
		this.notify();
	}
	public synchronized String popCommand(){
		return commandQueue.pop();
	}
	public synchronized boolean isEmpty(){
		return commandQueue.isEmpty();
	}
}
