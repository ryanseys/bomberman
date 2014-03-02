import org.json.*;


public class Game {
	// Currently we need to limit number of players to 2
	public static int MAX_PLAYERS = 2;

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
		if((this.numPlayers == MAX_PLAYERS)){
			System.out.println("Player cannot join, the game is full");
			return false;
		}
		else if(this.started){
			System.out.println("Player cannot join, the game has started");
			return false;
		}
		else {
			numPlayers++;
			System.out.println("Player succesfully joined the game");
			return true;
		}
	}

	// Initialize the board, placing the players at random locations.
	// All spots are randomly filled as either empty or holding a box(and/or the door)
	// Add one power up... (May change)
	public void startGame(){
		this.players = new Player[numPlayers]; // Create array to store players in
		this.enemies = new Enemy[0];
		this.powerups = new Powerup[1];
		this.started = true;
		if(board == null)
			this.board = new Board(5); // TODO decide on size of board and # boxes
		board.initBoard(players, enemies, powerups);
	}

	// Moves the specified player in the specified direction
	public void playerMoved(int playerID, String direction){
		Player player = getPlayer(playerID);
		if(direction.equals("up")){
			board.moveUp(player);
		}
		else if(direction.equals("down")){
			board.moveDown(player);
		}
		else if(direction.equals("left")){
			board.moveLeft(player);
		}
		else if(direction.equals("right")){
			board.moveRight(player);
		}
		else{
			System.out.println("Invalid direction submitted by client!");
		}
		
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
	public JSONObject toJSON(){
		JSONObject game = new JSONObject();
		game.put("width", this.board.getWidth());
		game.put("height", this.board.getHeight());
		int[][] intBoard = board.toIntArr();
		JSONArray arr = new JSONArray();
		for(int i = 0; i < intBoard.length; i++){
			arr.put(i, new JSONArray(intBoard[i]));
		}
		game.put("board", arr);
		return game;
		
	}
	private Player getPlayer(int clientID){
		return players[clientID-1];
	}

	public void loadBoard(JSONObject board) {
		int width = board.getInt("width");
		int height = board.getInt("height");
		JSONArray boardArray = board.getJSONArray("board");
		int [][] intBoard = new int[width][height];
		
		for (int i = 0; i < boardArray.length(); i++) {
			JSONArray currArr = boardArray.getJSONArray(i);
			for(int j = 0; j < currArr.length(); j++){
				intBoard[i][j] = currArr.getInt(j);
				System.out.println(intBoard[i][j]);
			}
		}
		System.out.println(intBoard);
	}
	
}
