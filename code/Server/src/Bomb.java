import java.awt.Point;


public class Bomb extends Thread {
	private int range;    // The range this bomb will have
	private GameObject gameObj;
	private Player player;
	private int x,y;
	private Game game;
	private PlaceBomb plcbmb;
	
	public Bomb(Player player, Game game) {
		this.player=player;
		x=player.location.x;
		y=player.location.y;
		this.game=game;
		this.range = player.getBombRange();
		plcbmb=new PlaceBomb(this,x,y);
		plcbmb.start();
		
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			plcbmb.kill();
			synchronized(player){ 
				player.setCurrentBombs(player.getCurrentBombs()+1);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game.getBoard().fire(explosionHeightOne(), explosionHeightTwo(), explosionWidthOne(), explosionWidthTwo(), x,y);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game.getBoard().clearFire();
	}
	
	
	//will check to see if the range is within the limit of the grid in the y direction to the point of the placement of the bomb.
	private int explosionHeightOne()
	{	
		if ( y-range>=0)
		{
			return range;
		}else{
		return y;
		}
	}
	//will check to see if the range is within the limit of the grid in the y direction to the end of the map from the placement of the bomb.
	private int explosionHeightTwo()
	{	if((game.getBoard().getHeight()-y)-range>=0)
		{
			return range;
		}else{
		return game.getBoard().getHeight()-y;
		}
		
	}
	//will check to see if the range is within the limit of the grid in the x direction to the point of the placement of the bomb.
	private int explosionWidthOne()
	{	
		if ( x-range>=0)
		{
			return range;
		}else{
		return x;
		}
	}
	//will check to see if the range is within the limit of the grid in the x direction to the end of the map from the placement of the bomb.
	private int explosionWidthTwo()
	{	
		if((game.getBoard().getWidth()-x)-range>=0)
		{
			return range;
		}else{
		return game.getBoard().getWidth()-x;
		}
		
	}
	public Game getGame()
	{
		return game;
	}

}