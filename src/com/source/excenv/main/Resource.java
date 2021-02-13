package com.source.excenv.main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;

import java.io.IOException;

import javax.annotation.Resources;
import javax.imageio.ImageIO;

public class Resource {
	
	public static BufferedImage iconimage;
	
	//load all files
	public static void load() {
		System.out.println("loading images...");
		iconimage = loadImage("iconimage.png");
		System.out.println("finished loading images.");
	}
	
	//load sound
	@SuppressWarnings("unused")
	private static AudioClip loadSound(String filename) {
		URL fileURL = Resources.class.getResource("/resource/" + filename);	//get sound
		return Applet.newAudioClip(fileURL);	//return sound
	}
	
	//get image, as buffered type
	public static BufferedImage loadImage(String filename) {
		BufferedImage img = null;
		try {
			//read and get image
			img = ImageIO.read(Resources.class.getResourceAsStream("/asset/" + filename));
		} catch (IOException e) {
			//if failed, print err
			System.err.println("failed to load \"" + filename + "\"");
		}
		//return the buffered img type
		return img;
	}
	
	public static BufferedImage resizeImage(BufferedImage before, float factor) {
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(factor, factor);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		return after;
	}
}
