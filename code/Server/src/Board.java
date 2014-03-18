import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

// Board object holds all of the things that defines the game's board
public class Board {
	private static final int MAX_WIDTH = 10;  // Default width
	private static final int MAX_HEIGHT = 10; // Default height
	private GameObject[][] board;             // Array to hold things in the game
	private GameObject door;
	private int width;                        // Current game's width
	private int height;                       // Current game's height
	private int numBoxes;                     // Number of boxes in this game
	private ArrayList<GameObject> boxes;      // Array list of boxes in this game

	public Board(int numBoxes){
		this.width = MAX_WIDTH;
		this.height = MAX_HEIGHT;
		this.numBoxes = numBoxes;
		this.boxes = new ArrayList<GameObject>();
		this.initBoard();
	}

	public Board(int height, int width, int numBoxes){
		this.width = width;
		this.height = height;
		this.numBoxes = numBoxes;
		this.boxes = new ArrayList<GameObject>();
		this.initBoard();
	}

	/**
	 * @return the board
	 */
	public GameObject[][] getBoard() {
		return board;
	}

	/**
	 * @param set the board to configured width/height
	 */
	private void initBoard() {
		this.board = new GameObject[this.width][this.height];
	}

	// Initialize the board objects
	// pass in reference to list of players, enemies, and powerups
	public void initBoard(ArrayList<Player> players, ArrayList<Enemy> enemies, ArrayList<Powerup> powerups){
		initBoxes();
		initPlayers(players);
		initEnemies(enemies);
		initPowerups(powerups);
		placeDoor();
	}
	// Place the door randomly if it isnt instantiated already
	private void placeDoor(){
		if(door == null){
			Point emptySpot = getEmptySpot();
			door = new GameObject(GameObjectType.DOOR, emptySpot.getLocation().x, emptySpot.getLocation().y);
		}
//		door.setVisible(false); // TODO: Uncomment to hide door by default
		board[door.x()][door.y()] = door;
	}
	// Place the players randomly if they aren't instantiated already
	private void initPlayers(ArrayList<Player> players){
		Point emptySpot;
		for(Player player : players){
			if((player.x() == -1) || (player.y() == -1)){
				emptySpot = getEmptySpot();
				player.move(emptySpot.getLocation().x, emptySpot.getLocation().y);
			}
			board[player.x()][player.y()] = player;
		}
	}
	// Place the enemies randomly if they aren't instantiated already
	private void initEnemies(ArrayList<Enemy> enemies){
		Point emptySpot;
		Enemy enemy;
		while(enemies.size() < Game.NUM_ENEMIES){
			emptySpot = getEmptySpot();
			enemy = new Enemy(emptySpot.getLocation().x, emptySpot.getLocation().y);
			enemies.add(enemy);
		}
		for (Enemy e : enemies) {
			board[e.x()][e.y()] = e;
		}
	}
	// Place the powerups randomly if they aren't instantiated already
	private void initPowerups(ArrayList<Powerup> powerups){
		Point emptySpot;
		Powerup powerup;
		while(powerups.size() < Game.MAX_POWERUPS){
			emptySpot = getEmptySpot();
			powerup = new Powerup(emptySpot.getLocation().x, emptySpot.getLocation().y);
			powerups.add(powerup);
			board[powerup.x()][powerup.y()] = powerup;
		}
		for (Powerup p : powerups) {
			board[p.x()][p.y()] = p;
		}
	}
	//place a bomb behind the user
	public synchronized boolean placeBomb(int x, int y)
	{
		GameObject bombObj;
		if(isEmptySpot(x,y))
		{
			bombObj= new GameObject(GameObjectType.BOMB, x, y);
			board[x][y] = bombObj;
			return true;
		}else{
			return false;
		}

	}
	// will kill players in the range of fire
	public void fire(int heightOne,int heightTwo,int widthOne, int widthTwo,int x,int y)
	{
		board[x][y] = new GameObject(GameObjectType.FIRE, x, y);
		for(int i=0; i<=heightOne;i++)
		{
			if(board[x][y-i] instanceof Player)
			{
				((Player)board[x][y-i]).dies();	
				board[x][y-i]=new GameObject(GameObjectType.FIRE, x, y-i);
			}else if(board[x][y-i] instanceof Enemy){
				((Enemy)board[x][y-i]).dies();
				board[x][y-i]=new GameObject(GameObjectType.FIRE, x, y-i);

			}else if(isEmptySpot(x,y-i)){
				board[x][y-i]=new GameObject(GameObjectType.FIRE, x, y-i);

			}
		}
		for(int i=0; i<=heightTwo;i++)
		{
			if(board[x][y+i] instanceof Player)
			{
				((Player)board[x][y+i]).dies();	
				board[x][y+i]=new GameObject(GameObjectType.FIRE, x, y+i);
			}else if(board[x][y+i] instanceof Enemy){
				((Enemy)board[x][y+i]).dies();
				board[x][y+i]=new GameObject(GameObjectType.FIRE, x, y+i);
			}else if(isEmptySpot(x,y+i)){
				board[x][y+i]=new GameObject(GameObjectType.FIRE, x, y+i);
			}
		}
		for(int i=0; i<=widthOne;i++)
		{
			if(board[x-i][y] instanceof Player)
			{
				((Player)board[x-i][y]).dies();
				board[x-i][y]=new GameObject(GameObjectType.FIRE, x-i, y);
			}else if( board[x-i][y] instanceof Enemy){
				((Enemy)board[x-i][y]).dies();
				board[x-i][y]=new GameObject(GameObjectType.FIRE, x-i, y);
			}else if(isEmptySpot(x-i,y)){
				board[x-i][y]=new GameObject(GameObjectType.FIRE, x-i, y);
			}
		}
		for(int i=0; i<=widthTwo;i++)
		{
			if(board[x+i][y] instanceof Player)
			{
				((Player)board[x+i][y]).dies();
				board[x+i][y]=new GameObject(GameObjectType.FIRE, x+i, y);
			}else if(board[x+i][y] instanceof Enemy){
				((Enemy)board[x+i][y]).dies();
				board[x+i][y]=new GameObject(GameObjectType.FIRE, x+i, y);
			}else if(isEmptySpot(x+i,y)){
				board[x+i][y]=new GameObject(GameObjectType.FIRE, x+i, y);
			}
		}
		System.out.print(heightTwo);
	}
	
	//clears the fire from the map
	public synchronized void clearFire()
	{
		for(int i=0;i<MAX_WIDTH; i++)
		{
			for(int j=0;j<MAX_HEIGHT;j++)
			{
				if(board[i][j]!=null)
				{
					if (board[i][j].getType()==GameObjectType.FIRE)
					{
						board[i][j]= null;
					}
				}
			}
		}
	}
	// Place the boxes randomly if they aren't instantiated already
	private void initBoxes(){
		Point emptySpot;
		GameObject newBox;
		while(boxes.size() < numBoxes){
			emptySpot = getEmptySpot();
			newBox = new GameObject(GameObjectType.BOX, emptySpot.getLocation().x, emptySpot.getLocation().y);
			boxes.add(newBox);
			board[emptySpot.getLocation().x][emptySpot.getLocation().y] = newBox;
		}
		for (GameObject box : boxes) {
			board[box.x()][box.y()] = box;
		}
	}
	//checks to see if a given location is empty
	private boolean isEmptySpot(int x,int y)
	{
		if(x>=MAX_WIDTH || y>=MAX_HEIGHT ||x<0 || y<0)
		{
			return false;
		}else if(board[x][y]==null)
		{
			return true;
		}
		return false;
	}
	private boolean isWithinBorder(int x, int y)
	{
		if(x>=MAX_WIDTH || y>=MAX_HEIGHT ||x<0 || y<0)
		{
			return false;
		}else{
			return true;
		}
	}
	// Find a random empty spot on the board
	private Point getEmptySpot(){
		boolean emptySpot = true;
		int x, y;
		Random r = new Random();
		do{
			x = r.nextInt(this.width);
			y = r.nextInt(this.height);
			if(board[x][y] == null){
				emptySpot = true;
			}else{emptySpot = false;}
		}while(!emptySpot);
		return new Point(x,y);
	}
	// Handle a game object moving up
	public void moveUp(MovingObject obj) {
		int x = obj.x();
		int y = obj.y();
		if(onBoard(x, y - 1)){
			if(obj.getType().ordinal() <= GameObjectType.PLAYER_4.ordinal()){
				playerMove((Player) obj, x, y - 1);
			}
			else{
				enemyMove((Enemy) obj, x, y - 1);
			}
		}
	}
	// Handle a game object moving down
	public void moveDown(MovingObject obj) {
		int x = obj.x();
		int y = obj.y();
		if(onBoard(x, y + 1)){
			if(obj.getType().ordinal() <= GameObjectType.PLAYER_4.ordinal()){
				playerMove((Player) obj, x, y + 1);
			}
			else{
				enemyMove((Enemy) obj, x, y + 1);
			}
		}
	}
	// Handle a game object moving left
	public void moveLeft(MovingObject obj) {
		int x = obj.x();
		int y = obj.y();
		if(onBoard(x - 1, y)){
			if(obj.getType().ordinal() <= GameObjectType.PLAYER_4.ordinal()){
				playerMove((Player) obj, x - 1, y);
			}
			else{
				enemyMove((Enemy) obj, x - 1, y);
			}
		}
	}
	// Handle a game object moving right
	public void moveRight(MovingObject obj) {
		int x = obj.x();
		int y = obj.y();
		if(onBoard(x + 1, y)){
			if(obj.getType().ordinal() <= GameObjectType.PLAYER_4.ordinal()){
				playerMove((Player) obj, x + 1, y);
			}
			else{
				enemyMove((Enemy) obj, x + 1, y);
			}
		}
	}
	// Handle enemy movement
	private void enemyMove(Enemy enemy, int newX, int newY){
		//TODO - For next milestone do this
	}
	// Handle player movement checks if spot is taken and by what.
	private void playerMove(Player player, int newX, int newY){
		if(board[newX][newY] == null){
			board[player.x()][player.y()] = null;
			player.move(newX, newY);
			board[newX][newY] = player;
		}
		else{
			if(board[newX][newY].getType().ordinal() < GameObjectType.PLAYER_4.ordinal()){
				((Player) board[newX][newY]).dies();
				player.dies();
			}
			switch(board[newX][newY].getType()){
			case DOOR:
				board[player.x()][player.y()] = null;
				player.move(newX, newY);
				board[newX][newY] = player;
				// Handle player on door in game.
				break;
			case EMPTY:
//				// Can go there... Shouldnt hit this case ever...
//				board[player.x()][player.y()] = null;
//				player.move(newX, newY);
//				board[newX][newY] = player;
				System.out.println("Empty Game Object... Check Board.java");
				break;
			case POWERUP:
				board[player.x()][player.y()] = null;
				player.move(newX, newY);
				board[newX][newY] = player;
				// Handle player on powerup in game.
				break;
			case FIRE:
				//if the player steps into the fire
				player.dies();
			case BOMB:
				//board[player.x()][player.y()] = null;
				//player.move(newX, newY);
				//board[newX][newY] = player;
			default:
				// Don't go there...
				break;
			}
		}
	}

	// Returns whether the new location is on the board
	private boolean onBoard(int x, int y){
		boolean xValid = (x >= 0) && (x < this.width);
		boolean yValid = (y >= 0) && (y < this.height);
		return xValid && yValid;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	// Converts the state of the board to a 2D integer array in order to be passed to the client
	public int[][] toIntArr(){
		System.out.println("To int arr:");
		int[][] intArr = new int[this.width][this.height];
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				if(this.board[x][y] != null){
					if(this.board[x][y].isVisible()){
						intArr[x][y] = this.board[x][y].getType().ordinal();
					}
				}
				else{
					intArr[x][y] = GameObjectType.EMPTY.ordinal();
				}
				System.out.print(intArr[x][y]);
			}
			System.out.println();
		}
		return intArr;
	}
	// Converts a 2D integer array to a board state so that a game board state can be loaded in
	public void fromIntArr(int[][] intArr, int width, int height){
		GameObjectType type;
		this.board = new GameObject[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if(intArr[i][j] != GameObjectType.EMPTY.ordinal()){
					type = GameObjectType.values()[intArr[i][j]];
					switch(type){
					case DOOR:
						this.door = new GameObject(GameObjectType.DOOR, i, j);
						break;
					case BOX:
						GameObject box = new GameObject(GameObjectType.BOX, i, j);
						this.boxes.add(box);
						break;
					case PLAYER_1:
					case PLAYER_2:
					case PLAYER_3:
					case PLAYER_4:
						// this will place the player at its loaded location
						// (but one will be randomly generated in startGame anyway) so this
						// just breaks stuff for now
						//playerMove(new Player(type.ordinal()-1, i, j), i, j);
						break;
					default:
						break;
					}
				}
				//System.out.print(""+ intArr[i][j]);
			}
			//System.out.print("\n");
		}
	}

	/**
	 * @return the door
	 */
	public GameObject getDoor() {
		return this.door;
	}
}
