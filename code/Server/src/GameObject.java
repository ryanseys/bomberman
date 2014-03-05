import java.awt.Point;

public class GameObject {
	private boolean isVisible;
	private GameObjectType type;
	protected Point location;

	GameObject(GameObjectType type, int x, int y){
		this.type = type;
		this.location = new Point(x,y);
		this.isVisible = true;
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

	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
}
