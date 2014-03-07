import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TestDriver {
	String testcaseDirectory;
	String logDir; // directory to write logs
	Client c;

	TestDriver(Client c) {
		this.c = c;
		this.testcaseDirectory = "testcases/";
		this.logDir = "log/";
	}

	public void runTestCase(File f) throws IOException {
		boolean allTestsPassed = true;
		Logger log = new Logger(logDir + f.getName() + "-" + (new Date()).getTime() + ".log");

		log.append("Running test case: " + f.getName());

		// reset the server state first
		c.resetServer();

		// then run the test case
		String testcaseJSONString = getFileContents(f);
		JSONArray arr = new JSONArray(testcaseJSONString);
		for(int j = 0; j < arr.length(); j++) {
			JSONObject testcase = arr.getJSONObject(j);

			// get description
			String desc = null;
			try {
				desc = testcase.getString("desc");
			} catch(JSONException e) {}

			String request;
			try {
				request = testcase.getJSONObject("request").toString();
				c.send(request);
			}
			catch(JSONException e) {
				request = null;
			}
			String expectedResponse = testcase.getJSONObject("expectedResponse").toString().trim();

			String response = c.receive().trim();

			if(response.equals(expectedResponse)) {
				log.append("\nTest case passed!");

			}
			else {
				log.append("\nTest case failed!");
				allTestsPassed = false;
			}

			// log details about test case
			if(desc != null) {
				log.append("Description: \t\t" + desc);
			}
			if(request != null) {
				log.append("Request: \t\t" + request);
			}
			else {
				log.append("No request given for this test");
			}

			log.append("Expected response: \t" + expectedResponse);
			log.append("Actual response: \t" + response);
		}

		if(allTestsPassed) {
			System.out.println(" passed!");
		}
		else {
			System.out.println("FAILED!");
		}
	}

	public void runAllTests() throws IOException {
		File folder = new File(testcaseDirectory);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.print("Running test in " + listOfFiles[i].getName() + "...");
				runTestCase(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}

		// reset server one final time
//		c.resetServer();
	}

	private static String getFileContents(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int)file.length()];
		fis.read(data);
		fis.close();
		return new String(data, "UTF-8");
	}
}
