package com.bomberman.server;
// Class to hold functions specific to the enemy class
public class Enemy extends MovingObject {

	public Enemy(int x, int y) {
		super(GameObjectType.ENEMY, x, y);
	}
}