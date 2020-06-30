package com.source.excEnv.model.slider;

import java.awt.Color;
import java.awt.Dimension;

public class StandardSlider {
	
	public int posX, posY, length, defaultVal, minVal, maxVal, buttonX, buttonY, offset = 0, buttonRad, id;
	public float currentVal;
	public Dimension boundingBox;
	public boolean isDragged = false;
	public Color buttonColor;
	
	public String description;
	
	public StandardSlider(int posX, int posY, int length, int buttonRad, int defaultVal, int minVal, int maxVal, String description, int id) {
		
		if (defaultVal > maxVal || defaultVal < minVal) {
			throw new IllegalArgumentException("default Value cannot exceed maxVal or subceed minVal");
		}
		
		this.description = description;
		
		this.posX = posX;
		this.posY = posY;
		this.length = length;
		this.buttonRad = buttonRad;
		this.defaultVal = defaultVal;
		this.currentVal = defaultVal;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.id = id;
		
		buttonX = (int) (posX + (float) (Math.abs(minVal) + currentVal) / (maxVal - minVal) * length - buttonRad);
		buttonY = (int) (posY - buttonRad);
		
		buttonColor = new Color(0, 0, 0);
		
		boundingBox = new Dimension((int)(buttonRad), (int)(buttonRad));
	}
	
	public void updateOffset(int offset) {
		this.offset = offset;
	}
	
	public String getCurrentVal() {
		return String.valueOf(roundValueToSecondPlace(currentVal));
	}
	
	public Color getColor() {
		return buttonColor;
	}
	
	public float getCurrentValNum() {
		return currentVal;
	}
	
	public void offsetVal(float amt) {
		if (currentVal > minVal && currentVal < maxVal) {
			currentVal = (currentVal += amt);
			updateButtonPosition(currentVal);
			return;
		}
	}
	
	private float roundValueToSecondPlace(float value) {
		return Math.round(value * 100f) / 100f;
	}
	
	private void updateButtonPosition(float value) {
		buttonX = (int) (posX + (float) (Math.abs(minVal) + currentVal) / (maxVal - minVal) * length - buttonRad);
	}
	
	public void updateValue(int x) {
		if (x >= posX - buttonRad - this.offset && x <= (posX + length)){			
			buttonX = x + this.offset;
			currentVal = (((buttonX + buttonRad) - posX) / (float)(length) * (maxVal - minVal)) + minVal;
			return;
		}
		if (x < posX - buttonRad) {
			buttonX = posX - buttonRad;
			currentVal = minVal;
			return;
		} else if (x > posX + length) {
			buttonX = posX + length - buttonRad;
			currentVal = maxVal;
			return;
		}
	}
	
	public boolean isInBoundingBox(int x, int y) {
		if (x > buttonX && x < buttonX + (buttonRad * 2) && y > buttonY && y < buttonY + (buttonRad * 2)) {
			return true;
		}
		return false;
	}
	
	public void updateToHoverState() {
		while(buttonRad != 12f) {			
			buttonRad++;
			buttonX--;
			buttonY--;
			buttonColor = new Color(buttonColor.getRed() + 51, buttonColor.getGreen() + 15, buttonColor.getBlue() + 15);
			try {
				Thread.sleep(14);
			} catch (InterruptedException e) {
				System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
				System.err.println("---");
				System.err.println("line 65-80 > com.source.excEnv.model.slider.StandardSlider: unexpected runtime error");
				e.printStackTrace();
			}
		}
	}
	
	public void updateToNormalState() {
		while(buttonRad != 7f) {			
			buttonRad--;
			buttonX++;
			buttonY++;
			buttonColor = new Color(buttonColor.getRed() - 51, buttonColor.getGreen() - 15, buttonColor.getBlue() - 15);
			try {
				Thread.sleep(14);
			} catch (InterruptedException e) {
				System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
				System.err.println("---");
				System.err.println("line 82-96 > com.source.excEnv.model.slider.StandardSlider: unexpected runtime error");
				e.printStackTrace();
			}
		}
	}
}
