package com.bomberman.client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class ClientReceiver extends Thread {
	private MessageQueue recMsgQ;
	private DatagramSocket dsocket;
	private DatagramPacket data;
	byte[] receiveData;
	private boolean shouldQuit = false;

	public ClientReceiver(MessageQueue recMsgQ, DatagramSocket dsocket) throws SocketException {
		this.recMsgQ = recMsgQ;
		this.dsocket = dsocket;
	}

	@Override
	/**
	 * Thread that receives messages and adds them to a shared queue.
	 */
	public void run() {
		while(!shouldQuit) {
			receiveData = new byte[1024];
			data = new DatagramPacket(receiveData, receiveData.length);

			try {
				dsocket.receive(data);
			} catch (IOException e) {
				// if we closed the socket, because we are quitting
				// return because we are done!
				if(shouldQuit) {
					return;
				}
			}

			// add to the message queue (synced)
			synchronized(recMsgQ) {
				recMsgQ.add(new String((data.getData())));
				recMsgQ.notify();
			}
		}
	}

	/**
	 * Used to request that the thread should quit itself
	 */
	public void requestQuit() {
		shouldQuit = true;
		dsocket.close();
	}
}
