package de.mazenet.client;

import generated.*;

public abstract class KiPlayer 
{
	private int id;
	protected String name = "4711";
	
	abstract MoveMessageType getNextMove(MoveMessageType move);
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}
