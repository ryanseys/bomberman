//import java.io.BufferedReader;
//import java.io.InputStreamReader;


public class ClientMain {
	public static void main(String[] args) throws Exception {
		final int SERVER_PORT = 5000;
		
		Client client = new Client("localhost", SERVER_PORT);
		ClientView cv = new ClientView(client);
		
		System.out.println("Client started...");
//		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		
		boolean gameOn = true;
		while(true) {
//			System.out.print("> ");
//			String msg = userInput.readLine();
			
			// send a message
//			client.send(msg);
			
			// wait for the response...
			String serverMsg = client.receive();
			
			client.setState(serverMsg); // set the state of the game based on the response
			
			// update view here (uses the state of the client to render)
			cv.render();
		}
	}
}
