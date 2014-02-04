package org.crazystudios.pong;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.crazystudios.entity.Ball;
import org.crazystudios.entity.Bat;
import org.crazystudios.entity.Entity;
import org.crazystudios.graphics.Art;
import org.crazystudios.graphics.Screen;
import org.crazystudios.input.Button;
import org.crazystudios.input.Keyboard;
import org.crazystudios.sound.Sound;

public class Pong extends Canvas implements Runnable {
	
	private static transient  final Logger LOG = Logger.getLogger(Pong.class.getName());
	private static final long serialVersionUID = 1L;
	
	private static final transient int SCREEN_WIDTH = 800;
	private static final transient int SCREEN_HEIGHT = 500;
	private static final transient double FRAMERATE = 60.0;
	
	public transient Thread thread;
	
	private final transient Keyboard keyboard;
	private final transient Screen screen;
	private final transient JFrame frame;
    
	public transient boolean running;
	
	private transient gameStates gameState = gameStates.MAINMENU;
	
	private final transient Bat player1 = new Bat(25, SCREEN_HEIGHT / 2, 16, 32, 5);
	private final transient Bat player2 = new Bat(755, SCREEN_HEIGHT / 2, 16, 32, 5);
	private final transient Ball ball   = new Ball(SCREEN_WIDTH / 2,SCREEN_HEIGHT / 2,16,16, 5);
	
	private final transient Button play = new Button(Art.playb, Art.splayb,   SCREEN_WIDTH / 2  - Art.title.width / 2, 100);
	private final transient Button exit = new Button(Art.exitb, Art.sexitb,   SCREEN_WIDTH / 2  - Art.title.width / 2, 300);
	
	private transient boolean playCountdown;
	private transient int loops;
	private transient double screenShakeTicks;
	private transient boolean roundReset;
	
	private transient boolean muted;
	
	private final transient Sound music = new Sound("res/sounds/soundtrack.wav");
	private final transient Sound beep = new Sound("res/sounds/beep.wav");
	
	private enum gameStates {
		MAINMENU, 
		GAME,
		PAUSED,
		GAMEOVER
	}
	
	public Pong() {
		
		super();
		
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		
		frame  = new JFrame("Game");
		screen = new Screen(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		keyboard = new Keyboard();
		addKeyListener(keyboard);
		
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		
		final Image icon = Toolkit.getDefaultToolkit().getImage("res/icon.png");
		frame.setIconImage(icon);
		
		frame.setIgnoreRepaint( true );
		frame.setResizable(false);
	    frame.setVisible(true);
	    frame.setLocationRelativeTo(null);
	    frame.toFront();
	    
	    
	    running = false;
	    playCountdown = false;
	    loops = 0;
	    muted = false;
	    screenShakeTicks = 0;
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0D / FRAMERATE;
		long timer = System.currentTimeMillis();
		
		double delta = 0;
		double fps = 0;
		double tick = 0;
		
		play.hovered = true;
		music.playClip(-20, true);
		
		while(running) {
			final long now  = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
				while (delta >= 1) {
					ticks();
					if (screenShakeTicks > 0) {
						screenShakeTicks -= delta;
					}
					tick++;
					delta--;
				}
				if (playCountdown && loops >= 3) {					
					 loops = 0;
					 playCountdown = false;
				}
			 render();
			 fps++;
			 
		    if (System.currentTimeMillis() - timer > 1000) {
			  frame.setTitle("Pong | Frames: " + fps + " | ticks: " + tick);
			  timer += 1000;
			  fps = 0;
			  tick = 0;
			  if (playCountdown) {
				  loops += 1;
			  }
			}
			
		}
	}
	
	public void ticks() {
		keyboard.update();
	
		switch (gameState) {
		
			case MAINMENU: 
					menuTicks();
					break;
			case GAME:
					gameTicks();
					break;
			case PAUSED:
					pausedTicks();
					break;
			case GAMEOVER:
					if (keyboard.menu|| keyboard.select) {
						gameState = gameStates.MAINMENU;
						ball.setXandY(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
						ball.resetTrail();
						try {
							TimeUnit.MILLISECONDS.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					break;
		}
			
			if (keyboard.mute) {
				if (muted) {
					muted = false;
					music.playClip(-15, true);
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				} else {
					muted = true;
					music.stopClip();
					
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	private void menuTicks() {
		if(play.pressed(keyboard)) {
			gameState = gameStates.GAME;
			playCountdown = true;
			player1.resetScore();
			player2.resetScore();
			ball.setXandY(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
			ball.resetTrail();
		}
		
		if (exit.pressed(keyboard)) {
			System.exit(0);
		}
		
		if (keyboard.up && !play.hovered) {
			exit.hovered = false;
			play.hovered = true;
		}
		
		if (keyboard.down && !exit.hovered) {
			play.hovered = false;
			exit.hovered = true;
		}
		
		ball.updatePosition();
		ball.updateSpeed(SCREEN_WIDTH, SCREEN_HEIGHT);
		player1.mainMenuMovment(ball);
		player2.mainMenuMovment(ball);
		
		if (collision(player1, ball) || collision(player2, ball)) {
			ball.reverseXDirection();
			if (!muted) {
				beep.playClip(-10, false);
			}
		}
		
		roundReset = true;
	}
	
	private void gameTicks() {
		if (!playCountdown) {
			ball.updatePosition();
			ball.updateSpeed(SCREEN_WIDTH, SCREEN_HEIGHT);
			player2.updatePositionAi(SCREEN_WIDTH, SCREEN_HEIGHT, ball);
			player1.updatePositionPlayer(keyboard, SCREEN_HEIGHT);
			checkAndUpdateScores();
			
			if (keyboard.pause) {
				gameState = gameStates.PAUSED;
				
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if (keyboard.menu) {
				gameState = gameStates.MAINMENU;
			}
			
		} else {
			if (roundReset) {
				player1.setY(-100);
				player2.setY(-100);
				ball.setXandY(SCREEN_WIDTH / 2, 0);
				ball.resetTrail();
				roundReset = false;
			}
			if (player1.getY() < SCREEN_HEIGHT /2) {player1.setY( player1.getY() + player1.getSpeed() );}
			if (player1.getY() < SCREEN_HEIGHT /2) { player2.setY( player1.getY() + player1.getSpeed() );}
			if (ball.getY() < SCREEN_HEIGHT /2) { ball.setY( ball.getY() + player1.getSpeed() );}
		}
		
		if (collision(player1, ball) || collision(player2, ball)) {
			ball.reverseXDirection();
			screenShakeTicks = 20;
			if (!muted) {
				beep.playClip(-10, false);
			}
		}
		
		if (!this.frame.isFocused()) {
			gameState = gameStates.PAUSED;
		}
	}

	private void pausedTicks() {
		if (keyboard.pause || keyboard.menu) {
			gameState = gameStates.GAME;
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void render() {
		 final BufferStrategy buffer = getBufferStrategy();
         if (buffer == null) {
             createBufferStrategy(3);
             return;
         }
         
         final Graphics g = buffer.getDrawGraphics();

         screen.clear();
         
         screen.blit(Art.background, 0, 0);
        
         if(screenShakeTicks > 0){
        	Random rnd = new Random();
        	int x = rnd.nextInt(10);
        	int y = rnd.nextInt(10);
        	g.translate(x, y);
        }
         
         player1.render(screen);
         player2.render(screen);
         ball.render(screen);
         
 		switch (gameState) {
		
 				case MAINMENU:
 		        	 menuRender();
 		        	 break;
 		        	 
 				case GAME:
 					 gameRender();
 			         break;
 			         
 				case PAUSED:
 					 pauseRender();
 					 break;
 				case GAMEOVER:
 					break;
 		}
         
         g.drawImage(screen.image, 0, 0, getWidth(), getHeight() , null);
         
         g.setColor(Color.WHITE);
         g.setFont(new Font("consolas", Font.PLAIN, 32)); 
         
         	if (gameState == gameStates.GAME) {
   			 g.drawString(String.valueOf(player1.getScore()), SCREEN_WIDTH / 5, 50);
   			 g.drawString(String.valueOf(player2.getScore()), SCREEN_WIDTH - SCREEN_WIDTH / 5, 50);
         } else if (gameState == gameStates.GAMEOVER) {
        	  if (player1.getScore() >= 11) {
        		  g.drawString("PLayer 1 Wins!", SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2);
        		  g.setFont(new Font("consolas", Font.PLAIN, 14));
        		  g.drawString("Press Enter The Continue...", SCREEN_WIDTH / 3, (SCREEN_HEIGHT / 2) + 34);
        	  }
        	  if (player2.getScore() >= 11) {
        		  g.drawString("PLayer 2 Wins!", SCREEN_WIDTH / 3, SCREEN_HEIGHT / 2);
        		  g.setFont(new Font("consolas", Font.PLAIN, 14));
        		  g.drawString("Press Enter The Continue...", SCREEN_WIDTH / 3, (SCREEN_HEIGHT / 2) + 34);
        	  }
         }

         g.dispose();
         buffer.show();
    }
	
	private void menuRender() {
		 screen.blit(Art.title,  SCREEN_WIDTH / 2  - Art.title.width / 2, 0);
     	 play.render(screen);
     	 exit.render(screen);
	}
	
	private void gameRender() {
         if (playCountdown) {
			switch (loops) {
				case 0:
					screen.blit(Art.three,   SCREEN_WIDTH / 2  - Art.three.width / 2, 0);
					break;
				case 1: 
					screen.blit(Art.two,     SCREEN_WIDTH / 2  - Art.three.width / 2, 0);
					break;
				case 2:
					screen.blit(Art.one,     SCREEN_WIDTH / 2  - Art.three.width / 2, 0);
					break;
				case 3:
					screen.blit(Art.zero,    SCREEN_WIDTH / 2  - Art.three.width / 2, 0);
					break;
			}
         }
	}
	
	private void pauseRender() {
		 screen.blit(Art.paused,  SCREEN_WIDTH / 2  - Art.paused.width / 2,   SCREEN_HEIGHT / 2  - Art.paused.height / 2);
	}
	
	private boolean collision(final Entity one, final Entity two) {
		return ((one.getY() + one.getH()) <= two.getY() || one.getY() >= (two.getY() + two.getH()) || (one.getX() + one.getW()) <= two.getX() || one.getX() >= (two.getX() + two.getH()) ) ? false : true;
	}
	
	private void checkAndUpdateScores() {
		if (ball.getX() < 0 ) {
			player2.updateScore();
			playCountdown = true;
			roundReset = true;
		}
		
		if (ball.getX() + ball.getW() >= SCREEN_WIDTH) {
			player1.updateScore();
			playCountdown = true;
			roundReset = true;
		}
		
		if (player1.getScore() >= 11 || player2.getScore() >= 11) {
			gameState = gameStates.GAMEOVER;
		}
	}
	
	public void start() {
		  thread = new Thread(this, "Pong");
		  thread.start();
		  running = true;
	}
	
    public void stop() {
      try {
		thread.join();
	  } catch (InterruptedException e) {
		  LOG.log(null, "Could Not Stop Thread!", e);
	  }
	}

	public static void main(final String[] args) {
       new Pong().start();
	}

}
