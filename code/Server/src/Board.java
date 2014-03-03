import java.awt.Point;
import java.util.*;


public class Board {
	private static final int MAX_WIDTH = 10;
	private static final int MAX_HEIGHT = 10;
	private GameObject[][] board;
	private Door door;
	private int width;
	private int height;
	private int numBoxes;
	private ArrayList<GameObject> boxes;

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

	// TODO initialize the board objects
	// pass in # of players, # of enemies, # of powerups
	public void initBoard(Player[] players, ArrayList<Enemy> enemies, ArrayList<Powerup> powerups){
		initBoxes();
		initPlayers(players);
		initEnemies(enemies);
		initPowerups(powerups);
		placeDoor();
	}
	
	private void placeDoor(){
		if(door == null){
			Point emptySpot = getEmptySpot();
			door = new Door(emptySpot.getLocation().x, emptySpot.getLocation().y);
		}
		board[door.x()][door.y()] = door;
	}
	
	private void initPlayers(Player[] players){
		Point emptySpot;
		for (int i=0;i < players.length; i++) {
			emptySpot = getEmptySpot();
			players[i] = new Player(i, emptySpot.getLocation().x, emptySpot.getLocation().y);
			board[emptySpot.getLocation().x][emptySpot.getLocation().y] = players[i];
		}
	}
	
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
	
	private void enemyMove(Enemy enemy, int newX, int newY){
		//TODO - For next milestone do this
	}
	
	private void playerMove(Player player, int newX, int newY){
		if(board[newX][newY] == null){
			board[player.x()][player.y()] = null;
			player.move(newX, newY);
			board[newX][newY] = player;	
		}
		else{
			switch(board[newX][newY].getType()){
			case DOOR:
				board[player.x()][player.y()] = null;
				player.move(newX, newY);
				board[newX][newY] = player;
				// Handle player on door in game.
				break;
			case EMPTY:
				// Can go there... Shouldnt hit this case ever...
				board[player.x()][player.y()] = null;
				player.move(newX, newY);
				board[newX][newY] = player;	
				break;
			case POWERUP:
				board[player.x()][player.y()] = null;
				player.move(newX, newY);
				board[newX][newY] = player;
				// Handle player on powerup in game.
				break;
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
	
	public int[][] toIntArr(){
		int[][] intArr = new int[this.width][this.height];
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				if(this.board[x][y] != null){
					if(this.board[x][y].getType() == GameObjectType.DOOR){
						if(this.door.isVisible())
							intArr[x][y] = this.board[x][y].getType().ordinal();
					}
					else{
						intArr[x][y] = this.board[x][y].getType().ordinal();
					}
				}
				else{
					intArr[x][y] = GameObjectType.EMPTY.ordinal();
				}
			}
		}
		return intArr;
	}
	
	public void fromIntArr(int[][] intArr, int width, int height){
		GameObjectType type;
		this.board = new GameObject[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if(intArr[i][j] != GameObjectType.EMPTY.ordinal()){
					type = GameObjectType.values()[intArr[i][j]];
					switch(type){
					case DOOR:
						this.door = new Door(i, j);
						break;
					case BOX:
						GameObject box = new GameObject(GameObjectType.BOX, i, j);
						this.boxes.add(box);
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	/**
	 * @return the door
	 */
	public Door getDoor() {
		return door;
	}
}
