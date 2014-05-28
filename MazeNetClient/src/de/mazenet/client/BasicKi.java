package de.mazenet.client;

import generated.CardType;
import generated.CardType.Openings;
import generated.MazeCom;
import generated.MoveMessageType;
import generated.ObjectFactory;
import generated.PositionType;

public class BasicKi extends KiPlayer 
{
	private ObjectFactory factory;
	
	public BasicKi()
	{
		setName("BasicKi");
		factory = new ObjectFactory();
	}
	
	@Override
	MazeCom getNextMove(MazeCom move) 
	{
		MazeCom nextMove = factory.createMazeCom();
		MoveMessageType messageType = factory.createMoveMessageType();
				
		CardType card = move.getAwaitMoveMessage().getBoard().getShiftCard();
		/*Openings value = factory.createCardTypeOpenings();
		value.setLeft(true);
		value.setTop(true);
		
		card.setOpenings(value);*/
		
		messageType.setShiftCard(card);
		
		PositionType position = factory.createPositionType();
		position.setCol(0);
		position.setRow(1);			
		messageType.setShiftPosition(position);
		
		PositionType positionPin = factory.createPositionType();
		positionPin.setCol(0);
		positionPin.setRow(0);
		messageType.setNewPinPos(positionPin);
		
		
		nextMove.setMoveMessage(messageType);
		
		
		
		
		return nextMove;
	}
	
}
