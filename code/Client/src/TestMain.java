import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;


public class TestMain {
	String testcaseDirectory;
	Client c;

	TestMain(Client c) {
		this.c = c;
		this.testcaseDirectory = "testcases/";
	}

	public static void main(String args[]) throws IOException, InterruptedException {
		System.out.println("Running tests...");
		TestMain t = new TestMain(new Client("127.0.0.1", 5000));
		t.runAllTests();
		System.out.println("Finished running tests!");
	}

	public void runTestCase(File f) throws IOException {
		// reset the server state first
		JSONObject resetMsg = new JSONObject();
		resetMsg.put("command", "reset");
		c.send(resetMsg.toString());
		c.receive(); // wait til it replies

		// then run the test case
		String testcaseJSONString = getFileContents(f);
		System.out.println(testcaseJSONString);
		JSONArray arr = new JSONArray(testcaseJSONString);
		for(int j = 0; j < arr.length(); j++) {
			JSONObject testcase = arr.getJSONObject(j);
			String request = testcase.getJSONObject("request").toString();
			String expectedResponse = testcase.getJSONObject("expectedResponse").toString().trim();
			c.send(request);
			String response = c.receive().trim();
			if(response.equals(expectedResponse)) {
				System.out.println("TESTCASE PASSED");
			}
			else {
				System.out.println("TESTCASE FAILED!");
				System.out.println("Expected response: " + expectedResponse);
				System.out.println("Actual response: " + response);
			}
		}

	}

	public void runAllTests() throws IOException {
		File folder = new File(testcaseDirectory);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("Running test in " + listOfFiles[i].getName());
				runTestCase(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}
	}

	public static String getFileContents(String filename) throws IOException {
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int)file.length()];
		fis.read(data);
		fis.close();
		return new String(data, "UTF-8");
	}

	public static String getFileContents(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int)file.length()];
		fis.read(data);
		fis.close();
		return new String(data, "UTF-8");
	}
}
