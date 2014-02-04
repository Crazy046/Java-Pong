package org.crazystudios.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	
	
	private transient boolean[] keys = new boolean[65536];
	public transient boolean up, down, select, menu, pause, mute;
	
	public void update() {
		up   = keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_DOWN];
		select = keys[KeyEvent.VK_ENTER];
		menu = keys[KeyEvent.VK_ESCAPE];
		pause = keys[KeyEvent.VK_P];
		mute = keys[KeyEvent.VK_M];
	}
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent arg0) {
		
	}

}
