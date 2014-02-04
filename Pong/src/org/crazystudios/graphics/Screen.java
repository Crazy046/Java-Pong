package org.crazystudios.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class Screen extends Images {

	public transient BufferedImage image;

	public Screen (final int width, final int height) {
		super(width,height);	
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
}
