
public abstract class MovingObject extends GameObject {

	public MovingObject(GameObjectType type, int x, int y) {
		super(type, x, y);
	}

	public void move(int x, int y){
		super.setLocation(x, y);
	}
}
