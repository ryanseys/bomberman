package com.bomberman.client;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ClientView {

	Font font = new Font(Font.MONOSPACED, Font.PLAIN, 20);
	Client client;
	JTextArea textarea;
	JButton button;
	JPanel view;
	JLabel gameOverLabel;
	JLabel powerups;
	JLabel bombs;
	JLabel  lives;
	JLabel player1;

	JFrame frame;
	JMenuBar menubar;
	JMenu fileMenu;
	JMenuItem connMenuItem;
	JMenuItem lMenuItem;
	JLabel background;
	JMenuItem eMenuItem;

	/**
	 * Create a GUI view for the client! It's awesome!
	 * @param c client object used to connect to server
	 * @throws IOException
	 */
	public ClientView (Client c) throws IOException {
		this.client = c;
		frame = new JFrame("Bomberman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	if(client.isPlayer() && client.isGameOn()) {
					client.endGame();
				}
		    }
		});
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
				try {
					if(client.isPlayer() && client.isGameOn()) {
						client.quit();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				else if((playerid > 0) && (client.isGameOn())){
					client.endGame();
				}
				else if(playerid == 0){
					String clientTypes[] = {"player", "spectator"};
					String clientType = (String) JOptionPane.showInputDialog(view,
							"Connect as?",
							"Connect",
							JOptionPane.PLAIN_MESSAGE,
							null,
							clientTypes,
							clientTypes[0]);
					if(clientType != null){
						frame.setTitle("Bomberman - Connecting as " + clientType +"...");
						connMenuItem.setText("Connecting...");
						connMenuItem.setEnabled(false);
						client.connect(clientType);
						render();
					}
				}
			}
		});

		lMenuItem = new JMenuItem("Load");
		lMenuItem.setMnemonic(KeyEvent.VK_L);
		lMenuItem.setToolTipText("Load Board");
		final FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter(new String("*.json"), "json");
		lMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				view.requestFocusInWindow();
				int playerid = client.getPlayerID();
				if((playerid >= 0) && (!client.isGameOn() || client.isGameOver())) {
					JFileChooser loadFile = new JFileChooser("./boards");
					loadFile.setApproveButtonText("Load");
					loadFile.setAcceptAllFileFilterUsed(false);
					loadFile.setFileFilter(jsonFilter);
					loadFile.showOpenDialog(view);
					if(loadFile.getSelectedFile()!=null){
						File boardFile = loadFile.getSelectedFile();
						BufferedReader br = null;
						try {
							br = new BufferedReader(new FileReader(boardFile));
						} catch (FileNotFoundException e) {
							System.out.println("Could not find specified file");
						}
						String currLine;
						String gameBoard = "";
						try {
							while((currLine = br.readLine()) != null){
								gameBoard += currLine;
							}
						} catch (IOException e) {
							System.out.println("Error reading loaded board");
						}
						client.loadGame(gameBoard);
					}
				}
				else if((playerid >= 0) && (client.isGameOn())){
					JFileChooser saveFile = new JFileChooser("./boards");
					saveFile.setAcceptAllFileFilterUsed(false);
					saveFile.setFileFilter(jsonFilter);
					saveFile.setApproveButtonText("Save");
					int saving = saveFile.showSaveDialog(view);
					if(saving == JFileChooser.APPROVE_OPTION){
						FileWriter fw = null;
						try {
							fw = new FileWriter(saveFile.getSelectedFile()+".json");
							fw.write(client.getBoardToSave());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						try {
							fw.close();
						} catch (IOException e) {
							System.out.println("Error saving the file");
						}
					}
				}
				else {
					System.out.println("!!!!!!"); // Here for testing, delete this statement later...
				}
			}
		});
		// Add file items
		fileMenu.add(connMenuItem);
		fileMenu.add(lMenuItem);
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

		frame.setSize(370, 390);
		frame.setResizable(false);
		frame.setVisible(true);

		player1 = new JLabel();
		player1.setAlignmentX(JLabel.LEFT);
		player1.setFont(font);
		panel.add(player1);
		player1.setVisible(false);


		gameOverLabel = new JLabel(new ImageIcon("gameover.png"));
		frame.add(gameOverLabel);

		powerups = new JLabel();
		powerups.setAlignmentX(JLabel.LEFT);
		powerups.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
		panel.add(powerups);

		bombs = new JLabel();
		bombs.setAlignmentX(JLabel.LEFT);
		bombs.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
		panel.add(bombs);

		lives = new JLabel();
		lives.setAlignmentX(JLabel.LEFT);
		lives.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
		panel.add(lives);

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
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "SpaceBar");

		am.put("RightArrow", new buttonAction("RightArrow"));
		am.put("LeftArrow", new buttonAction("LeftArrow"));
		am.put("UpArrow", new buttonAction("UpArrow"));
		am.put("DownArrow", new buttonAction("DownArrow"));
		am.put("SpaceBar", new buttonAction("SpaceBar"));
	}

	/**
	 * When you hit a button on the keyboard,
	 * an event will fire and be handled here.
	 */
	public class buttonAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private String cmd;

		public buttonAction(String cmd) {
			this.cmd = cmd;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(client.isPlayer()){
				if (cmd.equalsIgnoreCase("LeftArrow")) {
					client.move(Action.LEFT);
				} else if (cmd.equalsIgnoreCase("RightArrow")) {
					client.move(Action.RIGHT);
				} else if (cmd.equalsIgnoreCase("UpArrow")) {
					client.move(Action.UP);
				} else if (cmd.equalsIgnoreCase("DownArrow")) {
					client.move(Action.DOWN);
				} else if (cmd.equalsIgnoreCase("SpaceBar")){
					client.deployBomb();
				}
			}
		}
	}

	/**
	 * Render the GUI awesomeness state.
	 */
	public void render() {
		int playerid = client.getPlayerID();

		if((playerid > 0) && !client.isGameOn() && !client.isGameOver()) {
			frame.setTitle("Bomberman - Player " + playerid);
			connMenuItem.setText("Start New Game");
			connMenuItem.setToolTipText("Start New Game");
			connMenuItem.setEnabled(true);
			lMenuItem.setText("Load");
			lMenuItem.setToolTipText("Load");
			lMenuItem.setEnabled(true);
			lMenuItem.setVisible(true);
			gameOverLabel.setVisible(false);
			powerups.setVisible(false);
			bombs.setVisible(false);
			lives.setVisible(false);
			player1.setVisible(false);
		}
		else if((playerid < 0) && !client.isGameOn() && !client.isGameOver()) {
			frame.setTitle("Bomberman - Spectator");
			connMenuItem.setText("Start New Game");
			connMenuItem.setToolTipText("Start New Game");
			connMenuItem.setEnabled(false);
			connMenuItem.setVisible(false);
			lMenuItem.setEnabled(false);
			lMenuItem.setVisible(false);
			gameOverLabel.setVisible(false);
			powerups.setVisible(false);
			bombs.setVisible(false);
			lives.setVisible(false);
		}
		else if((playerid > 0)  && client.isGameOn()) {
			if(client.getLives() == 0){
				player1.setVisible(true);
				player1.setText("DEAD");
				frame.setTitle("Bomberman - Player " + playerid + " - Dead");
			}
			else{
				player1.setVisible(false);
				frame.setTitle("Bomberman - Player " + playerid + " - In Game");
			}
			background.setVisible(false);
			textarea.setVisible(true);
			gameOverLabel.setVisible(false);
			powerups.setVisible(true);
			powerups.setText("Powerups: " + client.getPowerups());
			bombs.setVisible(true);
			bombs.setText("Bombs: " + client.getBombs());
			lives.setVisible(true);
			lives.setText("Lives: " + client.getLives());
			connMenuItem.setText("End Game");
			connMenuItem.setToolTipText("End Game");
			connMenuItem.setEnabled(true);
			lMenuItem.setText("Save");
			lMenuItem.setToolTipText("Save");
			lMenuItem.setEnabled(true);
			lMenuItem.setVisible(true);

		}
		else if((playerid < 0)  && client.isGameOn()) {
			frame.setTitle("Bomberman - Spectator");
			background.setVisible(false);
			textarea.setVisible(true);
			gameOverLabel.setVisible(false);
			powerups.setVisible(true);
			bombs.setVisible(true);
			player1.setVisible(true);
			connMenuItem.setText("End Game");
			connMenuItem.setToolTipText("End Game");
			connMenuItem.setEnabled(false);
			connMenuItem.setVisible(false);
			lMenuItem.setEnabled(false);
			lMenuItem.setVisible(false);
			player1.setText("Player 1");
			powerups.setText("Powerups: " + client.getPowerups());
			bombs.setText("Bombs: " + client.getBombs());
			lives.setText("Lives: " + client.getLives());
		}
		else if(((playerid > 0)) && (client.isGameOver())) {
			player1.setVisible(false);
			frame.setTitle("Bomberman - Player " + playerid + " - Game Over");
			textarea.setVisible(false);
			background.setVisible(false);
			if(client.isWinner()){
				gameOverLabel.setIcon(new ImageIcon("winner.png"));
			}
			else{
				gameOverLabel.setIcon(new ImageIcon("gameover.png"));
			}
			gameOverLabel.setVisible(true);
			powerups.setVisible(false);
			bombs.setVisible(false);
			connMenuItem.setText("Start New Game");
			connMenuItem.setToolTipText("Start New Game");
			connMenuItem.setEnabled(true);
			lMenuItem.setText("Load");
			lMenuItem.setToolTipText("Load");
			lMenuItem.setEnabled(true);
			lMenuItem.setVisible(true);
		}
		else if(((playerid < 0)) && (client.isGameOver())) {
			frame.setTitle("Bomberman - Game Over");
			textarea.setVisible(false);
			gameOverLabel.setVisible(true);
			powerups.setVisible(false);
			bombs.setVisible(false);
			connMenuItem.setText("Start New Game");
			connMenuItem.setToolTipText("Start New Game");
			connMenuItem.setEnabled(false);
			connMenuItem.setVisible(false);
			lMenuItem.setEnabled(false);
			lMenuItem.setVisible(false);
			player1.setVisible(false);
		}

		// always render the game board
		textarea.setText(client.getGameBoard());

		view.grabFocus();
	}
}
