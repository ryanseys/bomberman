import java.util.concurrent.ThreadFactory;


public class BombFactory implements ThreadFactory{
	private Game game;
	public BombFactory(Game g) {
		this.game = g;
	}

	public Thread newThread(Runnable r) {
		if(r.getClass() != Bomb.class){
			return null;
		}
		Bomb bomb = (Bomb) r;
		game.getBoard().placeBomb(bomb, bomb.x(), bomb.y());
		return new Thread(r);
	}    
}
