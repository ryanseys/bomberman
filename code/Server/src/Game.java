import java.util.ArrayList;

import org.json.*;


public class Game {
	// Currently we need to limit number of players to 2
	public static int MAX_PLAYERS = 2;
	public static int MAX_POWERUPS = 1;
	public static int NUM_ENEMIES = 0;
	public static int DEFAULT_DOORS = 5;
	private Player[] players;
	private ArrayList<Enemy> enemies;
	private ArrayList<Powerup> powerups;
	
	private Board board;
	private int numPlayers;
	private boolean isStarted;
	private boolean isFinished;

	public Game() {
		this.isStarted = false;
		this.isFinished = false;
		this.numPlayers = 0;
		this.powerups = new ArrayList<Powerup>();
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
		else if(this.isStarted){
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
		this.enemies = new ArrayList<Enemy>();
		this.isStarted = true;
		if(board == null)
			this.board = new Board(DEFAULT_DOORS); // TODO decide on size of board and # boxes
		board.initBoard(players, enemies, powerups);
	}

	// Moves the specified player in the specified direction
	public void playerMoved(int playerID, String direction){
		System.out.println("player: " + playerID + " moved " + direction);
		Player player = getPlayer(playerID);
		player.getBombRange();
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
		checkPowerups();
		checkDoors();
		
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
		int numPowerups = 0;
		int boxes = 0;
		GameObjectType type;
		JSONArray boardArray = board.getJSONArray("board");
		int [][] intArr = new int[width][height];
		
		for (int i = 0; i < boardArray.length(); i++) {
			JSONArray currArr = boardArray.getJSONArray(i);
			for(int j = 0; j < currArr.length(); j++){
				intArr[i][j] = currArr.getInt(j);
				type = GameObjectType.values()[intArr[i][j]];
				if(type != GameObjectType.EMPTY){
					switch(type){
					case BOX:
						boxes++;
						break;
					case ENEMY:
						break;
					case PLAYER_1:
						break;
					case PLAYER_2:
						break;
					case PLAYER_3:
						break;
					case PLAYER_4:
						break;
					case POWERUP:
						if(numPowerups < MAX_POWERUPS){
							powerups.add(new Powerup(i, j));
						}
						break;
					default:
						break;
					}
				}			
				System.out.print(intArr[i][j]);
			}
			System.out.println();
		}
		this.board = new Board(width, height, boxes);
		this.board.fromIntArr(intArr, width, height);
	}
	private synchronized void checkPowerups(){
		if(powerups.size() == 0)
			return;
		for (Player player : players) {
			for (Powerup powerup : powerups){
				if(player.getLocation().equals(powerup.getLocation())){
					powerups.remove(powerup);
					player.powerup();
					return;
				}
			}
		}
	}
	private synchronized void checkDoors(){
		GameObject door = board.getDoor();
		for (Player player : players) {
			if(player.getLocation().equals(door.getLocation())){
				if(enemies.size() == 0){
					// No enemies left
					// TODO - Handle end of game scenario...
					endGame();
					return;
				}
			}
		}
		
	}
	/**
	 * @return the isStarted
	 */
	public boolean isStarted() {
		return isStarted;
	}
}
