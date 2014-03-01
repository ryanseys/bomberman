import java.awt.Point;
import java.util.Random;


public class Board {
	private static final int MAX_WIDTH = 25;
	private static final int MAX_HEIGHT = 25;
	private GameObject[][] board;
	private GameObject door;

	public Board(){
		this.setBoard(new GameObject[MAX_WIDTH][MAX_HEIGHT]);
	}

	public Board(int height, int width){
		this.setBoard(new GameObject[height][width]);
	}

	/**
	 * @return the board
	 */
	public GameObject[][] getBoard() {
		return board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(GameObject[][] board) {
		this.board = board;
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
	}
	private void initPlayers(Player[] players){
		Point emptySpot;
		
		for (int i=0;i < players.length; i++) {
			emptySpot = getEmptySpot();
			players[i] = new Player(emptySpot.getLocation().x, emptySpot.getLocation().y);
		}
	}
	
	private void initEnemies(Enemy[] enemies){
		Point emptySpot;		
		for (int i=0;i < enemies.length; i++) {
			emptySpot = getEmptySpot();
			enemies[i] = new Enemy(emptySpot.getLocation().x, emptySpot.getLocation().y);
		}
	}
	private void initPowerups(Powerup[] powerups){
		Point emptySpot;
		for (int i=0;i < powerups.length; i++) {
			emptySpot = getEmptySpot();
			powerups[i] = new Powerup(emptySpot.getLocation().x, emptySpot.getLocation().y);
		}
	}
	private Point getEmptySpot(){
		boolean emptySpot = true;
		int x, y;
		Random r = new Random();
		do{
			x = r.nextInt(MAX_WIDTH);
			y = r.nextInt(MAX_HEIGHT);
			if(board[x][y] == null){
				emptySpot = true;
			}else{emptySpot = false;}
		}while(!emptySpot);
		return new Point(x,y);
	}
}
