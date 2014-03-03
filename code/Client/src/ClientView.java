import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class ClientView implements ActionListener {
	
	Font font = new Font("LucidaSans", Font.PLAIN, 20);
	Client client;
	JTextField field;
	JButton button;
	
	public ClientView (Client c) {
		this.client = c;
		JFrame frame = new JFrame("Bomberman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    button = new JButton("Connect to server...");
	    field = new JTextField();

	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    button.addActionListener(this);
	    field.setAlignmentX(Component.CENTER_ALIGNMENT);

	    Container panel = frame.getContentPane();
	    panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
	    field.setEditable(false);
	    panel.add( field );
	    panel.add( button );

	    frame.setSize(500, 500);
	    frame.setVisible(true);
	    
	    // Handle keyboard input
	    JPanel view = ((JPanel) panel);
	    InputMap im = view.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap am = view.getActionMap();
	    
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RightArrow");
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LeftArrow");
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UpArrow");
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DownArrow");

	    am.put("RightArrow", new ArrowAction("RightArrow"));
	    am.put("LeftArrow", new ArrowAction("LeftArrow"));
	    am.put("UpArrow", new ArrowAction("UpArrow"));
	    am.put("DownArrow", new ArrowAction("DownArrow"));
	}
	
	public class ArrowAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private String cmd;

	    public ArrowAction(String cmd) {
	        this.cmd = cmd;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        if (cmd.equalsIgnoreCase("LeftArrow")) {
	        	client.move(Direction.LEFT);
	        } else if (cmd.equalsIgnoreCase("RightArrow")) {
	        	client.move(Direction.RIGHT);
	        } else if (cmd.equalsIgnoreCase("UpArrow")) {
	            client.move(Direction.UP);
	        } else if (cmd.equalsIgnoreCase("DownArrow")) {
	        	client.move(Direction.DOWN);
	        }
	    }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		button.setText("Connecting...");
		button.setEnabled(false);
		client.connect();
		render();
	}
	
	public void render() {
		int playerid = client.getPlayerID();
		if(playerid > 0) {
			button.setText("Connected as Player " + playerid);
		}
		
		// get state from client and render state
		field.setText(client.getGameBoard());
	}
}
