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
		this.buffer2 = msg.toString();
		controllerBufferOne = true;
	}

	void updateGameState() {
		synchronized (controllerBufferOne){
			JSONObject msg;
			if(controllerBufferOne){
				msg = new JSONObject();
				msg.put("game", game.toJSON());
				msg.put("type", "broadcast");
				buffer1 = msg.toString();
				controllerBufferOne = false;
			}else{
				msg = new JSONObject();
				msg.put("game", game.toJSON());
				msg.put("type", "broadcast");
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
