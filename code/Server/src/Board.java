
public class Board {
	private static final int MAX_WIDTH = 25, MAX_HEIGHT = 25;
	private GameObject[][] board;
	
	Board(){
		this.setBoard(new GameObject[MAX_WIDTH][MAX_HEIGHT]);
	}
	Board(int height, int width){
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
	public void initBoard(int players, int enemies, int powerups){
		
	}

}
