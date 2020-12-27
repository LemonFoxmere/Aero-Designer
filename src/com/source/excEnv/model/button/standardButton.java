package com.source.excEnv.model.button;

import java.awt.Color;

public abstract class standardButton implements classicButton{

	public int x, y, w, h;
	public final int wf, hf;
	public Color buttonColor;
	public final String message;
	
	public standardButton(int x, int y, int w, int h, String message) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		wf = w;
		hf = h;
		this.message = message;
		buttonColor = new Color(0, 0, 0);
	}
	
	@Override
	public void onHover() {
		while(w < wf+11) {
			w+=2;
			h+=2;
			x--;
			y--;
			buttonColor = new Color(buttonColor.getRed() + 5, buttonColor.getGreen() + 24, buttonColor.getBlue() + 36);
			try {
				Thread.sleep(14);
			} catch (InterruptedException e) {
				System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
				System.err.println("---");
				System.err.println("line 15-31 > com.source.excEnv.model.button.classicButton: unexpected runtime error");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onLostFocus() {
		while(w > wf) {
			w-=2;
			h-=2;
			x++;
			y++;
			buttonColor = new Color(buttonColor.getRed() - 5, buttonColor.getGreen() - 24, buttonColor.getBlue() - 36);
			try {
				Thread.sleep(14);
			} catch (InterruptedException e) {
				System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
				System.err.println("---");
				System.err.println("line 38-54 > com.source.excEnv.model.button.classicButton: unexpected runtime error");
				e.printStackTrace();
			}
		}		
	}
	
	@Override
	public void pressed() {
		while(w > wf+5) {
			w-=2;
			h-=2;
			x++;
			y++;
			buttonColor = new Color(buttonColor.getRed() - 5, buttonColor.getGreen() - 5, buttonColor.getBlue() - 5);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
				System.err.println("---");
				System.err.println("line 61-78 > com.source.excEnv.model.button.classicButton: unexpected runtime error");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void released() {
		while(w < wf+11) {
			w+=2;
			h+=2;
			x--;
			y--;
			buttonColor = new Color(buttonColor.getRed() + 5, buttonColor.getGreen() + 5, buttonColor.getBlue() + 5);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
				System.err.println("---");
				System.err.println("line 61-78 > com.source.excEnv.model.button.classicButton: unexpected runtime error");
				e.printStackTrace();
			}
		}
	}
	
	public boolean inBound(int x, int y) {
		return (x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.h) ? true:false;
	}
}
