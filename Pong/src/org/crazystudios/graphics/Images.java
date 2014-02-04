package org.crazystudios.graphics;

import java.util.Arrays;

public class Images {

	public transient int width;
	public transient int height;
	public transient int[] pixels;
	
	public Images(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}
	
	public void fill(final int colour) {
		Arrays.fill(pixels, colour);
	}

	public void blit(final Images image, int x, int y) {
      
        int x1 = x + image.width;
        int y1 = y + image.height;
        if (x < 0) {x = 0;}
        if (y < 0) {y = 0;}
        if (x1 > width)  {x1 = width;}
        if (y1 > height) {y1 = height;}
        final int ww = x1 - x;

        for (int yy = y; yy < y1; yy++) {
            int tp = yy * width + x;
            final int sp = (yy - y) * image.width + (x - x);
            tp -= sp;
            for (int xx = sp; xx < sp + ww; xx++) {
                int col = image.pixels[xx];
                if (col < 0) {
                	pixels[tp + xx] = col;
                }
            }
        }
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
		   pixels[i] = 0xFF000000;
	    }
	}
	
}
