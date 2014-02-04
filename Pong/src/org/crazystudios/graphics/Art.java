package org.crazystudios.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.crazystudios.pong.Pong;

public class Art {
	
	private transient static final Logger LOG = Logger.getLogger(Pong.class.getName());
	
	public static Images bat  = Load("res/textures/bat.png");
	public static Images ball = Load("res/textures/ball.png");
	public static Images background = Load("res/textures/background.png");
	
	public static Images title = Load("res/textures/title.png");
	
	public static Images playb = Load("res/textures/playb.png");
	public static Images splayb = Load("res/textures/splayb.png");
	
	public static Images exitb = Load("res/textures/exitb.png");
	public static Images sexitb = Load("res/textures/sexitb.png");
	
	public static Images paused = Load("res/textures/Pause.png");
	
	public static Images three = Load("res/textures/3.png");
	public static Images two   = Load("res/textures/2.png");
	public static Images one   = Load("res/textures/1.png");
	public static Images zero  = Load("res/textures/0.png");
	
	public static Images trail = Load("res/textures/particle.png");
	
	public static Images Load(String string) {		
		Images result;
		
		 try {
	            final BufferedImage bi = ImageIO.read(new File(string));

	            final int w = bi.getWidth();
	            final int h = bi.getHeight();

	            result = new Images(w, h);
	            bi.getRGB(0, 0, w, h, result.pixels, 0, w);

	           
	        } catch (IOException e) {
	        	 LOG.log(null, "Cant Load Image!", e);
	        	 result = null;
	        }
		 
		 return result;
	}
	
	public static int generatRandomPositiveNegitiveValue(int max , int min) {
	    //Random rand = new Random();
	    int ii = -min + (int) (Math.random() * ((max - (-min)) + 1));
	    return ii;
	}
	
}
