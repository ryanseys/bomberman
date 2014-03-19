package com.bomberman.test;
import java.io.IOException;

import com.bomberman.client.Client;


public class TestMain {

	public static void main(String args[]) throws IOException, InterruptedException {
		System.out.println("Running tests...");
		Client c = new Client("127.0.0.1", 5000);
		TestDriver t = new TestDriver(c);
		t.runAllTests();
		c.quit();
		System.out.println("Finished running tests!");
	}
}
