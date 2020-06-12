package com.source.excEnv.model.button;

import com.source.framework.util.ExportAircraft;

public class exportButton extends standardButton{
	
	public exportButton(int x, int y, int w, int h, String message) {
		super(x, y, w, h, message);
	}

	@Override
	public void action() {
		ExportAircraft.exportDefaultParam();
	}
}
