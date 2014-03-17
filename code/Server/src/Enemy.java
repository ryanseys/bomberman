// Class to hold functions specific to the enemy class
public class Enemy extends MovingObject {
	private boolean isAlive;  // If the Enemy is alive
	public Enemy(int x, int y) {
		super(GameObjectType.ENEMY, x, y);
	}
	public void dies() {
		this.setVisible(false);
		this.isAlive = false;
		this.move(-1, -1);
	}
}
