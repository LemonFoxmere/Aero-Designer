package com.source.excEnv.model.bots;

import com.source.excEnv.model.player.sideway_player;

public abstract class bot extends sideway_player {
	//the update is where the bot do its calculations
	public abstract void update();
	public abstract void updateRect();
	
	//moveMent, same as player
	@Override
	public abstract void moveLeft();
	@Override
	public abstract void moveRight();
	@Override
	public abstract void jump();
}
