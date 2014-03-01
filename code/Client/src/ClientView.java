import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class ClientView extends JFrame{
	
	public ClientView()
	{ 
		super("Bomberman");
	    JPanel panel = (JPanel) this.getContentPane();
	    InputMap im = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap am = panel.getActionMap();
	    
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
	
	public void update(String update){
		
		render();
	}
	
	public void render(){
		
	}
	
}
