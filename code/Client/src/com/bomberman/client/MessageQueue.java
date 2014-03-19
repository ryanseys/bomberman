package com.bomberman.client;
import java.util.LinkedList;


public class MessageQueue {
	private LinkedList<String> messageQueue;

	public MessageQueue() {
		messageQueue = new LinkedList<String>();
	}
	public synchronized void add(String command) {
		messageQueue.add(command);
	}
	public synchronized String pop() {
		return messageQueue.pop();
	}
	public synchronized boolean isEmpty() {
		return messageQueue.isEmpty();
	}
	public synchronized void clear() {
		messageQueue.clear();
	}
}
