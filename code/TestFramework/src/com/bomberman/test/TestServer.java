package com.bomberman.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.bomberman.server.Server;

public class TestServer {
	@Rule public TestName testName = new TestName(); // Requires junit 4.7
	String logDir = "log/";
	Server server;

	@Before
	/**
	 * Set up happens before every test case starts.
	 * @throws Exception
	 */
	public void setUp() throws Exception {
		server = new Server(); // create and start server
		// Redirects System.out.println to a file :D
		try {
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(logDir +
					this.getClass().getSimpleName() + "-" + // this class name (yay reflection!)
					testName.getMethodName() + "-" + // append the test method name
					(new Date()).getTime() + ".txt")), true));
		} catch (Exception e) {
		     e.printStackTrace();
		}
	}

	@After
	/**
	 * tearDown happens after every test case finishes.
	 * @throws Exception
	 */
	public void tearDown() throws Exception {
		server.kill(); // kill the server
	}

	@Test
	/**
	 * Assert that the server is running once object is created.
	 * @throws Exception
	 */
	public void testServerIsRunning() throws Exception {
		assertTrue(server.isRunning());
	}

	@Test
	/**
	 * Assert that a server has a port when started.
	 * @throws Exception
	 */
	public void testServerPort() throws Exception {
		int port = server.getPort();
		assertTrue(port == 5000);
	}

	@Test
	/**
	 * Assert that the message queue is empty at the initial server state
	 * @throws Exception
	 */
	public void testServerEmptyMessageQueue() throws Exception {
		assertTrue(server.getMessageQueue().isEmpty());
	}
}
