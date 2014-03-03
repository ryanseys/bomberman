import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class ClientView implements ActionListener {
	
	Font font = new Font("LucidaSans", Font.PLAIN, 20);
	Client client;
	JTextField field;
	
	public ClientView (Client c) {
		this.client = c;
		JFrame frame = new JFrame("Bomberman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JButton button = new JButton("Connect to server...");
	    field = new JTextField();

	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    button.addActionListener(this);
	    field.setAlignmentX(Component.CENTER_ALIGNMENT);

	    Container panel = frame.getContentPane();
	    panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
	    field.setEditable(false);
	    panel.add( field );
	    panel.add( button );
	    frame.pack();
	    frame.setVisible(true);
	    
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
	            System.out.println("The left arrow was pressed!");
	        } else if (cmd.equalsIgnoreCase("RightArrow")) {
	            System.out.println("The right arrow was pressed!");
	        } else if (cmd.equalsIgnoreCase("UpArrow")) {
	            System.out.println("The up arrow was pressed!");
	        } else if (cmd.equalsIgnoreCase("DownArrow")) {
	            System.out.println("The down arrow was pressed!");
	        }
	    }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Connecting...");
		client.connect();
		render();
	}
	
	public void render() {
		// get state from client and render state
		field.setText(client.getState());
	}
}
