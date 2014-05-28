package de.mazenet.client;

import generated.*;

public abstract class KiPlayer 
{
	private int id;
	protected String name = "4711";
	
	abstract MazeCom getNextMove(MazeCom mazeCom);
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}
