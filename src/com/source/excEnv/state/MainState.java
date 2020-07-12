package com.source.excEnv.state;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;

import com.source.excEnv.model.button.standardButton;
import com.source.excEnv.model.shape.vStabalizer;
import com.source.excEnv.model.shape.wing;
import com.source.excEnv.main.GameMain;
import com.source.excEnv.model.button.exportButton;
import com.source.excEnv.model.slider.*;

public class MainState extends State {
	
	private ArrayList<StandardSlider> sliders;
	private exportButton exportBtn;
	
	private int sliderYOffset = GameMain.GAME_HEIGHT/2-395;
	private int sliderXOffset = 60;
	
	private wing mainWing;
	private wing hStab;
	private vStabalizer vStab;

	//plane part animation
	int focusMode = 0; // 0 = bird view, 1 = side view, 2 = front view
	private AffineTransform birdScale, sideScale, frontScale;
	private int birdYVec, sideYVec, frontYVec;
	
	@Override
	public void init() {
		sliders = new ArrayList<StandardSlider>();
		
		//------Main Wing------
			//                             x                      y         len  ra de min max  desc
			sliders.add(new StandardSlider(0+sliderXOffset, 0+sliderYOffset, 300, 7, 1, 0, 5, "Angle of Attack [ Degrees ]",0));			
			sliders.add(new StandardSlider(0+sliderXOffset, 50+sliderYOffset, 300, 7, 42, 1, 50, "Semispan [ Meters ]",1));			
			sliders.add(new StandardSlider(0+sliderXOffset, 100+sliderYOffset, 300, 7, 0, -30, 30, "Dihedral [ Degrees ]",2));
			sliders.add(new StandardSlider(0+sliderXOffset, 150+sliderYOffset, 300, 7, 10, -35, 55, "Sweep [ Degrees ]",3));
			sliders.add(new StandardSlider(0+sliderXOffset, 200+sliderYOffset, 300, 7, 8, 1, 25, "Chord [ Meters ]",4));
		//------H stab------
			//                             x    y   len ra de min max  desc
			sliders.add(new StandardSlider(0+sliderXOffset, 300+sliderYOffset, 300, 7, 0, -5, 5, "Angle of Attack [ Degrees ]",5));			
			sliders.add(new StandardSlider(0+sliderXOffset, 350+sliderYOffset, 300, 7, 17, 1, 30, "Semispan [ Meters ]",6));			
			sliders.add(new StandardSlider(0+sliderXOffset, 400+sliderYOffset, 300, 7, 0, -30, 30, "Dihedral [ Degrees ]",7));
			sliders.add(new StandardSlider(0+sliderXOffset, 450+sliderYOffset, 300, 7, 20, -25, 55, "Sweep [ Degrees ]",8));
			sliders.add(new StandardSlider(0+sliderXOffset, 500+sliderYOffset, 300, 7, 8, 1, 25, "Chord [ Meters ]",9));
		//------V stab------
			//                             x    y   len ra de min max  desc			
			sliders.add(new StandardSlider(0+sliderXOffset, 600+sliderYOffset, 300, 7, 18, 1, 40, "Wingspan [ Meters ]",10));			
			sliders.add(new StandardSlider(0+sliderXOffset, 650+sliderYOffset, 300, 7, 10, 1, 30, "Chord [ Meters ]",11));
			sliders.add(new StandardSlider(0+sliderXOffset, 700+sliderYOffset, 300, 7, 20, -60, 35, "Sweep [ Degrees ]",12));
		
		mainWing = new wing(0,0, 0, 0, 0, 0, 0);
		hStab = new wing(0,0, 0, 0, 0, 0, 0);
		vStab = new vStabalizer(0, 0, 0, 0, 0);
		
		birdScale = new AffineTransform();
		sideScale = new AffineTransform();
		frontScale = new AffineTransform();
			
		exportBtn = new exportButton(62, GameMain.GAME_HEIGHT-130, 200, 50, "Export Aircraft");
		
		updateScaler(0);
	}

	@Override
	public void update(float delta) {
		//update main wing
		mainWing.updateAoa(sliders.get(0).getCurrentValNum());
		mainWing.updateSemiSpan(sliders.get(1).getCurrentValNum()*10);
		mainWing.updateDiheral(sliders.get(2).getCurrentValNum());
		mainWing.updateSweep(sliders.get(3).getCurrentValNum());
		mainWing.updateChord(sliders.get(4).getCurrentValNum()*12);
		
		//update hStab
		hStab.updateAoa(sliders.get(5).getCurrentValNum());
		hStab.updateSemiSpan(sliders.get(6).getCurrentValNum()*10);
		hStab.updateDiheral(sliders.get(7).getCurrentValNum());
		hStab.updateSweep(sliders.get(8).getCurrentValNum());
		hStab.updateChord(sliders.get(9).getCurrentValNum()*12);
		
		vStab.updateWingSpan(sliders.get(10).getCurrentValNum()*10);
		vStab.updateChord(sliders.get(11).getCurrentValNum()*12);
		vStab.updateSweep(sliders.get(12).getCurrentValNum());
		
		updateScaler(focusMode);
	}

	private void renderSlider(Graphics2D g, StandardSlider s) {		
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawLine(s.posX, s.posY, s.posX + s.length, s.posY);
		
		//render value on right side
		g.drawString(s.getCurrentVal(), s.posX + s.length + 20, s.posY + 5);
		g.setFont(new Font("MS Gothic", Font.PLAIN, 15));
		g.drawString(s.description, s.posX, s.posY - 10);
		g.setFont(new Font("MS Gothic", Font.PLAIN, 20));
		
		//render slider button
		g.setColor(s.buttonColor);
		g.fillOval(s.buttonX, s.buttonY, (int)s.buttonRad*2, (int)s.buttonRad*2);
	}
	
	
	
	private void updateScaler(int state) {
		if (state == 0){
			if (birdYVec < -200) {				
				birdYVec += 100;			
			} else if (birdYVec >= -200 && birdYVec < -80) { //these are to slow the decent for a smoother animation
				birdYVec += 25;
			} else if (birdYVec >= -80 && birdYVec <= 0) {
				birdYVec += 10;
			}
		} else if (state == 1 || state == 2){
			if (birdYVec > -1500) {
				birdYVec -= 90;
			}
		}
		birdScale.setToTranslation(0, birdYVec);
		
		// now we update the side scaler. simple shit
		if (state == 0){ // if its at front view, you want to vanish
			if (sideYVec < 1500) {
				if(sideYVec <= -1500) {
					sideYVec += 1400;
				}
				sideYVec += 90;			
			}
		} else if (state == 1){
			if (sideYVec > 200) {
				sideYVec -= 100;
			} else if (sideYVec <= 200 && sideYVec > 80) { //these are to slow the decent for a smoother animation
				sideYVec -= 25;
			} else if (sideYVec <= 80 && sideYVec > 0) {
				sideYVec -= 10;
			}
			
			if (sideYVec < -200) {
				sideYVec += 100;
			} else if (sideYVec >= -200 && sideYVec < -80) { //these are to slow the decent for a smoother animation
				sideYVec += 25;
			} else if (sideYVec >= -80 && sideYVec < 0) {
				sideYVec += 10;
			}
		} else if (state == 2) {
			if (sideYVec > -1500) {
				if(sideYVec >= 1500) {
					sideYVec -= 600;
				}
				sideYVec -= 90;
			}
		}
		sideScale.setToTranslation(0, sideYVec);
		
		//finally, the front view
		if (state == 2){
			if (frontYVec > 200) {				
				frontYVec -= 100;			
			} else if (frontYVec <= 200 && frontYVec > 80) { //these are to slow the decent for a smoother animation
				frontYVec -= 25;
			} else if (frontYVec <= 80 && frontYVec >= 0) {
				frontYVec -= 10;
			}
		} else if (state == 1 || state == 0){
			if (frontYVec < 1500) {
				frontYVec += 90;
			}
		}
		frontScale.setToTranslation(0, frontYVec);
	}
	
	private void renderBirdView(Graphics2D g, wing mainWing, wing hStab, int offsetPlaneX, int offsetPlaneY) {
		g.transform(birdScale);
		//main wing section
		g.setColor(new Color(
				sliders.get(1).getColor().getRed()+sliders.get(3).getColor().getRed()+sliders.get(4).getColor().getRed(), 
				sliders.get(1).getColor().getGreen()+sliders.get(3).getColor().getGreen()+sliders.get(4).getColor().getGreen(), 
				sliders.get(1).getColor().getBlue()+sliders.get(3).getColor().getBlue()+sliders.get(4).getColor().getBlue()));
		
		float birdOffsetY = GameMain.GAME_HEIGHT/2-680/2-50;
		
		{
			float birdOffsetX = 90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX;
			g.fillPolygon(new int[]{(int) (mainWing.x+birdOffsetX-1),
					(int) (mainWing.x1+birdOffsetX-1), 
					(int) (mainWing.x1+birdOffsetX-1), 
					(int) (mainWing.x+birdOffsetX-1)},
					new int[]{(int) (mainWing.y+250-(mainWing.w/5)+offsetPlaneY+birdOffsetY), 
							(int) (mainWing.y1+250-(mainWing.w/5)+offsetPlaneY+birdOffsetY), 
							(int) (mainWing.y1+mainWing.w+250-(mainWing.w/5)+offsetPlaneY+birdOffsetY), 
							(int) (mainWing.y+mainWing.w+250-(mainWing.w/5)+offsetPlaneY+birdOffsetY)}, 4);
		}{
			float birdOffsetX = 90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX;
			g.fillPolygon(new int[]{(int) (mainWing.x+birdOffsetX+1),
					(int) (0-mainWing.x1+birdOffsetX+1), 
					(int) (0-mainWing.x1+birdOffsetX+1), 
					(int) (mainWing.x+birdOffsetX+1)},
					new int[]{(int) (mainWing.y+250-(mainWing.w/5)+offsetPlaneY+birdOffsetY), 
							(int) (mainWing.y1+250-(mainWing.w/5)+offsetPlaneY+birdOffsetY), 
							(int) (mainWing.y1+mainWing.w+250-(mainWing.w/5)+offsetPlaneY+birdOffsetY), 
							(int) (mainWing.y+mainWing.w+250-(mainWing.w/5)+offsetPlaneY+birdOffsetY)}, 4);
		}
		//hStab section
		g.setColor(new Color(
				sliders.get(6).getColor().getRed()+sliders.get(8).getColor().getRed()+sliders.get(9).getColor().getRed(), 
				sliders.get(6).getColor().getGreen()+sliders.get(8).getColor().getGreen()+sliders.get(9).getColor().getGreen(), 
				sliders.get(6).getColor().getBlue()+sliders.get(8).getColor().getBlue()+sliders.get(9).getColor().getBlue()));
		{
			float birdOffsetX = 90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX;
			g.fillPolygon(new int[]{(int)hStab.x+90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX-20,
					(int) (hStab.x1+birdOffsetX-20), 
					(int) (hStab.x1+birdOffsetX-20), 
					(int) (hStab.x+birdOffsetX-20)},
					new int[]{(int) (hStab.y+500+offsetPlaneY+birdOffsetY), 
							(int) (hStab.y1+500+offsetPlaneY+birdOffsetY), 
							(int) (hStab.y1+hStab.w+500+offsetPlaneY+birdOffsetY), 
							(int) (hStab.y+hStab.w+500+offsetPlaneY+birdOffsetY)}, 4);
		}{
			float birdOffsetX = 90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX;
			g.fillPolygon(new int[]{(int)hStab.x+90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX+20,
					(int) (0-hStab.x1+birdOffsetX+20), 
					(int) (0-hStab.x1+birdOffsetX+20), 
					(int) (hStab.x+birdOffsetX+20)},
					new int[]{(int) (hStab.y+500+offsetPlaneY+birdOffsetY), 
							(int) (hStab.y1+500+offsetPlaneY+birdOffsetY), 
							(int) (hStab.y1+(int)hStab.w+500+offsetPlaneY+birdOffsetY), 
							(int) (hStab.y+(int)hStab.w+500+offsetPlaneY+birdOffsetY)}, 4);
		}
		
		//render body
		g.setColor(new Color(180,180,180));
		
		{
			int birdOffsetX = (int) (GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2));
			
			g.fillRect(birdOffsetX+offsetPlaneX, (int) (120+birdOffsetY+offsetPlaneY), 90, 310);
			g.fillOval(birdOffsetX+offsetPlaneX, (int) (0+birdOffsetY+offsetPlaneY), 90, 240);
			g.fillOval(birdOffsetX+offsetPlaneX, (int) (180+birdOffsetY+offsetPlaneY), 90, 500);
			g.setColor(new Color(160, 160, 160));
			g.fillOval(GameMain.GAME_WIDTH-(10/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX, (int) (450+birdOffsetY+offsetPlaneY), 10, 170);
			g.setColor(new Color(130,130,130));
			g.fillOval(GameMain.GAME_WIDTH-(10/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX, (int) (480+birdOffsetY+offsetPlaneY), 10, 140);
		}
		
		try {
			g.transform(birdScale.createInverse());
		} catch (NoninvertibleTransformException e) {
			System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
			System.err.println("---");
			e.printStackTrace();
		}
	}
	
	private void renderSideView(Graphics2D g, wing mainWing, wing hStab, vStabalizer vStab, int offsetPlaneX, int offsetPlaneY) {		
		g.transform(sideScale);
		
		float sideOffsetX = GameMain.GAME_WIDTH-((GameMain.GAME_WIDTH-530)/2)+offsetPlaneX - 25;
		float sideOffsetY = GameMain.GAME_HEIGHT/2 + offsetPlaneY - 50;

		{
			float vStabOffsetY = 10;
			float vStabOffsetX = 226 - vStab.chord/1.5f;
			g.setColor(new Color(
					sliders.get(10).getColor().getRed()+sliders.get(11).getColor().getRed()+sliders.get(12).getColor().getRed(), 
					sliders.get(10).getColor().getGreen()+sliders.get(11).getColor().getGreen()+sliders.get(12).getColor().getGreen(), 
					sliders.get(10).getColor().getBlue()+sliders.get(11).getColor().getBlue()+sliders.get(12).getColor().getBlue()));
			g.fillPolygon(new int[]{
				(int) (vStab.x+sideOffsetX+vStabOffsetX),
				(int) (vStab.x1+sideOffsetX+vStabOffsetX),
				(int) (vStab.x1+vStab.w+sideOffsetX+vStabOffsetX),
				(int) (vStab.x+vStab.w+sideOffsetX+vStabOffsetX)
			},
			new int[]{
				(int) (vStab.y+sideOffsetY+vStabOffsetY),
				(int) (vStab.y-(vStab.y1-vStab.y)+sideOffsetY+vStabOffsetY),
				(int) (vStab.y-(vStab.y1-vStab.y)+sideOffsetY+vStabOffsetY),
				(int) (vStab.y+sideOffsetY+vStabOffsetY)
			}, 4);
		}
		
		{
			g.setColor(new Color(180,180,180));
			g.fillRect((int) sideOffsetX - 350, (int) (sideOffsetY), 500, 100);
			g.fillOval((int) sideOffsetX - 450, (int) (sideOffsetY), 200, 100);
			g.fillOval((int) sideOffsetX - 50, (int) (sideOffsetY), 400, 100);
		}
		
		{			
			g.rotate(Math.toRadians(mainWing.aoa), sideOffsetX-50-(mainWing.chord*1.6/3), sideOffsetY+70);
			g.setColor(new Color(
					sliders.get(0).getColor().getRed()/2+100, 
					sliders.get(0).getColor().getGreen()/2+100, 
					sliders.get(0).getColor().getBlue()/2+100));
			g.fillOval((int) (sideOffsetX-100-(mainWing.chord*1.6/3)), 
					(int) sideOffsetY+70, 
					(int) (mainWing.chord*1.6+ (mainWing.semiSpan * (mainWing.sweep/90))), 
					(int) 18);

			g.setColor(new Color(
					sliders.get(0).getColor().getRed(), 
					sliders.get(0).getColor().getGreen(), 
					sliders.get(0).getColor().getBlue()));
			g.fillOval((int) (sideOffsetX-100-(mainWing.chord*1.6/3) + (mainWing.semiSpan * (mainWing.sweep/90))), 
					(int) sideOffsetY+70, 
					(int) (mainWing.chord*1.6), 
					(int) 18);
			g.rotate(-Math.toRadians(mainWing.aoa), sideOffsetX-50-(mainWing.chord*1.6/3), sideOffsetY+70);
		}
		
		{			
			g.rotate(Math.toRadians(hStab.aoa), sideOffsetX+185, sideOffsetY+70);
			g.setColor(new Color(
					sliders.get(5).getColor().getRed()/2+100, 
					sliders.get(5).getColor().getGreen()/2+100, 
					sliders.get(5).getColor().getBlue()/2+100));
			g.fillOval((int) (sideOffsetX+135), 
					(int) sideOffsetY+30, 
					(int) (hStab.chord*1+ (hStab.semiSpan * (hStab.sweep/90))), 
					(int) 10);

			g.setColor(new Color(
					sliders.get(5).getColor().getRed(), 
					sliders.get(5).getColor().getGreen(), 
					sliders.get(5).getColor().getBlue()));
			g.fillOval((int) (sideOffsetX+135 + (hStab.semiSpan * (hStab.sweep/90))), 
					(int) sideOffsetY+30, 
					(int) (hStab.chord*1), 
					(int) 10);
			g.rotate(-Math.toRadians(hStab.aoa), sideOffsetX+185, sideOffsetY+70);
		}
		
		try {
			g.transform(sideScale.createInverse());
		} catch (NoninvertibleTransformException e) {
			System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
			System.err.println("---");
			e.printStackTrace();
		}
	}
	
	private void renderFrontView(Graphics2D g, wing mainWing, wing hStab, vStabalizer vStab, int offsetPlaneX, int offsetPlaneY) {
		g.transform(frontScale);
		
		float frontOffsetX = GameMain.GAME_WIDTH-((GameMain.GAME_WIDTH-530)/2)+offsetPlaneX - 25;
		float frontOffsetY = GameMain.GAME_HEIGHT/2 + offsetPlaneY - 50;
		
		{	// vstab	
			g.setColor(new Color(
					sliders.get(10).getColor().getRed()+sliders.get(11).getColor().getRed()+sliders.get(12).getColor().getRed(), 
					sliders.get(10).getColor().getGreen()+sliders.get(11).getColor().getGreen()+sliders.get(12).getColor().getGreen(), 
					sliders.get(10).getColor().getBlue()+sliders.get(11).getColor().getBlue()+sliders.get(12).getColor().getBlue()));
			g.fillPolygon(new int[] {(int) (frontOffsetX-10), (int) (frontOffsetX+10), (int) frontOffsetX}, 
					new int[] {(int) frontOffsetY-38, (int) frontOffsetY-38, (int) (frontOffsetY-38-vStab.wingSpan)}, 3);		
		}

		{	//main wing
			g.setColor(new Color(
					sliders.get(2).getColor().getRed(), 
					sliders.get(2).getColor().getGreen(), 
					sliders.get(2).getColor().getBlue()));
			g.rotate(Math.toRadians(mainWing.dihedral), frontOffsetX-40, frontOffsetY+25);
			{
				g.fillPolygon(new int[] {(int) frontOffsetX-40,(int) (frontOffsetX-mainWing.x1),(int) frontOffsetX-40}, 
						new int[] {(int) frontOffsetY+15,(int) (frontOffsetY+25),(int) (frontOffsetY+35)}, 3);
			}
			g.rotate(-Math.toRadians(mainWing.dihedral), frontOffsetX-40, frontOffsetY+25);
			g.rotate(-Math.toRadians(mainWing.dihedral), frontOffsetX+40, frontOffsetY+25);
			{
				g.fillPolygon(new int[] {(int) frontOffsetX+40,(int) (frontOffsetX+mainWing.x1),(int) frontOffsetX+40}, 
						new int[] {(int) frontOffsetY+15,(int) (frontOffsetY+25),(int) (frontOffsetY+35)}, 3);
			}
			g.rotate(Math.toRadians(mainWing.dihedral), frontOffsetX+40, frontOffsetY+25);
		}
		
		{	//main wing
			g.setColor(new Color(
					sliders.get(7).getColor().getRed(), 
					sliders.get(7).getColor().getGreen(), 
					sliders.get(7).getColor().getBlue()));
			g.rotate(Math.toRadians(hStab.dihedral), frontOffsetX-35, frontOffsetY-20);
			{
				g.fillPolygon(new int[] {(int) frontOffsetX-35,(int) (frontOffsetX-hStab.x1),(int) frontOffsetX-35}, 
						new int[] {(int) frontOffsetY-15,(int) (frontOffsetY-20),(int) (frontOffsetY-25)}, 3);
			}
			g.rotate(-Math.toRadians(hStab.dihedral), frontOffsetX-35, frontOffsetY-20);
			g.rotate(-Math.toRadians(hStab.dihedral), frontOffsetX+35, frontOffsetY-20);
			{
				g.fillPolygon(new int[] {(int) frontOffsetX+35,(int) (frontOffsetX+hStab.x1),(int) frontOffsetX+35}, 
						new int[] {(int) frontOffsetY-15,(int) (frontOffsetY-20),(int) (frontOffsetY-25)}, 3);
			}
			g.rotate(Math.toRadians(hStab.dihedral), frontOffsetX+35, frontOffsetY-20);
		}

		{	//body
			g.setColor(new Color(160,160,160));			
			g.fillRoundRect((int) (frontOffsetX-45), (int) (frontOffsetY-5), 90, 55, 25, 25);
			g.setColor(new Color(180,180,180));			
			g.fillOval((int) (frontOffsetX-45), (int) (frontOffsetY-45), 90, 90);
			g.setColor(new Color(200,200,200));			
			g.fillOval((int) (frontOffsetX-15), (int) (frontOffsetY), 30, 30);
		}
		
		
		try {
			g.transform(frontScale.createInverse());
		} catch (NoninvertibleTransformException e) {
			System.err.println("An internal error has occoured. Please report this problem to reallemonorange@gmail.com along with the following error code: ");
			System.err.println("---");
			e.printStackTrace();
		}
	}
	
	private void renderPlane(Graphics2D g, wing mainWing, wing hStab) {
		int offsetPlaneX = 0;
		int offsetPlaneY = 0;

		renderBirdView(g, mainWing, hStab, offsetPlaneX, offsetPlaneY);
		renderSideView(g, mainWing, hStab, vStab, offsetPlaneX, offsetPlaneY);
		renderFrontView(g, mainWing, hStab, vStab, offsetPlaneX, offsetPlaneY);
	}
	
	private void renderButton(Graphics2D g, standardButton b){
		g.setColor(b.buttonColor);
		g.fillRect(b.x, b.y, b.w, b.h);
		
		//render message
		g.setColor(Color.WHITE);
		g.setFont(new Font("MS Gothic", Font.BOLD, 20));
		g.drawString(b.message, b.x+b.w/2-b.message.length()*5.7f, b.y+b.h/2+5);
	}
	
	@Override
	public void render(Graphics g) {
		//DO NOT DELETE THIS LINE
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2D.setFont(new Font("MS Gothic", Font.PLAIN, 20));
		
		//rendeer bg
		g2D.setColor(new Color(247, 247, 247));
		g2D.fillRect(0, 0, GameMain.GAME_WIDTH, GameMain.GAME_HEIGHT);
		//rendeer bg
		
		for (StandardSlider i : sliders) {		
			renderSlider(g2D, i);
		}
		
		//render separator
		g2D.setColor(new Color(217, 217, 217));
		g2D.setStroke(new BasicStroke(2));
		g2D.drawLine(20, 230+sliderYOffset, 450, 230+sliderYOffset);
		g2D.drawLine(20, 520+sliderYOffset, 450, 520+sliderYOffset);
		g2D.drawLine(530, 350, 530, GameMain.GAME_HEIGHT-450);
		
		g2D.setColor(Color.BLACK);	//reset color
		
		//render section names
		{
			//section main
			int offsetX = 165;
			int offsetY = sliderYOffset-35;
			g2D.setFont(new Font("Trebuchet MS", Font.BOLD, 25));
			g2D.drawString("Main Wing", offsetX, offsetY);
		}{
			//section main
			int offsetX = 150;
			int offsetY = 260+sliderYOffset;
			g2D.setFont(new Font("Trebuchet MS", Font.BOLD, 25));
			g2D.drawString("H. Stabilizer", offsetX, offsetY);
		}{
			//section main
			int offsetX = 150;
			int offsetY = 560+sliderYOffset;
			g2D.setFont(new Font("Trebuchet MS", Font.BOLD, 25));
			g2D.drawString("V. Stabilizer", offsetX, offsetY);
		}
		
		//render buttons
		renderButton(g2D, exportBtn);
		
		//render plane
		renderPlane(g2D, mainWing, hStab);
		
	}

	
	@Override
	public void onClick(MouseEvent e) {
	}

	@Override
	public void onKeyPress(KeyEvent e) {
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
	}
	
	//------
	
	@Override
	public void onMousePress(MouseEvent e) {
		for (StandardSlider i : sliders) {
			if (i.isInBoundingBox(e.getX(), e.getY())){
				int offset = - e.getX() + i.buttonX;
				i.updateOffset(offset);
				i.isDragged = true;
			}
		}
		
		if (exportBtn.inBound(e.getX(), e.getY())) {
			exportBtn.pressed();
		}
	}
	
	//------
	
	@Override
	public void onMouseRelease(MouseEvent e) {
		for (StandardSlider i : sliders) {
			i.isDragged = false;
		}
		
		//check if the box is pressed
		if (exportBtn.inBound(e.getX(), e.getY())) {
			exportBtn.action();
			exportBtn.released();
		}
	}
	
	//------

	private void updateHoveredSliders(MouseEvent e, StandardSlider s) {
		if (s.isInBoundingBox(e.getX(), e.getY())){
			s.updateToHoverState();
			
			//focus update
			if (s.id == 1 || 
					s.id == 3 || 
					s.id == 4 || 
					s.id == 6 || 
					s.id == 8 || 
					s.id == 9) {
				//then update the focus to bird view
				focusMode = 0;
			} else if (s.id == 0 || 
					s.id == 5 || 
					s.id == 10 || 
					s.id == 11 ||
					s.id == 12) {
				//then update the focus to side view
				focusMode = 1;
			} else {
				focusMode = 2;
			}
		} else {
			s.updateToNormalState();
		}
	}
	
	private void updateHoveredButtons(MouseEvent e, standardButton b) {
		if (b.inBound(e.getX(), e.getY())){
			b.onHover();
		} else {
			b.onLostFocus();
		}
	}
	
	@Override
	public void mouseMove(MouseEvent e) {
		for (StandardSlider i : sliders) {
			updateHoveredSliders(e, i);
		}
//		System.out.println(button.inBound(e.getX(), e.getY()));
		updateHoveredButtons(e, exportBtn);
	}

	//------
	
	@Override
	public void mouseDragged(MouseEvent e) {
		for(StandardSlider i : sliders) {
			if (i.isDragged){
				i.updateValue(e.getX());
			}
		}
	}
	
	//------

	@Override
	public void mouseScroll(MouseWheelEvent e) {
		for(StandardSlider i : sliders) {
			if (i.isInBoundingBox(e.getX(), e.getY())){
				if (e.isShiftDown()) {					
					if(e.getWheelRotation() == -1) {// increase value like a normal person
						i.offsetVal(0.01f);
						return;
					}
					i.offsetVal(-0.01f);
					return;
				}
				if(e.getWheelRotation() == -1) {// increase value like a normal person
					i.offsetVal(0.1f);
					return;
				}
				i.offsetVal(-0.1f);
				return;
			}
		}
	}
}
