package com.source.excenv.model.button;

import com.source.excenv.model.shape.vStabalizer;
import com.source.excenv.model.shape.wing;
import com.source.framework.util.ExportAircraft;

public class exportButton extends standardButton{
	
	public exportButton(int x, int y, int w, int h, String message) {
		super(x, y, w, h, message);
	}

	public void action(wing mainWing, wing hStab, vStabalizer vStab) {
		ExportAircraft.exportAircraft(mainWing, hStab, vStab);
	}
}
