package com.bomberman.test;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bomberman.server.Server;

public class TestServer {
	Server server;

	@Before
	/**
	 * Set up happens before every test case starts.
	 * @throws Exception
	 */
	public void setUp() throws Exception {
		server = new Server(); // create and start server
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
