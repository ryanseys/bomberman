// Class for game objects that can move such as the Enemy or the Player
public abstract class MovingObject extends GameObject {

	public MovingObject(GameObjectType type, int x, int y) {
		super(type, x, y);
	}
	// Move to a new position
	public void move(int x, int y){
		this.location.setLocation(x, y);
	}
}
