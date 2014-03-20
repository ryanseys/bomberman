package com.bomberman.server;
import org.json.JSONObject;


public class DoubleBuffer {
	private String buffer1;
	private String buffer2;
	private Boolean controllerBufferOne;
	private Game game;

	public DoubleBuffer(Game game) {
		JSONObject msg = new JSONObject();
		this.game = game;
		msg.put("game", game.toJSON());
		msg.put("type", "broadcast");
		JSONObject players = new JSONObject();
		for(Player p : game.getPlayers()) {
			String pid = Integer.toString(p.getType().ordinal());
			JSONObject playerData = new JSONObject();
			playerData.put("powerups", p.numPowerups());
			playerData.put("bombs", p.getCurrentBombs());
			playerData.put("lives", p.getLives());
			players.put(pid, playerData);
		}
		msg.put("players", players);
		this.buffer2 = msg.toString();
		controllerBufferOne = true;
	}

	void updateGameState() {
		synchronized (controllerBufferOne){
			JSONObject msg = new JSONObject();
			if(!game.isFinished()){
				msg.put("game", game.toJSON());
				msg.put("type", "broadcast");

				JSONObject players = new JSONObject();
				for(Player p : game.getPlayers()) {
					String pid = Integer.toString(p.getType().ordinal());
					JSONObject playerData = new JSONObject();
					playerData.put("powerups", p.numPowerups());
					playerData.put("bombs", p.getCurrentBombs());
					playerData.put("lives", p.getLives());
					players.put(pid, playerData);
				}
				msg.put("players", players);
			}
			else{
				msg.put("type", "game_over");
			}
			if(controllerBufferOne){
				buffer1 = msg.toString();
				controllerBufferOne = false;
			}else{
				buffer2 = msg.toString();
				controllerBufferOne = true;
			}
		}
	}

	public String getState() {
		synchronized (controllerBufferOne){
			if(!controllerBufferOne){
				return buffer1;
			}else{
				return buffer2;
			}
		}
	}
}
