package com.source.excEnv.model.shape;

import java.awt.Color;

public class quadShape{

	public float rotation = 0;
	public int x, y, w, h;
	public int rotCenterX, rotCenterY;
	
	public quadShape(int x, int y, int w, int h, float rotation) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.rotation = rotation;
		rotCenterX = (2*x + w) / 2;
		rotCenterY = (2*y + h) / 2;
	}
	
	public void updateX(int x) {
		this.x  = x;
	}public void updateY(int y) {
		this.y  = y;
	}public void updateW(int w) {
		this.w  = w;
	}public void updateH(int h) {
		this.h  = h;
	}public void updateRot(int rot) {
		rotation = rot;
	}public void updateRotPos(int x, int y) {
		rotCenterX = x;
		rotCenterX = y;
	}
}
