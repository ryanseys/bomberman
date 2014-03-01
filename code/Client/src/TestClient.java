
public class TestClient {
	ClientSender cs;
	ClientReceiver cr;
	public TestClient() {
		cs = new ClientSender();
		cr = new ClientReceiver();
		cs.start();
		cr.start();

		try {
			cs.join();
			cr.join();
		} catch (InterruptedException e) {
			System.out.println("An unexpected interrupt exception occurred!");
			e.printStackTrace();
		}
	}

	public void runAllTests() {
		System.out.println("TODO: Add tests here.");
	}
}
