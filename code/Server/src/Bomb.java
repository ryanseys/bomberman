import java.awt.Point;


public class Bomb extends Thread {
	private int range;    // The range this bomb will have
	private GameObject gameObj;
	private Player player;
	private Point bombPlace;
	private Game game;
	
	public Bomb(Player player, Game game) {
		this.player=player;
		this.game=game;
		this.range = player.getBombRange();
		bombPlace=game.getBoard().placeBomb(player);
		
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			synchronized(player){ 
				player.setCurrentBombs(player.getCurrentBombs()+1);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game.getBoard().fire(explosionHeightOne(), explosionHeightTwo(), explosionWidthOne(), explosionWidthTwo(), bombPlace);
	}
	
	
	//will check to see if the range is within the limit of the grid in the y direction to the point of the placement of the bomb.
	private int explosionHeightOne()
	{	
		if ( bombPlace.y-range>=0)
		{
			return range;
		}else{
		return bombPlace.y;
		}
	}
	//will check to see if the range is within the limit of the grid in the y direction to the end of the map from the placement of the bomb.
	private int explosionHeightTwo()
	{	if((game.getBoard().getHeight()-bombPlace.y)-range>=0)
		{
			return range;
		}else{
		return game.getBoard().getHeight()-bombPlace.y;
		}
		
	}
	//will check to see if the range is within the limit of the grid in the x direction to the point of the placement of the bomb.
	private int explosionWidthOne()
	{	
		if ( bombPlace.x-range>=0)
		{
			return range;
		}else{
		return bombPlace.x;
		}
	}
	//will check to see if the range is within the limit of the grid in the x direction to the end of the map from the placement of the bomb.
	private int explosionWidthTwo()
	{	
		if((game.getBoard().getWidth()-bombPlace.x)-range>=0)
		{
			return range;
		}else{
		return game.getBoard().getWidth()-bombPlace.x;
		}
		
	}
}