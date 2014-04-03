package com.bomberman.server;

import java.util.ArrayList;
import java.util.Random;

// Class to hold functions specific to the enemy class
public class Enemy extends MovingObject implements Runnable{
	private Board board;
	private String enemyType;
	private ArrayList<Player> players;
	private Game game;
	
	public Enemy(Game game, ArrayList<Player> players, Board board) {
		super(GameObjectType.ENEMY, -1, -1);
		this.players = players;
		this.board = board;
		this.game = game;
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
		try{
			Thread.sleep(3500);
		}catch(Exception e){e.printStackTrace();}
		while(this.isAlive()){
			if(players.size() == 0){
				this.dies();
				return;
			}
			if(enemyType.equals("smart")){
				smartEnemyMove();
			}
			else if(enemyType.equals("kamakazi")){
				Player target = null;
				while(target == null || !target.isAlive()){
					int i = ((new Random()).nextInt(players.size()));
					i -= (i == players.size()) ? 1 : 0;
					target = players.get(i);
				}
				synchronized(this){
					kamakaziEnemyMove(target);
				}
			}
			game.checkGameStatus();
			if(!this.isAlive()){
				return;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
	// Run towards enemy and try to kill you and them
	private void kamakaziEnemyMove(Player target){
		if(board == null){
			return;
		}
		if(this.x() < 0 || this.y() < 0){
			System.out.println("alive: " + this.isAlive());
		}
		boolean moveX = (Math.abs(this.x() - target.x()) > Math.abs(this.y() - target.y()));
		if(moveX){
			if(this.x() > target.x()){
				board.moveLeft(this);
			}
			else{
				board.moveRight(this);
			}
		}
		else{
			if(this.y() > target.y()){
				board.moveUp(this);
			}
			else{
				board.moveDown(this);
			}
		}
	}
}
