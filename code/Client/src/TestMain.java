
public class TestMain {
	static int port = 5000;
	static String server = "127.0.0.1";
	public static void main(String args[]) {
		System.out.println("Running tests...");
		TestClient tc = new TestClient(server, port);
		tc.runAllTests();
		System.out.println("Finished running tests!");
	}
}
