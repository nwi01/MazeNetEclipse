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
                    case ACCEPT:break;
                    case AWAITMOVE:break;
                    case DISCONNECT:break;
                    case LOGINREPLY: {
                        System.out.println("efefef");
                        break;
                    }
                    case MOVE:break;
                    case WIN: break;
                }
            }

        }

    }

}
