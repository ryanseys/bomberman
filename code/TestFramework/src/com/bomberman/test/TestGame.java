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

import com.bomberman.server.Game;

public class TestGame {
	@Rule public TestName testName = new TestName(); // Requires junit 4.7
	String logDir = "log/";
	Game game;

	@Before
	public void setUp() throws Exception {
		 game = new Game();
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
		game = null;
	}

	@Test
	public void testGameStarted() {
		game.startGame();
		assertTrue(game.isStarted());
	}

	@Test
	public void testAddOnePlayer() {
		game.addPlayer();
		game.startGame();
		assertTrue(game.getNumPlayers() == 1);
	}

	@Test
	public void testAddOnePlayerArraySize() {
		game.addPlayer();
		game.startGame();
		assertTrue(game.getPlayers().size() == 1);
	}

	@Test
	public void testAddTwoPlayers() {
		game.addPlayer();
		game.addPlayer();
		game.startGame();
		assertTrue(game.getNumPlayers() == 2);
	}

	@Test
	public void testAddTwoPlayersArraySize() {
		game.addPlayer();
		game.addPlayer();
		game.startGame();
		assertTrue(game.getPlayers().size() == 2);
	}

	@Test
	public void testGetBufferStateTwice() {
		game.addPlayer();
		game.startGame();
		assertTrue(game.getBuffer().getState() == game.getBuffer().getState());
	}
}
