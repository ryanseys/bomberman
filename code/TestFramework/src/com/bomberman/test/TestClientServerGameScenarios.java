package com.bomberman.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bomberman.client.Client;
import com.bomberman.server.Server;

public class TestClientServerGameScenarios {
	private String SERVER_ADDR = "localhost";
	private int SERVER_PORT = 5000;

	Client c1 = null;
	Client c2 = null;
	Client c3 = null;
	Client c4 = null;

	Server server;

	private String getFileContents(File file) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			byte[] data = new byte[(int)file.length()];
			fis.read(data);
			fis.close();
			return new String(data, "UTF-8");
		} catch (IOException e) {
			return "";
		}
	}

	@Before
	public void setUp() throws Exception {
		server = new Server();
		server.start();
		c1 = new Client(SERVER_ADDR, SERVER_PORT);
		c2 = new Client(SERVER_ADDR, SERVER_PORT);
		c3 = new Client(SERVER_ADDR, SERVER_PORT);
		c4 = new Client(SERVER_ADDR, SERVER_PORT);
	}

	@After
	public void tearDown() throws Exception {
		if(!(c1 == null)) {
			c1.quit();
		}
		if(!(c2 == null)) {
			c2.quit();
		}
		server.kill();
	}

	@Test
	public void testClientConnectAsPlayer() throws InterruptedException {
		c1.connect("player");
		String msg = c1.receiveNoBroadcasts();
		JSONObject expectedResp = new JSONObject();
		expectedResp.put("pid", 1);
		expectedResp.put("type", "player_join");
		expectedResp.put("resp", "Success");
		assertEquals(msg.trim(), expectedResp.toString().trim());
	}

	@Test
	public void testClientConnectTwoPlayers() throws InterruptedException {
		c1.connect("player");
		c2.connect("player");

		// receive message for p2
		String msg2 = c2.receiveNoBroadcasts();
		JSONObject expectedResp2 = new JSONObject();
		expectedResp2.put("pid", 2);
		expectedResp2.put("type", "player_join");
		expectedResp2.put("resp", "Success");
		assertEquals(msg2.trim(), expectedResp2.toString().trim());
	}

	@Test
	public void testClientConnectThreePlayers() throws InterruptedException {
		c1.connect("player");
		c2.connect("player");
		c3.connect("player");

		// receive message for p2
		String msg2 = c3.receiveNoBroadcasts();
		JSONObject expectedResp = new JSONObject();
		expectedResp.put("type", "player_join");
		expectedResp.put("resp", "Failure");
		assertEquals(msg2.trim(), expectedResp.toString().trim());
	}

	@Test
	public void testClientConnectFourPlayers() throws InterruptedException {
		c1.connect("player");
		c2.connect("player");
		c3.connect("player");
		c4.connect("player");

		// receive message for p2
		String msg2 = c4.receiveNoBroadcasts();
		JSONObject expectedResp = new JSONObject();
		expectedResp.put("type", "player_join");
		expectedResp.put("resp", "Failure");
		assertEquals(msg2.trim(), expectedResp.toString().trim());
	}

	@Test
	public void testClientLoadBoard() throws InterruptedException {
		JSONObject board1 = new JSONObject(getFileContents(new File("gameboards/game1.json")));
		c1.connect("player");
		String resp = c1.receiveNoBroadcasts();
		// load board
		JSONObject msg = new JSONObject();
		msg.put("command", "load");
		msg.put("game", board1);
		System.out.println(msg.toString());
		c1.send(msg.toString());
		// receive board load command response
		resp = c1.receiveNoBroadcasts();
		JSONObject expResp = new JSONObject();
		expResp.put("type", "response");
		expResp.put("resp", "Success");
		assertEquals(resp.trim(), expResp.toString().trim());
	}

	@Test
	public void testClientLoadBoardStartGame() throws InterruptedException {
		JSONObject board1 = new JSONObject(getFileContents(new File("gameboards/game1.json")));
		c1.connect("player");
		String resp = c1.receiveNoBroadcasts();
		// load board
		JSONObject msg = new JSONObject();
		msg.put("command", "load");
		msg.put("game", board1);
		System.out.println(msg.toString());
		c1.send(msg.toString());
		// receive board load command response
		resp = c1.receiveNoBroadcasts();
//		JSONObject expResp = new JSONObject();
//		expResp.put("type", "response");
//		expResp.put("resp", "Success");
//		assertEquals(resp.trim(), expResp.toString().trim());
		msg = new JSONObject();
		msg.put("command", "button");
		msg.put("pid", 1);
		msg.put("button", "start");
		c1.send(msg.toString());
		resp = c1.receive();
		System.out.println(resp);
		assertEquals(board1, (new JSONObject(resp)).get("game"));
	}
}
