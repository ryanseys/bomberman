
public class TestMain {
	static int port = 5000;
	static String server = "127.0.0.1";
	public static void main(String args[]) {
		System.out.println("Running tests...");
		TestClient tc = new TestClient(server, port);
		try {
			tc.runAllTests();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished running tests!");
	}
}
