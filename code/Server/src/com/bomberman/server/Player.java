package com.bomberman.server;
import java.util.ArrayList;
import java.util.Collection;



// Object to simulate the players
public class Player extends MovingObject {
	private int currentBombs; // Number of bombs that a current player has
	private int maxBombs; // Number of bombs that a current player has
	private int bombRange;    // The range of the bombs that the player drops
	private int lives;        // Number of lives a player has
	private int numPowerups = 0;

	public Player(int player_number, int x, int y) {
		super(playerNumToGameObj(player_number), x, y);
		this.currentBombs = 1;
		this.maxBombs = 10;
		this.lives = 1;
		this.bombRange = 1;
	}
	
	public synchronized int numPowerups(){
		return numPowerups;
	}

	/**
	 * @return the currentBombs
	 */

	public synchronized void dropBomb(){
		this.currentBombs--;
	}
	public boolean hasBombs(){
		return this.currentBombs > 0;
	}

	/**
	 * @return the lives
	 */
	public synchronized int getLives() {
		return lives;
	}

	/**
	 * @param lives
	 *            the lives to set
	 */
	public synchronized void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * @return the bombRange
	 */
	public synchronized int getBombRange() {
		return bombRange;
	}

	/**
	 * @param bombRange
	 *            the bombRange to set
	 */
	public synchronized void setBombRange(int bombRange) {
		this.bombRange = bombRange;
	}

	private static GameObjectType playerNumToGameObj(int playerNum) {
		GameObjectType gameObj = null;
		switch (playerNum) {
		case 1:
			gameObj = GameObjectType.PLAYER_1;
			break;
		case 2:
			gameObj = GameObjectType.PLAYER_2;
			break;
		case 3:
			gameObj = GameObjectType.PLAYER_3;
			break;
		case 4:
			gameObj = GameObjectType.PLAYER_4;
			break;
		}
		return gameObj;
	}
	
	// If a player lands on a powerup
	public synchronized void powerup() {
		this.incrementBombs();
		this.numPowerups++;
		this.bombRange++;
	}
	public void incrementBombs(){
		if(currentBombs < maxBombs){
			currentBombs++;
		}
	}

	public int getCurrentBombs() {
		return this.currentBombs;
	}


}
