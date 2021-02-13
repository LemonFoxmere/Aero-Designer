package com.source.excenv.model.shape;

public class vStabalizer extends rotatableParallelogram {

	public float wingSpan, chord, sweep;
	
	public vStabalizer(float x, float y, float wingSpan, float chord, float sweep) {
		super(x, y, wingSpan, chord, true);
		this.wingSpan = wingSpan;
		this.chord = chord;
		this.sweep = sweep;
	}

	public void updateWingSpan(float f) {
		this.wingSpan = f;
		super.updateLength(f);
	}public void updateChord(float f) {
		this.chord = f;
		super.updateWidth(f);
	}public void updateSweep(float f) {
		this.sweep = f;
		super.updateRot(f);
	}
	
}
