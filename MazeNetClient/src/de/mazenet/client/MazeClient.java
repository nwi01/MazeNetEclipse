package de.mazenet.client;

import generated.*;

import java.io.IOException;

public class MazeClient {
	// Member
    //MazeNet
    private static MoveMessageType lastMove;

    //Netzwerk
    private static MazeNetworkConnector connection;

    /**
     * @param args Konstruktor
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        boolean win = false;
        connection = new MazeNetworkConnector();

        // Ki anlegen
        KiPlayer player = new BasicKi();
        if (connection.open()) {
            while (!win) {
                MazeCom mazeCom = connection.waitMessage();
                switch (mazeCom.getMcType()) {
                    case ACCEPT:
                    	AcceptMessageType acceptMessage = mazeCom.getAcceptMessage();
                    	if(acceptMessage.isAccept())
                    	{
                    		System.out.println("Zug akzeptiert.");	
                    	}
                    	else
                    	{
                    		System.out.println("Zug nicht akzeptiert.");
                    		System.out.println(acceptMessage.getErrorCode());
                    		
                    	}                    	
                    	break;
                    case AWAITMOVE:
                    	MazeCom newMove = player.getNextMove(mazeCom);
                    	connection.sendMessage(newMove);
                    	break;
                    case DISCONNECT:
                    	System.out.println("Verbindung beendet.");
                    	break;
                    case LOGINREPLY: {
                    	connection.setId(mazeCom.getId());
                        System.out.println("Am Server angemeldet.");
                        break;
                    }
                    case WIN:
                    	System.out.println("GEWONNEN.");
                    	connection.close();
                    	System.exit(0);
                    	break;
                }
                Thread.sleep(2000);
            }

        }

    }

}
