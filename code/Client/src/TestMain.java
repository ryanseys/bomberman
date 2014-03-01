
public class TestMain {
	
	public static void main(String args[]) {
		System.out.println("Running tests...");
		TestClient tc = new TestClient();
		tc.runAllTests();
		System.out.println("Finished running tests!");
	}
}
