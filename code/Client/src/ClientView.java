import javax.swing.*;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientView extends JFrame{

	public ClientView() 
	{
	    super("ClientView");
	    GraphicsEnvironment.getLocalGraphicsEnvironment();
	    Font font = new Font("LucidaSans", Font.PLAIN, 14);
	    JTextArea textArea = new JTextArea();
	    textArea.setFont(font);
	    this.getContentPane().add(textArea);
	    textArea.show();
	  }

	public static void main (String[] args)
	{
		JFrame frame = new ClientView();
		frame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		
		frame.pack();
	    frame.setVisible(true);
	}
	
	public void update(String update){
		
		render();
	}
	
	public void render(){
		
	}
	
}
