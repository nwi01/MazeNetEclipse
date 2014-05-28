package server;

import generated.ErrorType;
import generated.TreasureType;

import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Stack;

import networking.Connection;

public class Player {
	final int ID;
	private String name;
	private TreasureType currentTreasure;
	private Stack<TreasureType> treasures;
	private Connection conToClient;
	private boolean initialized;

	/*
	 * Player darf nicht selber generiert werden nur vom Login erzeugt!
	 */
	public Player(int id, Connection c) {
		ID = id;
		this.name = "Player0" + ID;
		conToClient = c;
		currentTreasure = null;
		initialized = false;
		treasures = new Stack<TreasureType>();
		// Hinzufuegen des Starts als letzter zu holender Schatz
		// z.B.: TreasureType.START_01
		treasures.push(TreasureType.fromValue("Start0" + this.ID));
	}

	public TreasureType getCurrentTreasure() {
		return currentTreasure;
	}

	// returns remaing treasures
	public int foundTreasure() {
		try {
			currentTreasure = treasures.pop();
		} catch (EmptyStackException e) {
			return 0;
		}
		return treasures.size() + 1;

	}

	public int treasuresToGo() {
		return treasures.size() + ( (currentTreasure!=null)?1:0 );
	}

	public void setTreasure(Collection<? extends TreasureType> c) {
		this.treasures.addAll(c);
		this.currentTreasure=treasures.pop();
	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public Connection getConToClient() {
		return conToClient;
	}

	public void disconnect(ErrorType et) {
		conToClient.disconnect(et);
	}

	public void init(String name) {
		if (!initialized) {
			this.name = name;
			initialized = true;
		}
	}

	public boolean isInitialized() {
		return initialized;
	}

}
