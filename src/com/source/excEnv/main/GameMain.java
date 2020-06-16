package com.source.excEnv.main;

import java.awt.GraphicsDevice;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class GameMain {
	
	//variables
	private static final String titleName = "Aero Designer 1.5 Beta";
	
	public static int GAME_WIDTH;
	public static int GAME_HEIGHT;
	public static Game sGame;
	public static JFrame frame;
	
	public static void main(String[] args) {
		//inital frame
		GraphicsDevice gd = MouseInfo.getPointerInfo().getDevice();
		frame = new JFrame(titleName);	//make new frame
		
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
		
		GAME_WIDTH = gd.getDisplayMode().getWidth();
		GAME_HEIGHT = gd.getDisplayMode().getHeight();
		GAME_WIDTH -= (insets.left + insets.right);
		GAME_HEIGHT -= (insets.top + insets.bottom);		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//colse operation
		frame.setResizable(true);	//non-resizable
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(false);	//set fullscreen
		//add panel into frame
		sGame = new Game(GAME_WIDTH, GAME_HEIGHT);
		frame.add(sGame);
		frame.pack();
		frame.setVisible(true);	//visibility is true
		frame.setIconImage(Resource.iconimage);	//setting icon image		
	}
}
