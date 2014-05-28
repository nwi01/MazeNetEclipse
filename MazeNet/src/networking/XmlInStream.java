package networking;

import generated.MazeCom;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import tools.Debug;
import tools.DebugLevel;

public class XmlInStream extends UTFInputStream {

	private Unmarshaller unmarshaller;

	public XmlInStream(InputStream in) {
		super(in);
		try {
			JAXBContext jc = JAXBContext.newInstance(MazeCom.class);
			this.unmarshaller = jc.createUnmarshaller();
		} catch (JAXBException e) {
			Debug.print(
					"[ERROR]: Fehler beim initialisieren der JAXB-Komponenten",
					DebugLevel.DEFAULT);
		}
	}

	/**
	 * Liest eine Nachricht und gibt die entsprechende Instanz zurueck
	 * 
	 * @return
	 */
	public MazeCom readMazeCom() throws EOFException, SocketException {
		byte[] bytes = null;
		MazeCom result = null;
		try {
			String xml = this.readUTF8();
			Debug.print("Empfangen", DebugLevel.DEBUG);
			Debug.print(xml, DebugLevel.DEBUG);
			bytes = xml.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

			result = (MazeCom) this.unmarshaller.unmarshal(bais);
		} catch (JAXBException e) {
			e.printStackTrace();
			Debug.print("[ERROR]: Fehler beim unmarshallen der Nachricht",
					DebugLevel.DEFAULT);
		} catch (IOException e1) {
			//weiterleiten der Exception => damit Spieler korrekt entfernt wird
			if (e1 instanceof SocketException)
				throw new SocketException();
			else {
				e1.printStackTrace();
				Debug.print("[ERROR]: Fehler beim lesen der Nachricht",
						DebugLevel.DEFAULT);
			}
		} catch (NullPointerException e) {
			Debug.print(
					"[ERROR]: Nullpointer beim lesen der Nachricht aufgrund weiterer Fehler",
					DebugLevel.DEFAULT);
		}
		return result;
	}

}