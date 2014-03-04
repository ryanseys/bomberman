import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;


public class TestMain {
	static int port = 5000;
	static String server = "127.0.0.1";
	static String testcaseDirectory = "testcases/";
	static Client c;
	public static void main(String args[]) throws IOException {
		System.out.println("Running tests...");
		c = new Client(server, port);
		runAllTests(c);
		System.out.println("Finished running tests!");
	}

	public static void runTestCase(File f) throws IOException {
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
		JSONObject resetMsg = new JSONObject();
		resetMsg.put("command", "reset");
		c.send(resetMsg.toString());
	}

	public static void runAllTests(Client c) throws IOException {
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
