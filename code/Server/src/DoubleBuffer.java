
public class DoubleBuffer {
	private String buffer1;
	private String buffer2;
	private Boolean controllerBufferOne;
	private Game game;
	
	public DoubleBuffer(Game game) {
		this.game = game;
		this.buffer2 = game.toJSON().toString();
		controllerBufferOne = true;
	}
	public DoubleBuffer() {
		controllerBufferOne = true;
	}
	void controllerSetState() {
		synchronized (controllerBufferOne){
			if(controllerBufferOne){
				buffer1 = game.toJSON().toString();
				controllerBufferOne = false;
			}else{
				buffer2 = game.toJSON().toString();
				controllerBufferOne = false;
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
