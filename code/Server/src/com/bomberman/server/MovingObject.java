package com.bomberman.server;
// Class for game objects that can move such as the Enemy or the Player
public abstract class MovingObject extends GameObject {

	private boolean isAlive;  // If the player is alive
	private int lives;        // Number of lives a player has
	
	public MovingObject(GameObjectType type, int x, int y) {
		super(type, x, y);
		setAlive(true);
		this.lives = 1;
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
}
