package org.crazystudios.entity;

import java.awt.Point;

import org.crazystudios.graphics.Art;
import org.crazystudios.graphics.Screen;

public class Ball extends Entity {

	private int yspeed;
	private Point[] trail;
	
	public Ball(final int x, final int y, final int w, final int h, final int speed) {
		super(x, y, w, h, speed);
		this.yspeed = speed;
		
		trail = new Point[20];
		resetTrail();
	}

	public void setYSpeed(final int speed){ this.yspeed = speed; };
	public int getYSpeed(){ return this.yspeed; };
	
	public void render(final Screen screen) {
		for(int i = 0; i < this.trail.length; i ++) {
				screen.blit(Art.trail, (int) this.trail[i].getX(), (int) this.trail[i].getY());
		}
		
		screen.blit(Art.ball, this.getX(), this.getY());
	}
	
	public void setXandY(final int x, final int y) {
		this.setX(x);
		this.setY(y);
	}
	
	public void reverseXDirection() {
		final int newSpeed = this.getSpeed();
		this.setSpeed(-newSpeed);
	}
	
	public void reverseYDirection() {
		final int newSpeed = this.getYSpeed();
		this.setYSpeed(-newSpeed);
	}
	
	public void updatePosition() {
		addTrailPoint();
		
		int newPos = this.getX();
		newPos += this.getSpeed();
		
		this.setX(newPos);

		newPos = this.getY();
		newPos -= this.getYSpeed();
		
		this.setY(newPos);
	}
	
	public void updateSpeed(final int screenHeight, final int screenWidth) {
		if (this.getY() < 0 || (this.getY() + this.getH() * 20) > screenHeight) {
			reverseYDirection();
		}
	}
	
	
	public void resetTrail() {
		for(int i = 0; i < this.trail.length; i++) {
			this.trail[i] = new Point(-100,-100);
		}
	}
	
	private void addTrailPoint() {
		boolean ajustPoints = false;
		
		for(int i = 0; i < this.trail.length; i ++) {
			if (trail[i].equals(new Point(-100,-100))){ 
				trail[i] = new Point(this.getX(), this.getY()); ajustPoints = false; break;
			} else {
				ajustPoints = true;
			}
		}
		
		if (ajustPoints) {
			for(int i= this.trail.length - 1; i > 0 ;i--){ 
			     this.trail[i] = this.trail[i-1];
			}
			
			this.trail[0] = new Point(this.getX(), this.getY()); 
		}
		
	}
	
}
