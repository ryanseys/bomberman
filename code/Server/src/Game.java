
public class Game {
	// Currently we need to limit number of players to 2
	private static int MAX_PLAYERS = 2;

	private Client[] players;
	private Board board;
	private int numPlayers;
	private boolean started;
	private boolean isFinished;

	public Game() {
		this.started = false;
		this.isFinished = false;
		this.numPlayers = 0;
		this.players = new Client[MAX_PLAYERS];
	}

	/*
	 *  Try to add a player to the game.
	 *  return player id on success.
	 *  return -1 on failure.
	 */
	public synchronized boolean addPlayer(Client c){
		if((this.numPlayers == MAX_PLAYERS) || (this.started)){
			return false;
		}
		else {
			players[numPlayers++] = c;
			return true;
		}
	}

	// Initialize the board, placing the players at random locations.
	// All spots are randomly filled as either empty or holding a box(and/or the door)
	// Add one power up... (May change)
	public void startGame(){
		this.started = true;
		board = new Board(); // TODO decide on size of board
		board.initBoard(numPlayers, 0, 1);
	}

	// Moves the specified player in the specified direction
	public void playerMoved(int playerID, String direction){

	}

	/**
	 * @return the isFinished
	 */
	public boolean isFinished() {
		return isFinished;
	}

}
