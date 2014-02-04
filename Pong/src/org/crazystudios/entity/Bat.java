package org.crazystudios.entity;

import java.util.Random;

import org.crazystudios.graphics.Art;
import org.crazystudios.graphics.Screen;
import org.crazystudios.input.Keyboard;

public class Bat extends Entity {
	
	private int score;
	
	public Bat(final int x, final int y, final int w, final int h, final int speed) {
		super(x, y, w, h, speed);
		score = 0;
	}
	
	public int getScore(){ return this.score; }
	private void setScore(final int score){ this.score = score; }

	/**
	 * Draws the bat to the screen
	 * @param screen The screen object to draw to
	 */
	public void render(final Screen screen) {
		screen.blit(Art.bat, this.getX(), this.getY());
	}
	
	/**
	 * Controls the Moment while at the main menu
	 * @param ball The Ball to follow
	 */
	public void mainMenuMovment(final Entity ball) {
		this.setY(ball.getY());
	}

	
	/**
	 * Handles the play input and moment
	 * @param keyboard The keyboard object
	 * @param screenHeight The screen height
	 */
	public void updatePositionPlayer(final Keyboard keyboard, final int screenHeight) {
		if (keyboard.up && !hitTop()) {
			moveDown();
		}
				
		if (keyboard.down && !hitBottom(screenHeight)) {
			moveUp();
		}
	}

	public void updatePositionAi(final int screenWidth, final int screenHeight, final Ball ball) {
		if (ball.getSpeed() > 0) {
			
				Random random = new Random();
				double rnd = random.nextDouble();
				
				if (rnd > 0.038) {
					if (this.getY() < ball.getY()) {
						moveUp();
					} else if (this.getY() > ball.getY()) {
						moveDown();		
					}
				}
		} else {
				if (this.getY() < screenHeight / 2) {
					moveUp();
					
				} else if (this.getY() > screenHeight / 2) {
					moveDown();
				}
		}
	}
	
	public void moveUp() {
		int newPos = this.getY();
		newPos += this.getSpeed();
		
		this.setY(newPos);
	}
	
	public void moveDown() {
		int newPos = this.getY();
		newPos -= this.getSpeed();
		
		this.setY(newPos);
	}
	
	public void updateScore(){
		this.setScore(getScore() + 1);
	}
	
	public void resetScore() {
		this.setScore(0);
	}
	
	
	/*
	 *  Is the Bat hitting the top of the screen
	 *  @return true / false
	 */
	private boolean hitTop() {
		return (this.getY() < 0) ? true : false;
	}
	
	
	/*
	 *  Is the Bat hitting the bottom of the screen
	 *  @return true / false
	 */
	private boolean hitBottom(final int screenHeight) {
		return (this.getY() + this.getH()) >= screenHeight ? true : false;
	}
	
}
