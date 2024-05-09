import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * The SoundPlayer class is responsible for playing audio files.
 * It uses javax.sound.sampled library to handle audio playback.
 */
public class SoundPlayer {
    private Clip clip;

    /**
     * Constructs a SoundPlayer object with the specified audio file path.
     * @param filePath The file path of the audio file to be played.
     */
    public SoundPlayer(String filePath) {
        try {
            clip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the audio clip if it's not already running.
     */
    public void play() {
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition(0); 
            clip.start(); 
        }
    }

    /**
     * Stops the audio clip if it's running.
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); 
        }
    }
}