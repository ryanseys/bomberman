package com.bomberman.server;
// Class for game objects that can move such as the Enemy or the Player
public abstract class MovingObject extends GameObject {

	private boolean isAlive;  // If the player is alive
	private int lives;        // Number of lives a player has
	private int currentBombs; // Number of bombs that a current player has
	private int maxBombs; // Number of bombs that a current player has
	private int bombRange;    // The range of the bombs that the player drops
	private int numPowerups = 0;
	
	public MovingObject(GameObjectType type, int x, int y) {
		super(type, x, y);
		setAlive(true);
		this.lives = 1;
		this.currentBombs = 1;
		this.maxBombs = 10;
		this.bombRange = 1;
	}

	// Move to a new position
	public void move(int x, int y){
		this.location.setLocation(x, y);
	}

	public void dies() {
		this.lives--;
		if(lives == 0){
			this.setVisible(false);
			this.setAlive(false);
			this.move(-1, -1);
		}
		else{
			System.out.println("HOW YOU STILL ALIVE!!!");
		}
	}

	/**
	 * @return the isAlive
	 */
	public boolean isAlive() {
		return isAlive;
	}

	/**
	 * @param isAlive the isAlive to set
	 */
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	/**
	 * @return the lives
	 */
	public synchronized int getLives() {
		return lives;
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
