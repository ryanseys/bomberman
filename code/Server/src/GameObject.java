import java.awt.Point;

public class GameObject {

	GameObjectType type;
	private Point coord;

	GameObject(GameObjectType type, int x, int y){
		this.type = type;
		this.coord = new Point(x,y);
	}

	/**
	 * @return the coord
	 */
	public Point getCoord() {
		return coord;
	}

	/**
	 * Set the coordinate of the game object.
	 * @param x the new x value of the coordinate
	 * @param y the new y value of the coordinate
	 */
	public void setCoord(int x, int y) {
		coord.setLocation(x, y);
	}
}
