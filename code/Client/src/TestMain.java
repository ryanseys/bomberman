import java.net.SocketException;
import java.net.UnknownHostException;


public class TestMain {
	static int port = 5000;
	static String server = "127.0.0.1";
	public static void main(String args[]) throws SocketException, UnknownHostException {
		System.out.println("Running tests...");
		Client c = new Client(server, port);
		runAllTests(c);
		System.out.println("Finished running tests!");
	}
	
	public static void runAllTests(Client c) {
		// Run tests using client object
		
		// Tests go here
	}
}


