package com.bomberman.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.bomberman.client.Action;
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
		c1.send(msg.toString());

		// receive board load command response
		resp = c1.receiveNoBroadcasts();
		msg = new JSONObject();
		msg.put("command", "button");
		msg.put("pid", 1);
		msg.put("button", "start");
		c1.send(msg.toString());

		resp = c1.receive();
		System.out.println(resp);
		assertEquals(board1.toString(), (new JSONObject(resp)).get("game").toString().replace('5', '0'));
	}

	@Ignore
	@Test
	public void testClientMoveDown() {
		fail("Not implemented.");
	}

	@Ignore
	@Test
	public void testClientMoveUp() {
		fail("Not implemented.");
	}

	@Ignore
	@Test
	public void testClientMoveRight() {
		fail("Not implemented.");
	}

	@Ignore
	@Test
	public void testClientMoveLeft() {
		fail("Not implemented.");
	}

	@Test
	public void testClientExitDoor() {
		String board = getFileContents(new File("gameboards/game_beside_door.json"));
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		// load board
		c1.loadGame(board);
		c1.receiveNoBroadcasts();

		// start game
		c1.startGame();
		c1.receive(); // receive new board state

		// move up
		c1.move(Action.UP);

		c1.flushMessages();
		String resp = c1.receive();

		// should be game over
		JSONObject expectedResp = new JSONObject();
		expectedResp.put("type", "game_over");

		assertEquals(resp.trim(), expectedResp.toString());
	}

	@Ignore
	@Test
	public void testClientMoveIntoBox() {
		fail("Not implemented.");
	}

	@Test
	public void testClientMoveIntoBoardLimitDown() {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game_move_into_game_edge_down.json")));

		// connect
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		// load board
		c1.loadGame(board.toString());
		c1.setState(c1.receiveNoBroadcasts());

		//start
		c1.startGame();

		c1.receive();

		//move down
		c1.move(Action.DOWN);
		String resp = c1.receive();

		assertEquals((new JSONObject(resp)).get("game").toString().replace('5', '0'), board.toString());
	}

	@Test
	public void testClientMoveIntoBoardLimitLeft() {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game_move_into_game_edge_left.json")));

		// connect
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		// load board
		c1.loadGame(board.toString());
		c1.setState(c1.receiveNoBroadcasts());

		//start
		c1.startGame();

		c1.receive();

		//move down
		c1.move(Action.LEFT);
		String resp = c1.receive();

		assertEquals((new JSONObject(resp)).get("game").toString().replace('5', '0'), board.toString());
	}

	@Test
	public void testClientMoveIntoBoardLimitUp() {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game_move_into_game_edge_up.json")));
		// connect
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		// load board
		c1.loadGame(board.toString());
		c1.setState(c1.receiveNoBroadcasts());

		//start
		c1.startGame();

		c1.receive();

		//move down
		c1.move(Action.UP);
		String resp = c1.receive();

		assertEquals((new JSONObject(resp)).get("game").toString().replace('5', '0'), board.toString());
	}

	@Test
	public void testClientMoveIntoBoardLimitRight() {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game_move_into_game_edge_right.json")));

		// connect
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		// load board
		c1.loadGame(board.toString());
		c1.setState(c1.receiveNoBroadcasts());

		//start
		c1.startGame();

		c1.receive();

		//move down
		c1.move(Action.RIGHT);
		String resp = c1.receive();

		assertEquals((new JSONObject(resp)).get("game").toString().replace('5', '0'), board.toString());
	}

	@Test
	public void testClientPickUpItem() {
		JSONObject boardBefore = new JSONObject(getFileContents(new File("gameboards/game_pick_up_item_before.json")));
		JSONObject boardAfter = new JSONObject(getFileContents(new File("gameboards/game_pick_up_item_after.json")));
		// connect
		c1.connect("player");
		c1.receiveNoBroadcasts();

		// load board
		JSONObject msg = new JSONObject();
		msg.put("command", "load");
		msg.put("game", boardBefore);
		c1.send(msg.toString());
		c1.receiveNoBroadcasts();

		//start
		msg = new JSONObject();
		msg.put("command", "button");
		msg.put("pid", 1);
		msg.put("button", "start");
		c1.send(msg.toString());

		c1.receive();

		//move up
		msg = new JSONObject();
		msg.put("command", "move");
		msg.put("pid", 1);
		msg.put("direction", "up");
		c1.send(msg.toString());
		String resp = c1.receive();

		assertEquals((new JSONObject(resp)).get("game").toString().replace('5', '0'), boardAfter.toString());
	}

	@Test
	public void testClientTwoPlayersColliding() {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game_two_players.json")));
		c1.connect("player");
		c1.receiveNoBroadcasts();
		c2.connect("player");
		c2.receiveNoBroadcasts();

		JSONObject msg = new JSONObject();
		msg.put("command", "load");
		msg.put("game", board);
		c1.send(msg.toString());
		c1.receiveNoBroadcasts();

		msg = new JSONObject();
		msg.put("command", "button");
		msg.put("pid", 1);
		msg.put("button", "start");
		c1.send(msg.toString());
		c1.receive();

		//move player 1
		msg = new JSONObject();
		msg.put("command", "move");
		msg.put("pid", 1);
		msg.put("direction", "down");
		c1.send(msg.toString());

		// move player 2
		msg = new JSONObject();
		msg.put("command", "move");
		msg.put("pid", 2);
		msg.put("direction", "up");

		c2.send(msg.toString());

		c1.flushMessages();
		String resp = c1.receive();

		// should be game over
		System.out.println("RESP: " + resp);
		JSONObject expectedResp = new JSONObject();
		expectedResp.put("type", "game_over");

		assertEquals(resp.trim(), expectedResp.toString());
	}

	@Ignore
	@Test
	public void testClientDeployBomb() {
		fail("Not implemented.");
	}

	@Ignore
	@Test
	public void testClientBombNotDestroyBlock() {
		fail("Not implemented.");
	}

	@Ignore
	@Test
	public void testClientBombKillEnemy() {
		fail("Not implemented.");
	}

	@Ignore
	@Test
	public void testClientBombKillPlayerGameOver() {
		fail("Not implemented.");
	}

	@Ignore
	@Test
	public void testClientGetPowerup() {
		fail("Not implemented.");
	}

	@Ignore
	@Test
	public void testClientRunIntoEnemy() {
		fail("Not implemented.");
	}

	@Ignore
	@Test
	public void testClientRevealDoor() {
		fail("Not implemented.");
	}
}
