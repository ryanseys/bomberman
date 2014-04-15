package com.bomberman.server;
import java.awt.Point;
// Basic game object type that has all properties
// common to all of the objects on the board

public class GameObject {
	private boolean isVisible; // Is the object currently visible
	private GameObjectType type; // What type of game object is it?
	protected Point location; // Position of the object on the board

	GameObject(GameObjectType type, int x, int y) {
		this.type = type;
		this.location = new Point(x, y);
		this.isVisible = true;
	}

	// return x position
	public int x() {
		return location.x;
	}

	// return y position
	public int y() {
		return location.y;
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

	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible
	 *            the isVisible to set
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

}
