package de.mazenet.client;

import generated.MoveMessageType;

public class BasicKi extends KiPlayer 
{
	public BasicKi()
	{
		setName("BasicKi");
	}
	
	@Override
	MoveMessageType getNextMove(MoveMessageType move) 
	{
		return null;
	}
	
}
