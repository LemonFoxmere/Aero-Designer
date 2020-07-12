package com.source.excEnv.model.shape;

public class rotatableParallelogram{

	public float rotation = 0;
	public float x, x1, y, y1;
	public float r = 0;
	public float w;
	public final boolean verticle;
	
	public rotatableParallelogram(float x, float y, float length, float width, boolean verticle) {
		if (!verticle) {			
			this.r = length;
			this.w = width;
			this.x = x;
			this.y = y;
			this.x1 = x + r;
			this.y1 = y;
		} else {			
			this.r = length;
			this.w = width;
			this.x = x;
			this.y = y;
			this.x1 = x;
			this.y1 = y + r;
		}
		this.verticle = verticle;
	}
	
	public void updateX(int x) {
		this.x  = x;
	}public void updateY(int y) {
		this.y  = y;
	}public void updateLength(float len) {
		this.r = len;
		updatePos();
	}public void updateWidth(float wid) {
		this.w = wid;
	}public void updateRot(float f) {
		rotation = f;
		updatePos();
	}
	
	private void updatePos() {
		if(!verticle) {			
			x1 = (float) Math.cos(Math.toRadians(rotation)) * r + x;
			y1 = (float) Math.sin(Math.toRadians(rotation)) * r + y;
			return;
		}
		x1 = (float) Math.sin(Math.toRadians(rotation)) * r + x;
		y1 = (float) Math.cos(Math.toRadians(rotation)) * r + y;
	}
}
