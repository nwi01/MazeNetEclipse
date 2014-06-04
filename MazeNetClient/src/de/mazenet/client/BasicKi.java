package de.mazenet.client;

import de.mazenet.util.Util;
import server.Board;
import generated.BoardType;
import generated.CardType;
import generated.CardType.Openings;
import generated.MazeCom;
import generated.MazeComType;
import generated.MoveMessageType;
import generated.ObjectFactory;
import generated.PositionType;

public class BasicKi extends KiPlayer {
	private ObjectFactory factory;

	public BasicKi() {
		setName("BasicKi");
		factory = new ObjectFactory();
	}

	@Override
	MazeCom getNextMove(MazeCom move) {
		MazeCom nextMove = factory.createMazeCom();
		nextMove.setMcType(MazeComType.MOVE);
		MoveMessageType messageType = factory.createMoveMessageType();

		BoardType boardType = move.getAwaitMoveMessage().getBoard();
		PositionType oldPositionType = Util.findPlayer(MazeNetworkConnector.id,
				boardType);
		PositionType lastShiftPositionType = move.getAwaitMoveMessage()
				.getBoard().getForbidden();
		
		Openings openings = boardType.getShiftCard().getOpenings();
		Openings openCopy = Util.copyOpenings(openings);
		for (int i = 0; i < 4; i++) {
				openings.setBottom(openCopy.isLeft());
			    openings.setLeft(openCopy.isTop());
				openings.setTop(openCopy.isRight());
				openings.setRight(openCopy.isBottom());
			
			for (int j = 0; j < 4; j++) {
				for (int l = 0; l < 4; l++) {
					if (lastShiftPositionType != null
							&& lastShiftPositionType.getCol() == j
							&& lastShiftPositionType.getRow() == l) {
						continue;
					}
					simulateMove(boardType, openings, j, l);
				}
			}
		}

		return nextMove;
	}

	private void simulateMove(BoardType boardType, Openings openings, int i, int j){
		
	}
	
	

	// @Override
	// MazeCom getNextMove(MazeCom move)
	// {
	// MazeCom nextMove = factory.createMazeCom();
	// MoveMessageType messageType = factory.createMoveMessageType();
	//
	// CardType card = move.getAwaitMoveMessage().getBoard().getShiftCard();
	//
	// messageType.setShiftCard(card);
	//
	// PositionType position = factory.createPositionType();
	// position.setCol(0);
	// position.setRow(1);
	// messageType.setShiftPosition(position);
	//
	// PositionType positionPin = factory.createPositionType();
	// positionPin.setCol(0);
	// positionPin.setRow(0);
	// messageType.setNewPinPos(positionPin);
	//
	// nextMove.setMoveMessage(messageType);
	// nextMove.setMcType(MazeComType.MOVE);
	//
	// return nextMove;
	// }

}
