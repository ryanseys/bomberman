import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class Game {
	// Currently we need to limit number of players to 2
	public static int MAX_PLAYERS = 2;
	public static int MAX_POWERUPS = 1;
	public static int NUM_ENEMIES = 0;
	public static int DEFAULT_DOORS = 5;
	private ArrayList<Player> players;
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
		this.players = new ArrayList<Player>();
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
			this.numPlayers++;
			System.out.println("Player succesfully joined the game");
			return true;
		}
	}

	// Initialize the board, placing the players at random locations.
	// All spots are randomly filled as either empty or holding a box(and/or the door)
	// Add one power up... (May change)
	// TODO: There is a bug here. You cannot place players on the board if they have been pre-placed with load game.
	// TODO: There should be a way to specify players initial locations manually rather than randomly
	public void startGame(){
		this.enemies = new ArrayList<Enemy>();
		this.isStarted = true;
		if(board == null)
		{
			this.board = new Board(DEFAULT_DOORS); // TODO decide on size of board and # boxes
		}
		System.out.println("NUM PLAYERS: "+numPlayers + " players.size: " + this.players.size());
		while(this.players.size() < this.numPlayers){
			this.players.add((new Player(this.players.size()+1, -1,-1)));
		}
		while(this.numPlayers > this.players.size()){
			this.players.remove(this.players.size() -1);
		}
		
		board.initBoard(players, enemies, powerups);
		checkDoors();
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
		for(Player player : players){
			if(player.getType() == GameObjectType.values()[clientID]){
				return player;
			}
		}
		System.out.println("There is an issue in Game.java > getPlayer()");
		return null;
	}

	public void loadBoard(JSONObject board) {
		System.out.println("Loading board from JSON:");
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
						this.players.add(new Player(1,i,j));
						break;
					case PLAYER_2:
						if(MAX_PLAYERS > 1){
							this.players.add(new Player(2,i,j));
						}
						break;
					case PLAYER_3:
						if(MAX_PLAYERS > 2){
							this.players.add(new Player(3,i,j));
						}
						break;
					case PLAYER_4:
						if(MAX_PLAYERS > 3){
							this.players.add(new Player(4,i,j));
						}
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
		if(powerups.size() == 0) {
			return;
		}
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
		Door door = board.getDoor();
		if(enemies.size() == 0) {
			door.setVisible();
		}
		for (Player player : players) {
			if(player.getLocation().equals(door.getLocation())){
				// TODO - Handle end of game scenario...
				if(enemies.size() > 0) {
					door.setVisible();
				} else{
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

	/**
	 * @return the numPlayers
	 */
	public int getNumPlayers() {
		return numPlayers;
	}

	/**
	 * @param numPlayers the numPlayers to set
	 */
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
}
