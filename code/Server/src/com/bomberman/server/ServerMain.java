package com.bomberman.server;


public class ServerMain {

	public static void main(String[] args) throws Exception {
		Server s = new Server(args);
		s.start();
		s.join();
	}

}
