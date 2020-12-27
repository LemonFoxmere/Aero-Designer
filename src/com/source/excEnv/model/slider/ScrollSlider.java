package com.source.excEnv.model.slider;

import java.awt.Color;
import java.awt.Dimension;

public class ScrollSlider {
	public int x, y, l, w, currentState;
	public int Sx, Sy, Sl, Sw; // this is the slider positions
	@SuppressWarnings("unused")
	private final int Cx, Cy, Cl, Cw, CSx, CSy, CSl, CSw;

	public final int possibleStats;
	public final Dimension boundingBox;
	public Color bgColor = new Color(0, 0, 0), fgColor; // fgColor = 30, 144, 216
	
	public ScrollSlider(int x, int y, int w, int l, int possibleStats, int defaultStat) {
		this.possibleStats = possibleStats;
		this.currentState = defaultStat;
		this.fgColor = new Color(120, 120, 120);
		this.x = this.Cx = x;
		this.y = this.Cy = y;
		this.l = this.Cl = l;
		this.w = this.Cw = w;
		
		//init slider positions
		Sx = this.CSx  = x;
		Sw = this.CSw  = w;
		Sl = this.CSl  = (int) (l/possibleStats);
		Sy = this.CSy  = (defaultStat != (possibleStats-1)) ? y + defaultStat * Sl : y+l-Sl; 
		
		boundingBox = new Dimension(w, l);
	}
	
	public void onHover() {
		while(w < CSw+12) {
			w+=2;
			l+=2;
			x--;
			y--;

			Sw+=2;
			Sl+=2;
			Sx--;
			Sy--;
			fgColor = new Color(fgColor.getRed() + 18, fgColor.getGreen() -8, fgColor.getBlue() - 10);
			try {
				Thread.sleep(11);
			} catch (InterruptedException e) {
				System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
				System.err.println("---");
				e.printStackTrace();
			}
		}
	}
	
	public void onNormal() {
		while (w > CSw) {
			w-=2;
			l-=2;
			x++;
			y++;

			Sw-=2;
			Sl-=2;
			Sx++;
			Sy++;
			fgColor = new Color(fgColor.getRed() - 18, fgColor.getGreen() + 8, fgColor.getBlue() + 10);
			try {
				Thread.sleep(11);
			} catch (InterruptedException e) {
				System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
				System.err.println("---");
				e.printStackTrace();
			}
		}
	}
	
	public boolean isInBound(int x, int y) {
		return (x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.l) ? true : false;
	}
	
	public void changeMode(int newMode) {
		if (newMode > possibleStats-1 && newMode < 0) { //idiot proof safe
			return;
		}
		
		// we first change the current stat to the new Mode stat
		currentState = newMode;
		
		// we then calculate the new position of the slider
		final float newSliderY = (currentState != (possibleStats-1)) ? y + l*(currentState/(float)(possibleStats-1))-Sl*(currentState/(float)(possibleStats-1)): y+l-Sl; // prevention of overshoot
		
		// we can now update the slider to go there
		if (newSliderY < Sy) {			
			while (Sy >= newSliderY) {
				Sy-=2;
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
					System.err.println("---");
					e.printStackTrace();
				}
			}
			Sy = (int) newSliderY;
		} else if (newSliderY > Sy) {
			while (Sy <= newSliderY) {
				Sy+=2;
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
					System.err.println("---");
					e.printStackTrace();
				}
			}
			Sy = (int) newSliderY;
		} else {
			return;
		}
	}
}
