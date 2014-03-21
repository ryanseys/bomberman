package com.bomberman.server;

import java.util.ArrayList;
import java.util.Random;

// Class to hold functions specific to the enemy class
public class Enemy extends MovingObject implements Runnable{
	private Board board;
	private String enemyType;
	private ArrayList<Player> players;
	
	public Enemy(int x, int y, ArrayList<Player> players, Board board) {
		super(GameObjectType.ENEMY, x, y);
		this.players = players;
		this.board = board;
		int type = 0; //(new Random()).nextInt(0);
		
		switch(type){
		case(0):
			this.enemyType = "kamakazi";
			break;
		case(1):
			this.enemyType= "smart";
		}
		
	}
	
	public void run(){
		while(this.isAlive()){
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(enemyType.equals("smart")){
				smartEnemyMove();
			}
			else if(enemyType.equals("kamakazi")){
				Player target = null;
				for(Player player: players){
					
				}
				kamakaziEnemyMove();
			}
		}
	}
	private void smartEnemyMove(){
		if(board.getDoor().isVisible()){
			// Move towards the door
		}
		else{
			// Move towards player and try to drop bombs near them...
		}
	}
	private void kamakaziEnemyMove(){
		// Run towards enemy and try to kill you and them
	}
}
