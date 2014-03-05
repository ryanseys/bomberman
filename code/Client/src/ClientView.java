import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class ClientView implements ActionListener {

	Font font = new Font("LucidaSans", Font.PLAIN, 20);
	Client client;
	JTextArea textarea;
	JButton button;
	JPanel view;
	JFrame frame;
	JMenuBar menubar;
	JMenu fileMenu;

	public ClientView (Client c) {
		this.client = c;
		frame = new JFrame("Bomberman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menubar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenuItem eMenuItem = new JMenuItem("Quit");

		eMenuItem.setMnemonic(KeyEvent.VK_Q);
        eMenuItem.setToolTipText("Quit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        fileMenu.add(eMenuItem);

        menubar.add(fileMenu);
        frame.setJMenuBar(menubar);

		button = new JButton("Connect");
		textarea = new JTextArea();
		textarea.setFont(font);

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view.requestFocusInWindow();
				int playerid = client.getPlayerID();

				if((playerid > 0) && !client.isGameOn() && !client.isGameOver()) {
					client.startGame();
				}
				else {
					button.setText("Connecting...");
					button.setEnabled(false);
					client.connect();
					render();
				}
			}
		});

		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		textarea.setAlignmentX(Component.CENTER_ALIGNMENT);
		textarea.setAlignmentY(Component.TOP_ALIGNMENT);

		Container panel = frame.getContentPane();
		panel.setFocusable(true);

		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
		textarea.setEditable(false);
		textarea.setEnabled(false);
		textarea.setDisabledTextColor(Color.BLACK);
//		panel.add(menubar);
		panel.add(textarea);
		panel.add(button);

		frame.setSize(500, 500);
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

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public void render() {
		int playerid = client.getPlayerID();

		if((playerid > 0) && !client.isGameOn() && !client.isGameOver()) {
			frame.setTitle("Bomberman - Player " + playerid);
			button.setText("Start Game");
			button.setEnabled(true);
		}
		else if((playerid > 0)  && client.isGameOn()) {
			button.setText("End Game");
			button.setEnabled(false);
		}
		else if(client.isGameOver()) {
			button.setText("Game Over.");
			button.setEnabled(false);
		}

		// always render the game board
		textarea.setText(client.getGameBoard());

		view.grabFocus();
	}
}
