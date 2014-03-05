import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class ClientView {

	Font font = new Font("LucidaSans", Font.PLAIN, 28);
	Client client;
	JTextArea textarea;
	JButton button;
	JPanel view;
	JFrame frame;
	JMenuBar menubar;
	JMenu fileMenu;
	JMenuItem connMenuItem;
	JLabel background;
	JMenuItem eMenuItem;

	public ClientView (Client c) throws IOException {
		this.client = c;
		frame = new JFrame("Bomberman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menubar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		// background image
		background = new JLabel(new ImageIcon("bomberman.png"));

		// Quit item
		eMenuItem = new JMenuItem("Quit");
		eMenuItem.setMnemonic(KeyEvent.VK_Q);
        eMenuItem.setToolTipText("Quit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        // Connect item
        connMenuItem = new JMenuItem("Connect");
 		connMenuItem.setMnemonic(KeyEvent.VK_C);
 		connMenuItem.setToolTipText("Connect to server");
 		connMenuItem.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent event) {
            	view.requestFocusInWindow();
 				int playerid = client.getPlayerID();

 				if((playerid > 0) && !client.isGameOn() && !client.isGameOver()) {
 					client.startGame();
 				}
 				else if((playerid > 0) && client.isGameOver()) {
 					client.newGame();
 				}
 				else {
 					frame.setTitle("Bomberman - Connecting...");
 					connMenuItem.setText("Connecting...");
 					connMenuItem.setEnabled(false);
 					client.connect();
 					render();
 				}
             }
        });

 		// Add file items
        fileMenu.add(connMenuItem);
        fileMenu.add(eMenuItem);
        menubar.add(fileMenu);
        frame.setJMenuBar(menubar);

		textarea = new JTextArea();
		textarea.setFont(font);

		Container panel = frame.getContentPane();
		panel.setFocusable(true);
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );

		textarea.setAlignmentX(Component.CENTER_ALIGNMENT);
		textarea.setAlignmentY(Component.TOP_ALIGNMENT);
		textarea.setEditable(false);
		textarea.setEnabled(false);
		textarea.setDisabledTextColor(Color.BLACK);
		textarea.setVisible(false);

		frame.add(background);
		panel.add(textarea);

		frame.setSize(380, 450);
		frame.setResizable(false);
		frame.setVisible(true);

		// Handle keyboard input
		view = ((JPanel) panel);
		view.setRequestFocusEnabled(true);
		view.requestFocusInWindow();
		view.grabFocus();
		InputMap im = view.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
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

	public void render() {
		int playerid = client.getPlayerID();

		if((playerid > 0) && !client.isGameOn() && !client.isGameOver()) {
			frame.setTitle("Bomberman - Player " + playerid);
			connMenuItem.setText("New Game");
			connMenuItem.setEnabled(true);
		}
		else if((playerid > 0)  && client.isGameOn()) {
			frame.setTitle("Bomberman - Player " + playerid + " - In Game");
			background.setVisible(false);
			textarea.setVisible(true);
			connMenuItem.setText("End Game");
			connMenuItem.setEnabled(true);
		}
		else if(client.isGameOver()) {
			frame.setTitle("Bomberman - Player " + playerid + " - Game Over");
			connMenuItem.setText("New Game");
			connMenuItem.setEnabled(true);
		}

		// always render the game board
		textarea.setText(client.getGameBoard());

		view.grabFocus();
	}
}
