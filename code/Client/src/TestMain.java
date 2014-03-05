import java.io.IOException;


public class TestMain {

	public static void main(String args[]) throws IOException, InterruptedException {
		System.out.println("Running tests...");
		TestDriver t = new TestDriver(new Client("127.0.0.1", 5000));
		t.runAllTests();
		System.out.println("Finished running tests!");
	}
}
