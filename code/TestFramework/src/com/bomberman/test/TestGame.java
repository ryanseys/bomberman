package com.bomberman.test;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bomberman.server.Game;

public class TestGame {

	Game game;

	@Before
	public void setUp() throws Exception {
		 game = new Game();
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
