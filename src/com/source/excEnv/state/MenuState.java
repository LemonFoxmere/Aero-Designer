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
import com.source.excEnv.model.shape.wing;
import com.source.excEnv.main.GameMain;
import com.source.excEnv.model.button.exportButton;
import com.source.excEnv.model.slider.*;

public class MenuState extends State {
	
	private ArrayList<StandardSlider> sliders;
	private exportButton exportBtn;
	
	private int sliderYOffset = GameMain.GAME_HEIGHT/2-370;
	private int sliderXOffset = 60;
	
	private wing mainWing;
	private wing hStab;

	//plane part animation
	int focusMode = 0; // 0 = bird view, 1 = side view, 2 = front view
	private AffineTransform birdScale, sideScale, frontScale;
	private float birdScaler = 0f, birdSize = 1.01f;
	private float sideScaler = 0f, sideSize = 1.01f;
	private float frontScaler = 0f, frontSize = 1.01f;
	
	@Override
	public void init() {
		sliders = new ArrayList<StandardSlider>();
		
		//------Main Wing------
			//                             x                      y         len  ra de min max  desc
			sliders.add(new StandardSlider(0+sliderXOffset, 0+sliderYOffset, 300, 7, 1, 0, 2, "Angle of Attack [ Degrees ]",0));			
			sliders.add(new StandardSlider(0+sliderXOffset, 50+sliderYOffset, 300, 7, 42, 1, 50, "Semispan [ Meters ]",1));			
			sliders.add(new StandardSlider(0+sliderXOffset, 100+sliderYOffset, 300, 7, 0, -89, 89, "Dihedral [ Degrees ]",2));
			sliders.add(new StandardSlider(0+sliderXOffset, 150+sliderYOffset, 300, 7, 10, -35, 55, "Sweep [ Degrees ]",3));
			sliders.add(new StandardSlider(0+sliderXOffset, 200+sliderYOffset, 300, 7, 8, 1, 25, "Chord [ Meters ]",4));
		//------H stab------
			//                             x    y   len ra de min max  desc
			sliders.add(new StandardSlider(0+sliderXOffset, 300+sliderYOffset, 300, 7, 0, -5, 5, "Angle of Attack [ Degrees ]",5));			
			sliders.add(new StandardSlider(0+sliderXOffset, 350+sliderYOffset, 300, 7, 17, 1, 30, "Semispan [ Meters ]",6));			
			sliders.add(new StandardSlider(0+sliderXOffset, 400+sliderYOffset, 300, 7, 0, -89, 89, "Dihedral [ Degrees ]",7));
			sliders.add(new StandardSlider(0+sliderXOffset, 450+sliderYOffset, 300, 7, 20, -25, 55, "Sweep [ Degrees ]",8));
			sliders.add(new StandardSlider(0+sliderXOffset, 500+sliderYOffset, 300, 7, 8, 1, 25, "Chord [ Meters ]",9));
		//------V stab------
			//                             x    y   len ra de min max  desc			
			sliders.add(new StandardSlider(0+sliderXOffset, 600+sliderYOffset, 300, 7, 25, 1, 50, "Wingspan [ Meters ]",10));			
			sliders.add(new StandardSlider(0+sliderXOffset, 650+sliderYOffset, 300, 7, 6, 1, 50, "Chord [ Meters ]",11));
		
		mainWing = new wing(0,0, 0, 0, 0, 0, 0);
		hStab = new wing(0,0, 0, 0, 0, 0, 0);

		birdScale = new AffineTransform();
			
		exportBtn = new exportButton(62, GameMain.GAME_HEIGHT-130, 200, 50, "Export Aircraft");
		
		updateScaler(0);
	}

	@Override
	public void update(float delta) {
		//update main wing
		mainWing.updateSemiSpan(sliders.get(1).getCurrentValNum()*10);
		mainWing.updateSweep(sliders.get(3).getCurrentValNum());
		mainWing.updateChord(sliders.get(4).getCurrentValNum()*12);
		
		//update hStab
		hStab.updateSemiSpan(sliders.get(6).getCurrentValNum()*10);
		hStab.updateSweep(sliders.get(8).getCurrentValNum());
		hStab.updateChord(sliders.get(9).getCurrentValNum()*12);
		
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
	
	private void renderButton(Graphics2D g, standardButton b){
		g.setColor(b.buttonColor);
		g.fillRect(b.x, b.y, b.w, b.h);
		
		//render message
		g.setColor(Color.WHITE);
		g.setFont(new Font("MS Gothic", Font.BOLD, 20));
		g.drawString(b.message, b.x+b.w/2-b.message.length()*5.7f, b.y+b.h/2+5);
	}

	private float translationXIntensity = (GameMain.GAME_WIDTH-(GameMain.GAME_WIDTH-530)/2)*(1-birdScaler);
	
	private void updateScaler(int state) {
		//simple reverse scaler to reset last transformation and prepare for new transformation

		if (state == 0){
			if (birdScaler < 1) {				
				birdScaler += 0.025;			
			}
		} else if (state == 1){
			if (birdScaler < 0.3) {				
				birdScaler += 0.025;
			} else if (birdScaler > 0.4) {
				birdScaler -= 0.020;
			}
		} else if (state == 2){
			if (birdScaler > 0.2) {
				birdScaler -= 0.025;
			}
		}
		//created this piece of art by accident hahahahahahahahahahhah it works
		birdScale.setToScale(birdScaler,birdScaler);
		birdScale.translate((GameMain.GAME_WIDTH-(GameMain.GAME_WIDTH-530)/2)*(1-birdScaler), 0);
	}
	
	private void renderPlane(Graphics2D g, wing mainWing) {
		int offsetPlaneX = 0;
		int offsetPlaneY = 280;

		g.transform(birdScale);
		
		//main wing section
		g.setColor(new Color(
				sliders.get(1).getColor().getRed()+sliders.get(3).getColor().getRed()+sliders.get(4).getColor().getRed(), 
				sliders.get(1).getColor().getGreen()+sliders.get(3).getColor().getGreen()+sliders.get(4).getColor().getGreen(), 
				sliders.get(1).getColor().getBlue()+sliders.get(3).getColor().getBlue()+sliders.get(4).getColor().getBlue()));
		g.fillPolygon(new int[]{(int)mainWing.x+90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX-1,
				(int)mainWing.x1+90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX-1, 
				(int)mainWing.x1+90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX-1, 
				(int)mainWing.x+90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX-1},
				new int[]{(int)mainWing.y+300-((int)mainWing.w/5)+offsetPlaneY-100, 
						(int)mainWing.y1+300-((int)mainWing.w/5)+offsetPlaneY-100, 
						(int)mainWing.y1+(int)mainWing.w+300-((int)mainWing.w/5)+offsetPlaneY-100, 
						(int)mainWing.y+(int)mainWing.w+300-((int)mainWing.w/5)+offsetPlaneY-100}, 4);
		g.fillPolygon(new int[]{(int)mainWing.x+90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX+1,
				0-(int)mainWing.x1+90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX+1, 
				0-(int)mainWing.x1+90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX+1, 
				(int)mainWing.x+90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX+1},
				new int[]{(int)mainWing.y+300-((int)mainWing.w/5)+offsetPlaneY-100, 
						(int)mainWing.y1+300-((int)mainWing.w/5)+offsetPlaneY-100, 
						(int)mainWing.y1+(int)mainWing.w+300-((int)mainWing.w/5)+offsetPlaneY-100, 
						(int)mainWing.y+(int)mainWing.w+300-((int)mainWing.w/5)+offsetPlaneY-100}, 4);
		
		//hStab section
		g.setColor(new Color(
				sliders.get(6).getColor().getRed()+sliders.get(8).getColor().getRed()+sliders.get(9).getColor().getRed(), 
				sliders.get(6).getColor().getGreen()+sliders.get(8).getColor().getGreen()+sliders.get(9).getColor().getGreen(), 
				sliders.get(6).getColor().getBlue()+sliders.get(8).getColor().getBlue()+sliders.get(9).getColor().getBlue()));
		g.fillPolygon(new int[]{(int)hStab.x+90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX-20,
				(int)hStab.x1+90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX-20, 
				(int)hStab.x1+90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX-20, 
				(int)hStab.x+90+GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX-20},
				new int[]{(int)hStab.y+300+offsetPlaneY+200, 
						(int)hStab.y1+300+offsetPlaneY+200, 
						(int)hStab.y1+(int)hStab.w+300+offsetPlaneY+200, 
						(int)hStab.y+(int)hStab.w+300+offsetPlaneY+200}, 4);
		g.fillPolygon(new int[]{(int)hStab.x+90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX+20,
				0-(int)hStab.x1+90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX+20, 
				0-(int)hStab.x1+90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX+20, 
				(int)hStab.x+90/2+GameMain.GAME_WIDTH-(90+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX+20},
				new int[]{(int)hStab.y+300+offsetPlaneY+200, 
						(int)hStab.y1+300+offsetPlaneY+200, 
						(int)hStab.y1+(int)hStab.w+300+offsetPlaneY+200, 
						(int)hStab.y+(int)hStab.w+300+offsetPlaneY+200}, 4);
		
		//render body
		g.setColor(new Color(150, 150, 150));
		
		g.fillRect(GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX, 120+offsetPlaneY, 90, 310);
		g.fillOval(GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX, 0+offsetPlaneY, 90, 240);
		g.fillOval(GameMain.GAME_WIDTH-(90/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX, 180+offsetPlaneY, 90, 500);
		g.setColor(new Color(65, 65, 65));
		g.fillOval(GameMain.GAME_WIDTH-(10/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX, 450+offsetPlaneY, 10, 170);
		g.setColor(new Color(0,0,0));
		g.fillOval(GameMain.GAME_WIDTH-(10/2+(GameMain.GAME_WIDTH-530)/2)+offsetPlaneX, 480+offsetPlaneY, 10, 140);

		try {
			g.transform(birdScale.createInverse());
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		g.fillRect(0, 0, 100, 100);
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
		renderPlane(g2D, mainWing);
		
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
					s.id == 11) {
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
