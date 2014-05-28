package server;

import generated.BoardType;

import generated.CardType;
import generated.CardType.Openings;
import generated.CardType.Pin;
import generated.MoveMessageType;
import generated.PositionType;
import generated.TreasureType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import config.Settings;

import server.Card.CardShape;
import server.Card.Orientation;
import tools.Debug;
import tools.DebugLevel;

public class Board extends BoardType {

	private TreasureType currentTreasure;

	public Board() {
		super();
		forbidden = null;
		this.getRow();
		// Erst werden alle Karten mit einer Standardkarte belegt
		for (int i = 0; i < 7; i++) {
			this.getRow().add(i, new Row());
			this.getRow().get(i).getCol();
			for (int j = 0; j < 7; j++) {
				this.getRow().get(i).getCol()
						.add(j, new Card(CardShape.I, Orientation.D0, null));
			}

		}
		// Dann wird das Spielfeld regelkonform aufgebaut
		generateInitialBoard();
	}

	private void generateInitialBoard() {
		// fixedCards:
		// Die festen, unveraenderbaren Karten auf dem Spielbrett
		setCard(0, 0, new Card(CardShape.L, Orientation.D90, null));
		setCard(0, 2,
				new Card(CardShape.T, Orientation.D0, TreasureType.SYM_13));
		setCard(0, 4,
				new Card(CardShape.T, Orientation.D0, TreasureType.SYM_14));
		setCard(0, 6, new Card(CardShape.L, Orientation.D180, null));
		setCard(2, 0, new Card(CardShape.T, Orientation.D270,
				TreasureType.SYM_15));
		setCard(2, 2, new Card(CardShape.T, Orientation.D270,
				TreasureType.SYM_16));
		setCard(2, 4,
				new Card(CardShape.T, Orientation.D0, TreasureType.SYM_17));
		setCard(2, 6, new Card(CardShape.T, Orientation.D90,
				TreasureType.SYM_18));
		setCard(4, 0, new Card(CardShape.T, Orientation.D270,
				TreasureType.SYM_19));
		setCard(4, 2, new Card(CardShape.T, Orientation.D180,
				TreasureType.SYM_20));
		setCard(4, 4, new Card(CardShape.T, Orientation.D90,
				TreasureType.SYM_21));
		setCard(4, 6, new Card(CardShape.T, Orientation.D90,
				TreasureType.SYM_22));
		setCard(6, 0, new Card(CardShape.L, Orientation.D0, null));
		setCard(6, 2, new Card(CardShape.T, Orientation.D180,
				TreasureType.SYM_23));
		setCard(6, 4, new Card(CardShape.T, Orientation.D180,
				TreasureType.SYM_24));
		setCard(6, 6, new Card(CardShape.L, Orientation.D270, null));

		// die freien verschiebbaren Teile auf dem Spielbrett
		ArrayList<Card> freeCards = new ArrayList<Card>();
		Random random = new Random();
		if (Settings.TESTBOARD) {
			random.setSeed(Settings.TESTBOARD_SEED);
		}
		// 15 mal L-shape (6 (sym) + 9 (ohne))
		freeCards.add(new Card(CardShape.L, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_01));
		freeCards.add(new Card(CardShape.L, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_02));
		freeCards.add(new Card(CardShape.L, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_03));
		freeCards.add(new Card(CardShape.L, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_04));
		freeCards.add(new Card(CardShape.L, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_05));
		freeCards.add(new Card(CardShape.L, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_06));

		for (int i = 0; i < 9; i++) {
			freeCards.add(new Card(CardShape.L, Orientation.fromValue(random
					.nextInt(4) * 90), null));
		}

		// 13 mal I-shape
		for (int i = 0; i < 13; i++) {
			freeCards.add(new Card(CardShape.I, Orientation.fromValue(random
					.nextInt(4) * 90), null));
		}

		// 6 mal T-shape
		freeCards.add(new Card(CardShape.T, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_07));
		freeCards.add(new Card(CardShape.T, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_08));
		freeCards.add(new Card(CardShape.T, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_09));
		freeCards.add(new Card(CardShape.T, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_10));
		freeCards.add(new Card(CardShape.T, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_11));
		freeCards.add(new Card(CardShape.T, Orientation.fromValue(random
				.nextInt(4) * 90), TreasureType.SYM_12));

		if (!Settings.TESTBOARD)
			Collections.shuffle(freeCards);

		int k = 0;
		for (int i = 1; i < 7; i += 2) {
			for (int j = 0; j < 7; j += 1) {
				setCard(i, j, freeCards.get(k++));
			}
		}
		for (int i = 1; i < 7; i += 2) {
			for (int j = 0; j < 7; j += 2) {
				setCard(j, i, freeCards.get(k++));
			}
		}
		this.setShiftCard(freeCards.get(k));
		getCard(0, 0).getPin().getPlayerID().add(1);
		getCard(0, 6).getPin().getPlayerID().add(2);
		getCard(6, 0).getPin().getPlayerID().add(3);
		getCard(6, 6).getPin().getPlayerID().add(4);

		// Start als Schatz hinterlegen
		getCard(0, 0).setTreasure(TreasureType.START_01);
		getCard(0, 6).setTreasure(TreasureType.START_02);
		getCard(6, 0).setTreasure(TreasureType.START_03);
		getCard(6, 6).setTreasure(TreasureType.START_04);

		Debug.print(this.toString(), DebugLevel.DEBUG);
	}

	// Ausgabe des Spielbretts als AsciiArt
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Board [currentTreasure=" + currentTreasure + "]\n");
		sb.append("SpielBrett:\n");
		sb.append(" ------ ------ ------ ------ ------ ------ ------ \n");
		for (int i = 0; i < getRow().size(); i++) {
			StringBuilder line1 = new StringBuilder("|");
			StringBuilder line2 = new StringBuilder("|");
			StringBuilder line3 = new StringBuilder("|");
			StringBuilder line4 = new StringBuilder("|");
			StringBuilder line5 = new StringBuilder("|");
			StringBuilder line6 = new StringBuilder("|");
			for (int j = 0; j < getRow().get(i).getCol().size(); j++) {
				Card c = new Card(getCard(i, j));
				if (c.getOpenings().isTop()) {
					line1.append("##  ##|");
					line2.append("##  ##|");
				} else {
					line1.append("######|");
					line2.append("######|");
				}
				if (c.getOpenings().isLeft()) {
					line3.append("  ");
					line4.append("  ");
				} else {
					line3.append("##");
					line4.append("##");
				}
				if (c.getPin().getPlayerID().size() != 0) {
					line3.append("S");
				} else {
					line3.append(" ");
				}
				if (c.getTreasure() != null) {
					String name = c.getTreasure().name();
					switch (name.charAt(1)) {
					case 'Y':
						// Symbol
						line3.append("T");
						break;
					case 'T':
						// Startpunkt
						line3.append("S");
						break;
					}
					line4.append(name.substring(name.length() - 2));
				} else {
					line3.append(" ");
					line4.append("  ");
				}
				if (c.getOpenings().isRight()) {
					line3.append("  |");
					line4.append("  |");
				} else {
					line3.append("##|");
					line4.append("##|");
				}
				if (c.getOpenings().isBottom()) {
					line5.append("##  ##|");
					line6.append("##  ##|");
				} else {
					line5.append("######|");
					line6.append("######|");
				}
			}
			sb.append(line1.toString() + "\n");
			sb.append(line2.toString() + "\n");
			sb.append(line3.toString() + "\n");
			sb.append(line4.toString() + "\n");
			sb.append(line5.toString() + "\n");
			sb.append(line6.toString() + "\n");
			sb.append(" ------ ------ ------ ------ ------ ------ ------ \n");
		}

		return sb.toString();
	}

	protected void setCard(int row, int col, Card c) {
		// Muss ueberschrieben werden, daher zuerst entfernen und dann...
		this.getRow().get(row).getCol().remove(col);
		// ...hinzufuegen
		this.getRow().get(row).getCol().add(col, c);
	}

	public CardType getCard(int row, int col) {
		return this.getRow().get(row).getCol().get(col);
	}

	// Fuehrt nur das Hereinschieben der Karte aus!!!
	protected void proceedShift(MoveMessageType move) {
		Position sm = new Position(move.getShiftPosition());
		if (sm.getCol() % 6 == 0) { // Col=6 oder 0
			if (sm.getRow() % 2 == 1) {
				// horizontal schieben
				int row = sm.getRow();
				int start = (sm.getCol() + 6) % 12; // Karte die rausgenommen
													// wird
				setShiftCard(getCard(row, start));

				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(row, i, new Card(getCard(row, i - 1)));
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(row, i, new Card(getCard(row, i + 1)));
					}
				}
			}
		} else if (sm.getRow() % 6 == 0) {
			if (sm.getCol() % 2 == 1) {
				// vertikal schieben
				int col = sm.getCol();
				int start = (sm.getRow() + 6) % 12; // Karte die rausgenommen
													// wird
				setShiftCard(getCard(start, col));
				if (start == 6) {
					for (int i = 6; i > 0; --i) {
						setCard(i, col, new Card(getCard(i - 1, col)));
					}
				} else {// Start==0
					for (int i = 0; i < 6; ++i) {
						setCard(i, col, new Card(getCard(i + 1, col)));
					}
				}

			}
		}
		forbidden = sm.getOpposite();
		Card c = null;
		c = new Card(move.getShiftCard());
		// Wenn Spielfigur auf neuer shiftcard steht,
		// muss dieser wieder aufs Brett gesetzt werden
		// Dazu wird Sie auf die gerade hereingeschoben
		// Karte gesetzt
		if (!shiftCard.getPin().getPlayerID().isEmpty()) {
			// Figur zwischenspeichern
			Pin temp = shiftCard.getPin();
			// Figur auf SchiebeKarte löschen
			shiftCard.setPin(new Pin());
			// Zwischengespeicherte Figut auf
			// neuer Karte plazieren
			c.setPin(temp);
		}
		setCard(sm.getRow(), sm.getCol(), c);
	}

	// gibt zurueck ob mit dem Zug der aktuelle Schatz erreicht wurde
	public boolean proceedTurn(MoveMessageType move, Integer currPlayer) {
		// XXX ACHTUNG wird nicht mehr auf Richtigkeit überprüft!!!
		this.proceedShift(move);
		Position target = new Position(move.getNewPinPos());
		movePlayer(findPlayer(currPlayer), target, currPlayer);
		Card c = new Card(getCard(target.getRow(), target.getCol()));
		return (c.getTreasure() == currentTreasure);

	}

	protected void movePlayer(PositionType oldPos, PositionType newPos,
			Integer playerID) {
		getCard(oldPos.getRow(), oldPos.getCol()).getPin().getPlayerID()
				.remove(playerID);
		getCard(newPos.getRow(), newPos.getCol()).getPin().getPlayerID()
				.add(playerID);
	}

	protected Board fakeShift(MoveMessageType move) {
		Board fake = (Board) this.clone();
		fake.proceedShift(move);
		return fake;
	}

	@Override
	public Object clone() {
		Board clone = new Board();
		if (forbidden == null) {
			clone.forbidden = null;
		} else {
			clone.forbidden = new Position(this.forbidden);
		}
		clone.shiftCard = new Card(this.shiftCard);
		clone.currentTreasure = this.currentTreasure;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				clone.setCard(i, j, new Card(this.getCard(i, j)));
			}
		}
		return clone;
	}

	public boolean validateTransition(MoveMessageType move, Integer playerID) {
		// Ueberpruefen ob das Reinschieben der Karte gueltig ist

		Position sm = new Position(move.getShiftPosition());
		if (!sm.isLoosePosition() || sm.equals(forbidden)) {
			System.err.println("Warning: verbotene Position der Schiebekarte");
			return false;
		}
		Card sc = new Card(move.getShiftCard());
		if (!sc.equals(shiftCard)) {
			System.err.println("Warning: Schiebekarte wurde illegal verändert");
			return false;
		}
		// Ueberpruefen ob der Spielzug gueltig ist
		Board fake = this.fakeShift(move);
		Position playerPosition = new Position(fake.findPlayer(playerID));
		Debug.print("Spieler möchte von: (" + playerPosition.getRow() + ":"
				+ playerPosition.getCol() + ")", DebugLevel.VERBOSE);
		Debug.print("nach:               (" + move.getNewPinPos().getRow()
				+ ":" + move.getNewPinPos().getCol() + ")", DebugLevel.VERBOSE);
		Debug.print("Brett nach dem Schieben:", DebugLevel.VERBOSE);
		Debug.print(fake.toString(), DebugLevel.VERBOSE);
		if (fake.pathpossible(playerPosition, move.getNewPinPos())) {
			Debug.print("Zug gültig", DebugLevel.VERBOSE);
			return true;
		} else {
			Debug.print(
					"Warning: Die Angegebene Position ist nicht erreichbar",
					DebugLevel.DEFAULT);
			return false;
		}
	}

	public boolean pathpossible(PositionType oldPos, PositionType newPos) {
		if (oldPos == null || newPos == null)
			return false;
		Position oldP = new Position(oldPos);
		Position newP = new Position(newPos);
		return getAlleEreichbarenNachbarn(oldP).contains(newP);
	}

	protected List<PositionType> getAlleEreichbarenNachbarn(
			PositionType position) {
		List<PositionType> erreichbarePositionen = new ArrayList<PositionType>();
		int[][] erreichbar = new int[7][7];
		erreichbar[position.getRow()][position.getCol()] = 1;
		erreichbar = getAlleErreichbarenNachbarnMatrix(position, erreichbar);
		for (int i = 0; i < erreichbar.length; i++) {
			for (int j = 0; j < erreichbar[0].length; j++) {
				if (erreichbar[i][j] == 1) {
					erreichbarePositionen.add(new Position(i, j));
				}
			}
		}
		return erreichbarePositionen;
	}

	private int[][] getAlleErreichbarenNachbarnMatrix(PositionType position,
			int[][] erreichbar) {
		for (PositionType p1 : getDirektErreichbareNachbarn(position)) {
			if (erreichbar[p1.getRow()][p1.getCol()] == 0) {
				erreichbar[p1.getRow()][p1.getCol()] = 1;
				getAlleErreichbarenNachbarnMatrix(p1, erreichbar);
			}
		}
		return erreichbar;
	}

	private List<PositionType> getDirektErreichbareNachbarn(
			PositionType position) {
		List<PositionType> positionen = new ArrayList<PositionType>();
		CardType k = this.getCard(position.getRow(), position.getCol());
		Openings openings = k.getOpenings();
		if (openings.isLeft()) {
			if (position.getCol() - 1 >= 0
					&& getCard(position.getRow(), position.getCol() - 1)
							.getOpenings().isRight()) {
				positionen.add(new Position(position.getRow(), position
						.getCol() - 1));
			}
		}
		if (openings.isTop()) {
			if (position.getRow() - 1 >= 0
					&& getCard(position.getRow() - 1, position.getCol())
							.getOpenings().isBottom()) {
				positionen.add(new Position(position.getRow() - 1, position
						.getCol()));
			}
		}
		if (openings.isRight()) {
			if (position.getCol() + 1 <= 6
					&& getCard(position.getRow(), position.getCol() + 1)
							.getOpenings().isLeft()) {
				positionen.add(new Position(position.getRow(), position
						.getCol() + 1));
			}
		}
		if (openings.isBottom()) {
			if (position.getRow() + 1 <= 6
					&& getCard(position.getRow() + 1, position.getCol())
							.getOpenings().isTop()) {
				positionen.add(new Position(position.getRow() + 1, position
						.getCol()));
			}
		}
		return positionen;
	}

	public PositionType findPlayer(Integer PlayerID) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				Pin pinsOnCard = getCard(i, j).getPin();
				for (Integer pin : pinsOnCard.getPlayerID()) {
					if (pin == PlayerID) {
						PositionType pos = new PositionType();
						pos.setCol(j);
						pos.setRow(i);
						return pos;
					}
				}
			}
		}
		// Pin nicht gefunden
		return null;
	}

	public void setTreasure(TreasureType t) {
		currentTreasure = t;
	}

	public TreasureType getTreasure() {
		return currentTreasure;
	}
}
