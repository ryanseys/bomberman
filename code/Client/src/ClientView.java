import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class ClientView extends JFrame{
	
	Font font = new Font("LucidaSans", Font.PLAIN, 20);
	
	public ClientView()
	{ 
		super("Bomberman");
	    JPanel panel = (JPanel) this.getContentPane();
	    setSize(700,500);
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	    
	    JPanel contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        
	    JPanel mainDisplay = new JPanel(new GridLayout(0,2, 100, 100));
	    mainDisplay.setBackground(Color.WHITE);
	    mainDisplay.setFont(font);
	    contentPane.add(mainDisplay, BorderLayout.CENTER);
	    
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
	
	public static void main(String[] args)
	{
		JFrame f = new ClientView();
	    f.show();
	}
	
}
