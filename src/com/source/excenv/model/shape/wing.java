package com.source.excenv.model.shape;

public class wing extends rotatableParallelogram {

	public float semiSpan, sweep, chord, dihedral, aoa;
	
	public wing(int x, int y, int aoa, int semiSpan, int dihedral, int sweep, int chord) {
		super(x, y, semiSpan, chord, false);
		this.semiSpan = semiSpan;
		this.aoa = aoa;
		this.dihedral = dihedral;
		this.sweep = sweep;
		this.chord = chord;
	}
	
	public void updateSemiSpan(float f) {
		this.semiSpan = f;
		super.updateLength(f);
	}public void updateChord(float f) {
		this.chord = f;
		super.updateWidth(f);
	}public void updateSweep(float f) {
		this.sweep = f;
		super.updateRot(f);
	}public void updateAoa(float f) {
		this.aoa = f;
	}public void updateDiheral(float f) {
		this.dihedral = f;
	}
}
