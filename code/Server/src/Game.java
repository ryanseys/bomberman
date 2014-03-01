
public class Game {
	// Currently we need to limit number of players to 2
	private static int MAX_PLAYERS = 2;

	private Player[] players;
	private Enemy[] enemies;
	private Powerup[] powerups;
	private Board board;
	private int numPlayers;
	private boolean started;
	private boolean isFinished;

	public Game() {
		this.started = false;
		this.isFinished = false;
		this.numPlayers = 0;
	}

	/*
	 *  Try to add a player to the game.
	 *  return true on success.
	 *  return false on failure.
	 */
	public synchronized boolean addPlayer(){
		if((this.numPlayers == MAX_PLAYERS) || (this.started)){
			return false;
		}
		else {
			numPlayers++;
			return true;
		}
	}

	// Initialize the board, placing the players at random locations.
	// All spots are randomly filled as either empty or holding a box(and/or the door)
	// Add one power up... (May change)
	public void startGame(){
		this.players = new Player[numPlayers]; // Create array to store players in
		this.enemies = new Enemy[0];
		this.powerups = new Powerup[0];
		
		this.started = true;
		this.board = new Board(); // TODO decide on size of board
		
		board.initBoard(players, enemies, powerups);
	}

	// Moves the specified player in the specified direction
	public void playerMoved(int playerID, String direction){
		
	}

	/**
	 * @return the isFinished
	 */
	public synchronized boolean isFinished() {
		return isFinished;
	}

	public void endGame() {
		this.isFinished = true;
		
	}

	public void resetPlayer(int playerID) {
		// TODO Auto-generated method stub
		
	}

	public void dropBomb(int playerID) {
		// TODO Auto-generated method stub
		
	}
}
