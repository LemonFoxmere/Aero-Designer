package com.source.excenv.model.button;

public interface classicButton {
	//when mouse hover over
	public abstract void onHover();
	//when mouse lose focus
	public abstract void onLostFocus();
	//when pressed but not released
	public abstract void pressed();
	//when released
	public abstract void released();
}
