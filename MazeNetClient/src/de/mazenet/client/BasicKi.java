package de.mazenet.client;

import java.util.List;

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
import generated.TreasureType;

public class BasicKi extends KiPlayer {
	private ObjectFactory factory;
	private MoveMessageType nextMoveMessageType;
	private MoveMessageType oldMoveMessageType;

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
		TreasureType treasureType = move.getAwaitMoveMessage().getTreasure();

		Openings openings = boardType.getShiftCard().getOpenings();
		Openings openCopy = Util.copyOpenings(openings);
		
		List<Position> positions = Position.getPossiblePositionsForShiftcard();
		for (Position position : positions) {
			for (int i = 0; i < 4; i++) {
				openings.setBottom(openCopy.isLeft());
				openings.setLeft(openCopy.isTop());
				openings.setTop(openCopy.isRight());
				openings.setRight(openCopy.isBottom());

				if (lastShiftPositionType != null
						&& lastShiftPositionType.getCol() == position.getCol()
						&& lastShiftPositionType.getRow() == position.getRow()) {
					continue;
				}
				nextMoveMessageType = simulateMove(boardType, openings, position,
						treasureType);
			}
		}
		
		if(oldMoveMessageType != null && Util.isEqualPinPosition(oldMoveMessageType.getNewPinPos(), nextMoveMessageType.getNewPinPos()))
		{
			de.mazenet.client.Board newBoard = new de.mazenet.client.Board(boardType);
			
			PositionType ownPosition = newBoard.findPlayer(MazeNetworkConnector.id);
			List<PositionType> allReachablePositions = newBoard.getAllReachablePositions(ownPosition);
						
			nextMoveMessageType.setNewPinPos(allReachablePositions.get(0));
			nextMoveMessageType.setShiftPosition(Position.getPossiblePositionsForShiftcard().get(Position.getPossiblePositionsForShiftcard().size() * (int)Math.random()));
		}
		
		oldMoveMessageType =  nextMoveMessageType;
		nextMove.setMoveMessage(nextMoveMessageType);
		
		return nextMove;
	}

	private MoveMessageType simulateMove(BoardType boardType,
			Openings openings, Position position, TreasureType treasure) {
		// neues Board durch einschub der Karte
		MoveMessageType newMoveMessageType = factory.createMoveMessageType();
		CardType cardType = boardType.getShiftCard();
		cardType.setOpenings(openings);		
		newMoveMessageType.setShiftCard(cardType);
		newMoveMessageType.setShiftPosition(position);

		de.mazenet.client.Board newBoard = new de.mazenet.client.Board(
				boardType);
		newBoard = newBoard.fakeShift(newMoveMessageType);
		
		PositionType treasurePos = newBoard.findTreasure(treasure);
		
		if(treasurePos == null)
		{
			// alter Move
			return nextMoveMessageType;
		}

		// dann simulieren wie der Pin gezogen werden sollte.
		PositionType newPinPos = simulatePinMove(newBoard, treasurePos);
		
		newMoveMessageType.setNewPinPos(newPinPos);

		return compare(nextMoveMessageType, newMoveMessageType, treasurePos);
	}

	private PositionType simulatePinMove(de.mazenet.client.Board board, PositionType treasure) {
		PositionType ownPosition = board.findPlayer(MazeNetworkConnector.id);
		List<PositionType> allReachablePositions = board.getAllReachablePositions(ownPosition);
		
		PositionType ergebnis = allReachablePositions.get(0);
		
		if (allReachablePositions.contains(treasure))
		{
			return treasure;
		}
				
		for( int i = 1; i < allReachablePositions.size(); i++)
		{	
			ergebnis = compare(ergebnis, allReachablePositions.get(i), treasure);
		}
		
		return ergebnis;
	}
	
	private PositionType compare(PositionType oldPositionType, PositionType newPositionType, PositionType treasureType){
		if(oldPositionType == null)
		{
			return newPositionType;
		}
		
		int abstandOld = Math.abs(oldPositionType.getCol() - treasureType.getCol()) + Math.abs(oldPositionType.getRow() - treasureType.getRow());
		int abstandNew = Math.abs(newPositionType.getCol() - treasureType.getCol()) + Math.abs(newPositionType.getRow() - treasureType.getRow());
		
		if (abstandNew <= abstandOld) 
		{
			return newPositionType;
		} 
		else 
		{
			return oldPositionType;
		}
	}

	private MoveMessageType compare(MoveMessageType oldMoveMessage,
			MoveMessageType newMoveMessage, PositionType treasureType) 
	{
		if(oldMoveMessage == null)
		{
			return newMoveMessage;
		}
		
		int abstandOld = Math.abs(oldMoveMessage.getNewPinPos().getCol() - treasureType.getCol()) + Math.abs(oldMoveMessage.getNewPinPos().getRow() - treasureType.getRow());
		int abstandNew = Math.abs(newMoveMessage.getNewPinPos().getCol() - treasureType.getCol()) + Math.abs(newMoveMessage.getNewPinPos().getRow() - treasureType.getRow());
		
		if (abstandNew <= abstandOld) 
		{
			return newMoveMessage;
		} 
		else 
		{
			return oldMoveMessage;
		}
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
