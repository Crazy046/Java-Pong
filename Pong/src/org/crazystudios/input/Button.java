package org.crazystudios.input;

import org.crazystudios.graphics.Images;
import org.crazystudios.graphics.Screen;


public class Button {

	private final transient Images imageb;
	private final transient Images simageb;
	public transient int x, y;
	
	public transient boolean hovered;
	
	public Button(final Images imageb, final Images simageb , final int x, final int y) {
		this.imageb = imageb;
		this.simageb = simageb;
		this.x = x;
		this.y = y;
		this.hovered = false;
	}
	
	public boolean pressed (final Keyboard keyboard) {
		return keyboard.select && hovered ? true : false;
	}
	
	public void render(final Screen screen) {
		if (hovered) {
			screen.blit(simageb, this.x, this.y);			
		} else {
			screen.blit(imageb, this.x, this.y);
		}
	}
	
}
