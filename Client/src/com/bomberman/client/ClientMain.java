package com.bomberman.client;
public class ClientMain {

	/**
	 * Create a client and client view. Used to play the game for real.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		final int SERVER_PORT = 5000;

		Client client = new Client("localhost", SERVER_PORT);
		ClientView cv = new ClientView(client);

		System.out.println("Client started...");
		while(true) {
			// wait for the response...
			String serverMsg = client.receive();

			// set the state of the game based on the response
			client.setState(serverMsg);

			// update view here (uses the state of the client to render)
			cv.render();
		}
	}
}
