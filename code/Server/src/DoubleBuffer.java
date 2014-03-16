import org.json.JSONObject;


public class DoubleBuffer {
	private String buffer1;
	private String buffer2;
	private Boolean controllerBufferOne;
	private Game game;
	private JSONObject msg1;
	private JSONObject msg2;

	public DoubleBuffer(Game game) {
		msg2 = new JSONObject();
		this.game = game;
		msg2.put("game", game.toJSON());
		msg2.put("type", "broadcast");
		this.buffer2 = msg2.toString();
		controllerBufferOne = true;
	}
	public DoubleBuffer() {
		controllerBufferOne = true;
	}
	void controllerSetState() {
		synchronized (controllerBufferOne){
			if(controllerBufferOne){
				msg1 = new JSONObject();
				msg1.put("game", game.toJSON());
				msg1.put("type", "broadcast");
				buffer1 = msg1.toString();
				controllerBufferOne = false;
			}else{
				msg2 = new JSONObject();
				msg2.put("game", game.toJSON());
				msg2.put("type", "broadcast");
				buffer2 = msg2.toString();
				controllerBufferOne = true;
			}
		}
	}

	public String broadcasterGetState() {
		synchronized (controllerBufferOne){
			if(!controllerBufferOne){
				return buffer1;
			}else{
				return buffer2;
			}
		}
	}
}
