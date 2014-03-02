import java.awt.Point;
import java.util.Random;


public class Board {
	private static final int MAX_WIDTH = 25;
	private static final int MAX_HEIGHT = 25;
	private GameObject[][] board;
	private GameObject door;
	private int width;
	private int height;

	public Board(){
		this.width = MAX_WIDTH;
		this.height = MAX_HEIGHT;
		this.initBoard();
		
	}

	public Board(int height, int width){
		this.width = width;
		this.height = height;
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
	public void initBoard(Player[] players, Enemy[] enemies, Powerup[] powerups){
		initPlayers(players);
		initEnemies(enemies);
		initPowerups(powerups);
		//placeBlocks();
		placeDoor();
	}
	private void placeDoor(){
		Point emptySpot = getEmptySpot();
		door = new GameObject(GameObjectType.DOOR, emptySpot.getLocation().x, emptySpot.getLocation().y);
		board[emptySpot.getLocation().x][emptySpot.getLocation().y] = door;
	}
	private void initPlayers(Player[] players){
		Point emptySpot;
		for (int i=0;i < players.length; i++) {
			emptySpot = getEmptySpot();
			players[i] = new Player(i, emptySpot.getLocation().x, emptySpot.getLocation().y);
			board[emptySpot.getLocation().x][emptySpot.getLocation().y] = players[i];
		}
	}
	
	private void initEnemies(Enemy[] enemies){
		Point emptySpot;		
		for (int i=0;i < enemies.length; i++) {
			emptySpot = getEmptySpot();
			enemies[i] = new Enemy(emptySpot.getLocation().x, emptySpot.getLocation().y);
			board[emptySpot.getLocation().x][emptySpot.getLocation().y] = enemies[i];
		}
	}
	private void initPowerups(Powerup[] powerups){
		Point emptySpot;
		for (int i=0;i < powerups.length; i++) {
			emptySpot = getEmptySpot();
			powerups[i] = new Powerup(emptySpot.getLocation().x, emptySpot.getLocation().y);
			board[emptySpot.getLocation().x][emptySpot.getLocation().y] = powerups[i];
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
		if(onBoard(x, y + 1)){
			obj.move(x, y + 1);
			board[x][y] = null;
			board[x][y + 1] = obj;
		}
	}
	public void moveDown(MovingObject obj) {
	    int x = obj.x();
	    int y = obj.y();
	    if(onBoard(x, y - 1)){
	      obj.move(x, y - 1);
	      board[x][y] = null;
	      board[x][y - 1] = obj;
	    }
	}
	public void moveLeft(MovingObject obj) {
	    int x = obj.x();
	    int y = obj.y();
	    if(onBoard(x - 1, y)){
	      obj.move(x - 1, y);
	      board[x][y] = null;
	      board[x - 1][y] = obj;
	    }
	}
	public void moveRight(MovingObject obj) {
	    int x = obj.x();
	    int y = obj.y();
	    if(onBoard(x + 1, y)){
	      obj.move(x + 1, y);
	      board[x][y] = null;
	      board[x + 1][y] = obj;
	    }
	}
	
	// Returns whether the new location is on the board
	private boolean onBoard(int x, int y){
		boolean xValid = (x >= 0) && (x <= this.width);
		boolean yValid = (y >= 0) && (y <= this.height);
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
					intArr[x][y] = this.board[x][y].getType().ordinal();
				}
				else{
					intArr[x][y] = GameObjectType.EMPTY.ordinal();
				}
			}
		}
		return intArr;
	}
}
