import java.awt.Point;

public class GameObject {

	private GameObjectType type;
	private Point location;

	GameObject(GameObjectType type, int x, int y){
		this.type = type;
		this.location = new Point(x,y);
	}

	// return x position
	public int x(){
		return location.x;
	}
	// return y position
	public int y(){
		return location.y;
	}

	/**
	 * Set the coordinate of the game object.
	 * @param x the new x value of the coordinate
	 * @param y the new y value of the coordinate
	 */
	public void setLocation(int x, int y) {
		location.setLocation(x, y);
	}
	

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @return the type
	 */
	public GameObjectType getType() {
		return type;
	}
}
