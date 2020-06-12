package com.source.excEnv.model.player;

public abstract class sideway_player {
	
	//update
	public abstract void update();
	public abstract void updateRect();
	
	//standard movement
	public abstract void moveLeft();
	public abstract void moveRight();
	public abstract void jump();
}
