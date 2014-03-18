

public class PlaceBomb extends Thread{
	private int x,y;
	private Bomb bmb;
	private boolean threadkill;
	public PlaceBomb(Bomb bmb,int x, int y)
	{
		threadkill=false;
		this.bmb=bmb;
		this.x=x;
		this.y=y;
	}
	

	@Override
	public void run() {
		while(!threadkill)
		{
			bmb.getGame().getBoard().placeBomb(x,y);
		}
		
	}
	public void kill()
	{
		threadkill=true;
	}
}
