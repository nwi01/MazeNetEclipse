package de.mazenet.util;

import generated.BoardType;
import generated.CardType;
import generated.CardType.Openings;
import generated.ObjectFactory;
import generated.PositionType;
import generated.CardType.Pin;

public class Util {
	
	public static PositionType findPlayer(Integer PlayerID, BoardType board) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				Pin pinsOnCard = Util.getCard(i, j, board).getPin();
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
	
	public static CardType getCard(int row, int col, BoardType board) {
		return board.getRow().get(row).getCol().get(col);
	}
	
	public static Openings copyOpenings(Openings openings){
		ObjectFactory of = new ObjectFactory();
		Openings newOp = of.createCardTypeOpenings();
		newOp.setBottom(openings.isBottom());
		newOp.setRight(openings.isRight());
		newOp.setTop(openings.isTop());
		newOp.setLeft(openings.isLeft());
		return newOp;
	}

}
