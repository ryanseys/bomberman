package com.bomberman.server;

public class Bomb extends GameObject implements Runnable {
	private int range;    // The range this bomb will have
	private Player player;
	private Game game;

	public Bomb(Player player, Game game) {
		super(GameObjectType.BOMB, player.x(), player.y());
		this.game = game;
		this.player=player;
		this.range = player.getBombRange();
	}

	@Override
	public void run() {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		game.getBoard().fire(explosionHeightOne(), explosionHeightTwo(), explosionWidthOne(), explosionWidthTwo(), x,y);
		explode();
		game.checkGameStatus();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		synchronized(player){
			player.incrementBombs();
		}
		game.getBoard().clearFire();
		game.checkGameStatus();
	}

	private void explode(){
		this.game.bombExplosion(this);
		game.checkGameStatus();
	}
	/**
	 * @return the range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * @param range the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}

}