package org.crazystudios.sound;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.crazystudios.pong.Pong;

public class Sound {
	
	private transient static final Logger LOG = Logger.getLogger(Pong.class.getName());
	private transient Clip clip;
	
	public Sound(final String sound) {
        try {
        	final File soundFile = new File( sound);
        	final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( soundFile );
        	clip = AudioSystem.getClip();
        	clip.open(audioInputStream);
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			 LOG.log(null, "Cant Load Audio!", e);
		}
	}
	
	public void playClip(final float ajust, final boolean loop) {
		final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(ajust);
	    clip.setFramePosition(0);
		this.clip.start();
		
		if (loop) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public void stopClip() {
		this.clip.stop();
	}
	
}