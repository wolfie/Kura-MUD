package com.github.wolfie.kuramud.server;

public enum Direction {
	NORTH, EAST, SOUTH, WEST, UP, DOWN;
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
