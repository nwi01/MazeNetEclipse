package de.mazenet.client;

import generated.LoginMessageType;
import generated.MazeCom;
import generated.MazeComType;
import generated.ObjectFactory;

import java.io.IOException;
import java.net.Socket;

import networking.XmlInStream;
import networking.XmlOutStream;

public class MazeNetworkConnector {

    private final int port = 5123;
    private final String host = "localhost";
    private Socket socket;
    private XmlInStream inStream;
    private XmlOutStream outStream;
    private ObjectFactory factory = new ObjectFactory();
    public static int id;

    public boolean open() throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.inStream = new XmlInStream(socket.getInputStream());
            this.outStream = new XmlOutStream(socket.getOutputStream());

            MazeCom mazeCom = factory.createMazeCom();
            mazeCom.setMcType(MazeComType.LOGIN);

            LoginMessageType type = new LoginMessageType();
            type.setName("Niels & Mike");
            mazeCom.setLoginMessage(type);
            
            return sendMessage(mazeCom);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Senden des MazeCom Objektes
     *
     * @param mazeCom
     * @return
     */
    public boolean sendMessage(MazeCom mazeCom) {
        try {
            //Senden
        	mazeCom.setId(id);
            outStream.write(mazeCom);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Warten auf Antwort.
     *
     * @return
     */
    public MazeCom waitMessage() 
    {
        try 
        {
            //Warten auf Server
            while (true) 
            {
                MazeCom answerCom = inStream.readMazeCom();
                if (answerCom != null) 
                {
                    return answerCom;
                }
                Thread.sleep(500);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Setzen der Spieler-Id
     * @param id
     */
    public void setId(int id)
    {
    	this.id = id;
    }
}
