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
	 * @param coord the coord to set
	 */
	public void setCoord(Point coord) {
		this.coord = coord;
	}
}
