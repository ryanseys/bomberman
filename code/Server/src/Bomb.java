
public class Bomb extends GameObject {
	private int range;

	public Bomb(GameObjectType type, int x, int y) {
		super(GameObjectType.BOMB, x, y);
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}
}
