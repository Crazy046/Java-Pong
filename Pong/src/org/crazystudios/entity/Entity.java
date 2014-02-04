package org.crazystudios.entity;

public class Entity {

	
	private int x,y,w,h,speed;
	
	public Entity(final int x, final int y, final int w, final int h, final int speed) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.speed = speed;
	}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getW() { return this.w; }
	public int getH() { return this.h; }
	public int getSpeed() { return this.speed; }
	
	public void setX(final int x) {  this.x = x; }
	public void setY(final int y) {  this.y = y; }
	public void setW(final int w) {  this.w = w; }
	public void setH(final int h) {  this.h = h; }
	public void setSpeed(final int speed) {  this.speed = speed; }
	
}
