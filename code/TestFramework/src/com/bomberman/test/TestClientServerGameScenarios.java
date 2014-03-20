package com.bomberman.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.bomberman.client.Action;
import com.bomberman.client.Client;
import com.bomberman.server.Server;

public class TestClientServerGameScenarios {
	private String SERVER_ADDR = "localhost";
	private int SERVER_PORT = 5000;
	@Rule public TestName testName = new TestName(); // Requires junit 4.7

	Client c1 = null;
	Client c2 = null;
	Client c3 = null;
	Client c4 = null;
	String logDir = "log/";
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
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game1.json")));
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		// load board
		c1.loadGame(board.toString());
		String resp = c1.receiveNoBroadcasts();

		JSONObject expResp = new JSONObject();
		expResp.put("type", "response");
		expResp.put("resp", "Success");
		assertEquals(resp.trim(), expResp.toString().trim());
	}

	@Test
	public void testClientLoadBoardStartGame() throws InterruptedException {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game1.json")));
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		// load board
		c1.loadGame(board.toString());
		c1.receiveNoBroadcasts();

		// receive board load command response
		// start game
		c1.startGame();
		String resp = c1.receive(); // receive new board state
		assertEquals(board.toString(), (new JSONObject(resp)).get("game").toString());
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
		c1.setState(c1.receiveNoBroadcasts());

		// load board
		c1.loadGame(boardBefore.toString());
		c1.setState(c1.receiveNoBroadcasts());

		//start
		c1.startGame();
		c1.receive();

		//move UP
		c1.move(Action.UP);
		String resp = c1.receive();

		assertEquals((new JSONObject(resp)).get("game").toString(), boardAfter.toString());
	}

	@Test
	public void testClientTwoPlayersColliding() {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game_two_players.json")));
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());
		c2.connect("player");
		c2.setState(c2.receiveNoBroadcasts());

		c1.loadGame(board.toString());
		c1.setState(c1.receiveNoBroadcasts());

		//start
		c1.startGame();
		c1.receive();

		//move down
		c1.move(Action.DOWN);
		c1.receive();

		//move p2 UP
		c2.move(Action.UP);
		c2.receive();

		c1.flushMessages();
		String resp = c1.receive();

		// should be game over
		JSONObject expectedResp = new JSONObject();
		expectedResp.put("type", "game_over");

		assertEquals(resp.trim(), expectedResp.toString());
	}

	@Test
	public void testClientDeployBomb() {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game_blow_up_enemy_before.json")));
		JSONObject boardAfter = new JSONObject(getFileContents(new File("gameboards/game_blow_up_enemy.json")));
		//connect
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		//load
		c1.loadGame(board.toString());
		c1.setState(c1.receiveNoBroadcasts());

		//start
		c1.startGame();
		c1.receive();

		c1.deployBomb();

		c1.move(Action.LEFT);
		c1.receive();

		c1.move(Action.LEFT);
		c1.receive();

		// wait for bomb to explode
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//flush messages
		c1.flushMessages();
		String resp = c1.receive();
		String gameResult = (new JSONObject(resp)).get("game").toString();
		assertEquals(boardAfter.toString(), gameResult);
	}

	@Ignore
	@Test
	public void testClientBombNotDestroyBlock() {
		fail("Not implemented.");
	}

	@Test
	public void testClientBombKillOtherPlayerGameOver() {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game_kill_p2_with_bomb_before.json")));
		JSONObject boardAfter = new JSONObject(getFileContents(new File("gameboards/game_kill_p2_with_bomb_after.json")));

		//connect p1
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		//connect p2
		c2.connect("player");
		c2.setState(c2.receiveNoBroadcasts());

		//load
		c1.loadGame(board.toString());
		c1.setState(c1.receiveNoBroadcasts());

		//start
		c1.startGame();
		c1.receive();

		c1.deployBomb();

		c1.move(Action.LEFT);
		c1.receive();

		c1.move(Action.LEFT);
		c1.receive();

		// wait for bomb to explode
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//flush messages
		c1.flushMessages();
		String resp = c1.receive();
		String gameResult = (new JSONObject(resp)).get("game").toString();
		assertEquals(boardAfter.toString(), gameResult);
	}

	@Test
	public void testClientPlayerTwoBombAvoid() {
		JSONObject board = new JSONObject(getFileContents(new File("gameboards/game_p2_avoid_bomb_before.json")));
		JSONObject boardAfter = new JSONObject(getFileContents(new File("gameboards/game_p2_avoid_bomb_after.json")));

		//connect p1
		c1.connect("player");
		c1.setState(c1.receiveNoBroadcasts());

		//connect p2
		c2.connect("player");
		c2.setState(c2.receiveNoBroadcasts());

		//load
		c1.loadGame(board.toString());
		c1.setState(c1.receiveNoBroadcasts());

		//start
		c1.startGame();
		c1.receive();

		c1.deployBomb();

		c1.move(Action.LEFT);
		c1.receive();

		c1.move(Action.LEFT);
		c1.receive();

		c2.move(Action.DOWN);
		c2.receive();

		// wait for bomb to explode
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//flush messages
		c1.flushMessages();
		String resp = c1.receive();
		String gameResult = (new JSONObject(resp)).get("game").toString();
		assertEquals(boardAfter.toString(), gameResult);
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
