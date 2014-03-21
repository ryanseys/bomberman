package com.bomberman.server;


// Object to simulate the players
public class Player extends MovingObject {
	private int playerNum;

	public Player(int player_number, int x, int y) {
		super(playerNumToGameObj(player_number), x, y);
		this.playerNum = player_number;
	}
	private static GameObjectType playerNumToGameObj(int playerNum) {
		GameObjectType gameObj = null;
		switch (playerNum) {
		case 1:
			gameObj = GameObjectType.PLAYER_1;
			break;
		case 2:
			gameObj = GameObjectType.PLAYER_2;
			break;
		case 3:
			gameObj = GameObjectType.PLAYER_3;
			break;
		case 4:
			gameObj = GameObjectType.PLAYER_4;
			break;
		}
		return gameObj;
	}
	public int getPlayerNum(){
		return playerNum;
	}
}
