package com.source.excEnv.model.button;

public interface classicButton {
	//when mouse hover over
	public abstract void onHover();
	//when mouse lose focus
	public abstract void onLostFocus();
	//when clicked
	public abstract void action();
	//when pressed but not released
	public abstract void pressed();
	//when released
	public abstract void released();
}
