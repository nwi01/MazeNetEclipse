package config;

import java.util.Locale;
import server.userInterface.*;

public class Settings {
	private Settings() {
	}

	public final static int PORT = 5123;
	/**
	 * Startwert fuer die Spieleranzahl Kann aber noch veraendert werden,
	 * deshalb nicht final
	 */
	public static int DEFAULT_PLAYERS = 1;
	/**
	 * Die Zeit in Milisekunden, die die Animation eines Zug (die Bewegung des Pins) benoetigen soll
	 */
	public final static int MOVEDELAY = 1000;
	/**
	 * Die Zeit in Milisekunden, die das Einschieben der Shiftcard dauern soll
	 */
	public final static int SHIFTDELAY = 1000;
	/**
	 * Die Zeit in Milisekunden, nach der ein Logintimeout eintritt LOGINTIMEOUT
	 * = 60000 entspricht einer Minute
	 */
	public final static long LOGINTIMEOUT = 60000;
	public final static int LOGINTRIES = 3;
	/**
	 * Die maximale Anzahl der Versuche einen gueltigen Zug zu uebermitteln
	 */
	public final static int MOVETRIES = 3;
	public final static long SENDTIMEOUT = 1 * 60 * 1000;
	/**
	 * Wenn TESTBOARD = true ist, dann ist das Spielbrett bei jedem Start
	 * identisch (zum Debugging)
	 */
	public final static boolean TESTBOARD = true;
	/**
	 * Hiermit lassen sich die Testfaelle anpassen (Pseudozufallszahlen)
	 */
	public final static long TESTBOARD_SEED = 0;
	/**
	 * USERINTERFACE definiert die zu verwendende GUI Gueltige Werte:
	 * BetterUI(), GraphicalUI()
	 */
	public final static UI USERINTERFACE = new BetterUI();
	public final static String IMAGEPATH = "/server/userInterface/resources/";
	public final static String IMAGEFILEEXTENSION = ".png";
	public final static Locale LOCALE = new Locale("de");
}
