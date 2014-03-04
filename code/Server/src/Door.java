public class Door extends GameObject {
	private boolean isVisible;

	public Door(int x, int y) {
		super(GameObjectType.DOOR, x, y);
		this.isVisible = false;
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
	public void setVisible() {
		this.isVisible = true;
	}
}
