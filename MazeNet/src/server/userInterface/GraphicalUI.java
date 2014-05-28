package server.userInterface;

import generated.MoveMessageType;
import generated.TreasureType;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import config.Settings;
import server.Board;
import server.Card;
import server.Game;
import server.Player;
import server.Position;

public class GraphicalUI extends javax.swing.JFrame implements UI {
	
	private static final long serialVersionUID = -3985631697359900852L;
	private JPanel shiftCardContainer;
	private JLabel jLShiftCard;
	private JRadioButtonMenuItem MI4Spieler;
	private JRadioButtonMenuItem MI3Spieler;
	private JRadioButtonMenuItem MI2Spieler;
	private JRadioButtonMenuItem MI1Spieler;
	private JMenu MPlayerSettings;
	private JMenuItem MIStart;
	private JMenuItem MIStop;
	private JMenu jMenu1;
	private JMenuBar jMenuBar1;
	private JLabel jLTreasuresToGo;
	private JLabel jLName;
	private JLabel currentTreasure;
	private JLabel jLSpieler;
	private JLabel jLcurrentTreasure;
	private JPanel statisticsPane;
	private GraphicalBoard BoardPane;
	private GraphicalCardBuffered shiftCard;

	private JLabel[][] stats;
	@SuppressWarnings("unused")
	private Integer currentPlayer;
	protected static String[] arguments;
	private Game g;

	@Deprecated
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GraphicalUI inst = new GraphicalUI();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
		arguments=args;
	}

	public GraphicalUI() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			thisLayout.rowWeights = new double[] { 0.0, 0.0 };
			thisLayout.rowHeights = new int[] { 100, 100 };
			thisLayout.columnWeights = new double[] { 0.0, 0.0 };
			thisLayout.columnWidths = new int[] { 70, 30 };

			getContentPane().setLayout(thisLayout);
			this.setTitle("MazeCom");
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu1 = new JMenu();
					jMenuBar1.add(jMenu1);
					jMenu1.setText("Server");
					{
						MIStart = new JMenuItem();
						jMenu1.add(MIStart);
						MIStart.setText("Start");
						MIStart.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								MIStartActionPerformed(evt);
							}
						});
						//MIStart.addActionListener(new StartAction(this) );
					}
					{
						MIStop = new JMenuItem();
						jMenu1.add(MIStop);
						MIStop.setText("Stop");
						MIStop.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								MIStopActionPerformed(evt);
							}
						});
						//MIStop.addActionListener(new StartAction(this) );
					}
				}
				{
					MPlayerSettings = new JMenu();
					jMenuBar1.add(MPlayerSettings);
					MPlayerSettings.setText("Spieleranzahl");
					{
						MI1Spieler = new JRadioButtonMenuItem();
						MPlayerSettings.add(MI1Spieler);
						MI1Spieler.setText("1 Spieler");
						MI1Spieler.setSelected(true);
						MI1Spieler.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								Settings.DEFAULT_PLAYERS=1;
							}
						});
					}
					{
						MI2Spieler = new JRadioButtonMenuItem();
						MPlayerSettings.add(MI2Spieler);
						MI2Spieler.setText("2 Spieler");
						MI2Spieler.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								Settings.DEFAULT_PLAYERS=2;
							}
						});
					}
					{
						MI3Spieler = new JRadioButtonMenuItem();
						MPlayerSettings.add(MI3Spieler);
						MI3Spieler.setText("3 Spieler");
						MI3Spieler.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								Settings.DEFAULT_PLAYERS=3;
							}
						});
					}
					{
						MI4Spieler = new JRadioButtonMenuItem();
						MPlayerSettings.add(MI4Spieler);
						MI4Spieler.setText("4 Spieler");
						MI4Spieler.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								Settings.DEFAULT_PLAYERS=4;
							}
						});
					}
					ButtonGroup spielerAnz=new ButtonGroup();
					spielerAnz.add(MI1Spieler);
					spielerAnz.add(MI2Spieler);
					spielerAnz.add(MI3Spieler);
					spielerAnz.add(MI4Spieler);
					

				}
			}
			{
				BoardPane = new GraphicalBoard();
				getContentPane().add(
						BoardPane,
						new GridBagConstraints(0, 0, 1, 2, 0.7, 0.1,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(5, 5, 5, 5), 0, 0));
				BoardPane.setSize(700, 700);

			}
			this.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					System.out.println("this.windowClosed, event=" + evt);
					System.exit(0);
				}
			});

			{
				shiftCardContainer = new JPanel();
				GridBagLayout shiftCardContainerLayout = new GridBagLayout();
				shiftCardContainer.setLayout(shiftCardContainerLayout);
				getContentPane().add(
						shiftCardContainer,
						new GridBagConstraints(1, 0, 1, 1, 0.0, 0.3,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 0), 0, 0));
				shiftCardContainer.setPreferredSize(new java.awt.Dimension(120,
						150));
				{
					jLShiftCard = new JLabel();
					shiftCardContainer.add(jLShiftCard, new GridBagConstraints(
							0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
							0));
					jLShiftCard.setText("freie Karte:");
				}
				{
					shiftCard = new GraphicalCardBuffered();
					shiftCardContainer.add(shiftCard, new GridBagConstraints(0,
							1, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH,
							GridBagConstraints.VERTICAL,
							new Insets(0, 0, 0, 0), 0, 0));
					shiftCard
							.setPreferredSize(new java.awt.Dimension(120, 120));
					shiftCard.setSize(120, 120);

				}
				shiftCardContainerLayout.rowWeights = new double[] { 0.1, 0.1 };
				shiftCardContainerLayout.rowHeights = new int[] { 7, 7 };
				shiftCardContainerLayout.columnWeights = new double[] { 0.1 };
				shiftCardContainerLayout.columnWidths = new int[] { 7 };
			}
			{
				statisticsPane = new JPanel();
				GridBagLayout statisticsPaneLayout = new GridBagLayout();
				getContentPane().add(
						statisticsPane,
						new GridBagConstraints(1, 1, 1, 1, 0.0, 0.2,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 0), 0, 0));
				statisticsPaneLayout.rowWeights = new double[] { 0.1, 0.1, 0.1,
						0.1, 0.1, 0.1, 0.1, 0.1 };
				statisticsPaneLayout.rowHeights = new int[] { 7, 7, 7, 7, 7, 7,
						7, 7 };
				statisticsPaneLayout.columnWeights = new double[] { 0.1, 0.1 };
				statisticsPaneLayout.columnWidths = new int[] { 7, 7 };
				statisticsPane.setLayout(statisticsPaneLayout);
				{
					jLcurrentTreasure = new JLabel();
					statisticsPane.add(jLcurrentTreasure,
							new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.NONE, new Insets(0, 0,
											0, 0), 0, 0));
					jLcurrentTreasure.setText("gesuchter Schatz:");
				}
				{
					currentTreasure = new JLabel();
					statisticsPane.add(currentTreasure, new GridBagConstraints(
							0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
							0));
					currentTreasure
							.setIcon(new ImageIcon(
									getClass()
											.getClassLoader()
											.getResource(
													"server/userInterface/resources/sym05.png")));
				}
				{
					jLSpieler = new JLabel();
					statisticsPane.add(jLSpieler, new GridBagConstraints(0, 2,
							1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
							0));
					jLSpieler.setText("Spieler");
				}
				{
					jLName = new JLabel();
					statisticsPane.add(jLName, new GridBagConstraints(0, 3, 1,
							1, 0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
							0));
					jLName.setText("Name");
				}
				{
					jLTreasuresToGo = new JLabel();
					statisticsPane.add(jLTreasuresToGo, new GridBagConstraints(
							1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
							0));
					jLTreasuresToGo.setText("übrige Schätze");
				}
			}
			stats = new JLabel[4][2];
			for (int i = 0; i < stats.length; i++) {
				stats[i][0] = new JLabel();
				statisticsPane.add(stats[i][0], new GridBagConstraints(0,
						4 + i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				stats[i][0].setText((i + 1) + ": Nicht Verbunden");

				stats[i][1] = new JLabel();
				statisticsPane.add(stats[i][1], new GridBagConstraints(1,
						4 + i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				stats[i][1].setText("" + 0);
			}
			pack();
			this.setSize(1000, 700);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}

	@Override
	public void displayMove(MoveMessageType mm, Board gb, long moveDelay, long shiftDelay) {
		long pause = (long) (moveDelay * (2.0 / 3.0));
		long animation = moveDelay - pause;
		server.Position insertPos = new Position(mm.getShiftPosition());
		BoardPane.blink(insertPos, animation);
		updateBoard(gb);
		try {
			Thread.sleep(pause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(Board b) {
		updateBoard(b);
		this.setVisible(true);

	}

	private void updateBoard(Board b) {
		BoardPane.updateBoard(b);
		shiftCard.setCard(new Card(b.getShiftCard()));
		setCurrentTreasure(b.getTreasure());
	}

	private void setCurrentTreasure(TreasureType t) {
		if (t != null) {
			currentTreasure.setIcon(new ImageIcon(getClass().getClassLoader()
					.getResource(
							"server/userInterface/resources/" + t.value()
									+ ".png")));
		} else {
			currentTreasure.setIcon(null);
		}
	}

	@Override
	public void updatePlayerStatistics(List<Player> stats, Integer current) {
		currentPlayer=current;
		for (int i = 0; i < 4; ++i) {
			this.stats[i][0].setText((i + 1) + ": nicht verbunden");
			this.stats[i][1].setText("");
		}
		for (Player p : stats) {
			if (p.getID() == current) {
				this.stats[p.getID() - 1][0].setText(">" + p.getID() + ": "
						+ p.getName());
			} else {
				this.stats[p.getID() - 1][0].setText(p.getID() + ": "
						+ p.getName());
			}
			this.stats[p.getID() - 1][1].setText("" + p.treasuresToGo());
		}

	}
	
	private void MIStopActionPerformed(ActionEvent evt) {
		System.out.println("MIStop.actionPerformed, event="+evt);
		g.stopGame();
	}
	
	private void MIStartActionPerformed(ActionEvent evt) {
		System.out.println("MIStart.actionPerformed, event="+evt);
		if(g==null){
			setGame(new Game());
		}
		arguments=new String[0];
		g.parsArgs(arguments);
		g.setUserinterface(this);
		g.start();
	}

	@Override
	public void setGame(Game g) {
		this.g=g;
		
	}

}
