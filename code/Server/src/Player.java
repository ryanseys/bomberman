
public class Player extends MovingObject {
	private int maxBombs;
	private int currentBombs;
	private int bombRange;
	private int lives;

	public Player(int player_number, int x, int y) {
		super(playerNumToGameObj(player_number), x, y);
		this.maxBombs = 1;
		this.currentBombs = 1;
		this.lives = 1;
		this.bombRange = 1;
	}

	/**
	 * @return the maxBombs
	 */
	public int getMaxBombs() {
		return maxBombs;
	}

	/**
	 * @param maxBombs the maxBombs to set
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
	 * @param currentBombs the currentBombs to set
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
	 * @param lives the lives to set
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
	 * @param bombRange the bombRange to set
	 */
	public void setBombRange(int bombRange) {
		this.bombRange = bombRange;
	}
	
	private static GameObjectType playerNumToGameObj(int playerNum){
		GameObjectType gameObj = null;
		switch(playerNum){
		case 0:
			gameObj = GameObjectType.PLAYER_1;
			break;
		case 1:
			gameObj = GameObjectType.PLAYER_2;
			break;
		case 2:
			gameObj = GameObjectType.PLAYER_3;
			break;
		case 3:
			gameObj = GameObjectType.PLAYER_4;
			break;
		}
		return gameObj;
	}
	public void powerup(){
		this.maxBombs++;
		this.bombRange++;
	}

}
