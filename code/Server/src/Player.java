import java.util.ArrayList;



// Object to simulate the players
public class Player extends MovingObject {
	private int maxBombs;     // Max number of bombs a player can hold
	private int currentBombs; // Number of bombs that a current player has
	private int bombRange;    // The range of the bombs that the player drops
	private int lives;        // Number of lives a player has
	private ArrayList<Powerup> powerups;

	public Player(int player_number, int x, int y) {
		super(playerNumToGameObj(player_number), x, y);
		this.maxBombs = 10;
		this.currentBombs = 1;
		this.lives = 1;
		this.bombRange = 1;
		powerups = new ArrayList<Powerup>();
	}
	
	public void addPowerup(Powerup p){
		powerups.add(p);
	}
	
	public int getPowerups(){
		return powerups.size();
	}

	/**
	 * @return the maxBombs
	 */
	public int getMaxBombs() {
		return maxBombs;
	}

	/**
	 * @param maxBombs
	 *            the maxBombs to set
	 */
	public void setMaxBombs(int maxBombs) {
		this.maxBombs = maxBombs;
	}

	/**
	 * @return the currentBombs
	 */
	public int getCurrentBombs() {
		return currentBombs;
	}

	/**
	 * @param currentBombs
	 *            the currentBombs to set
	 */
	public void setCurrentBombs(int currentBombs) {
		this.currentBombs = currentBombs;
	}

	/**
	 * @return the lives
	 */
	public int getLives() {
		return lives;
	}

	/**
	 * @param lives
	 *            the lives to set
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * @return the bombRange
	 */
	public int getBombRange() {
		return bombRange;
	}

	/**
	 * @param bombRange
	 *            the bombRange to set
	 */
	public void setBombRange(int bombRange) {
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
	public void powerup() {
		if((currentBombs + 1) > maxBombs)
		{
			currentBombs = maxBombs;
		}else{
			currentBombs++;
		}
		this.bombRange++;
	}


}
