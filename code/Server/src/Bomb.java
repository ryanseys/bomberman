
public class Bomb extends Thread {
	private int range;    // The range this bomb will have
	private GameObject gameObj;
	Player player;
	Game game;
	public Bomb(Player player, Game game) {
		this.player=player;
		this.game=game;
		this.range = range;
		GameObject gameObj= new GameObject(GameObjectType.BOMB, this.player.x()+1, this.player.y()+1);
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
		System.out.print("I have awaken!");
	}
	
	private int explosionX()
	{
		return 0;
		
	}
	private int explosionY()
	{
		return 0;
		
	}
}