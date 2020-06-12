package com.source.excEnv.state;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import com.source.excEnv.model.button.standardButton;
import com.source.excEnv.main.GameMain;
import com.source.excEnv.model.button.exportButton;
import com.source.excEnv.model.slider.*;

public class MenuState extends State {
	
	private ArrayList<StandardSlider> sliders;
	private exportButton exportBtn;
	
	private int sliderYOffset = 150;
	private int sliderXOffset = 60;

	@Override
	public void init() {
		sliders = new ArrayList<StandardSlider>();
		
		//------Main Wing------
			//                             x    y   len ra de min max  desc
			sliders.add(new StandardSlider(0+sliderXOffset, 0+sliderYOffset, 300, 7, 0, -5, 5, "Angle of Attack [ Degrees ]"));			
			sliders.add(new StandardSlider(0+sliderXOffset, 50+sliderYOffset, 300, 7, 25, 1, 50, "Semispan [ Meters ]"));			
			sliders.add(new StandardSlider(0+sliderXOffset, 100+sliderYOffset, 300, 7, 0, -89, 89, "Dihedral [ Degrees ]"));
			sliders.add(new StandardSlider(0+sliderXOffset, 150+sliderYOffset, 300, 7, 0, -89, 89, "Sweep [ Degrees ]"));
			sliders.add(new StandardSlider(0+sliderXOffset, 200+sliderYOffset, 300, 7, 25, 1, 50, "Chord [ Meters ]"));
		//------H stab------
			//                             x    y   len ra de min max  desc
			sliders.add(new StandardSlider(0+sliderXOffset, 300+sliderYOffset, 300, 7, 0, -5, 5, "Angle of Attack [ Degrees ]"));			
			sliders.add(new StandardSlider(0+sliderXOffset, 350+sliderYOffset, 300, 7, 25, 1, 50, "Semispan [ Meters ]"));			
			sliders.add(new StandardSlider(0+sliderXOffset, 400+sliderYOffset, 300, 7, 0, -89, 89, "Dihedral [ Degrees ]"));
			sliders.add(new StandardSlider(0+sliderXOffset, 450+sliderYOffset, 300, 7, 0, -89, 89, "Sweep [ Degrees ]"));
			sliders.add(new StandardSlider(0+sliderXOffset, 500+sliderYOffset, 300, 7, 25, 1, 50, "Chord [ Meters ]"));
		//------V stab------
			//                             x    y   len ra de min max  desc			
			sliders.add(new StandardSlider(0+sliderXOffset, 600+sliderYOffset, 300, 7, 25, 1, 50, "Wingspan [ Meters ]"));			
			sliders.add(new StandardSlider(0+sliderXOffset, 650+sliderYOffset, 300, 7, 25, 1, 50, "Chord [ Meters ]"));
		
		exportBtn = new exportButton(62, GameMain.GAME_HEIGHT-130, 200, 50, "Export Aircraft");
	}

	@Override
	public void update(float delta) {
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
		g2D.drawLine(20, 240+sliderYOffset, 450, 240+sliderYOffset);
		g2D.drawLine(20, 540+sliderYOffset, 450, 540+sliderYOffset);
		g2D.drawLine(530, 200, 530, GameMain.GAME_HEIGHT-300);
		
		g2D.setColor(Color.BLACK);	//reset color
		
		//render section names
		{
			//section main
			int offsetX = 460;
			int offsetY = 190;
			g2D.rotate(Math.toRadians(90), offsetX, offsetY);
			g2D.setFont(new Font("Trebuchet MS", Font.BOLD, 25));
			g2D.drawString("Main Wing", offsetX, offsetY);
			g2D.rotate(Math.toRadians(-90), offsetX, offsetY);
		}{
			//section main
			int offsetX = 460;
			int offsetY = 475;
			g2D.rotate(Math.toRadians(90), offsetX, offsetY);
			g2D.setFont(new Font("Trebuchet MS", Font.BOLD, 25));
			g2D.drawString("H. Stabilizer", offsetX, offsetY);
			g2D.rotate(Math.toRadians(-90), offsetX, offsetY);
		}{
			//section main
			int offsetX = 150;
			int offsetY = 850;
			g2D.setFont(new Font("Trebuchet MS", Font.BOLD, 25));
			g2D.drawString("V. Stabilizer", offsetX, offsetY);
		}
		
		//render buttons
		renderButton(g2D, exportBtn);
		
		//section section
		
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
