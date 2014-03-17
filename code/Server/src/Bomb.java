
public class Bomb extends Thread {
	private int range;    // The range this bomb will have
	private GameObject gameObj;
	Player player;
	Game game;
	
	public Bomb(Player player, Game game) {
		this.player=player;
		this.game=game;
		this.range = player.getBombRange();
		game.getBoard().placeBomb(player);
		
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
	}
	//will check to see if the range is within the limit of the grid int the x direction.
	private int explosionX()
	{
		if(game.getBoard().getWidth()==0)
		{
			
		}
		
		return 0;
		
	}
	//will check to see if the range is within the limit of the grid int the y direction.
	private int explosionY()
	{
		return 0;
		
	}
}