import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private Clip clip;

    public SoundPlayer(String filePath) {
        try {
            clip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition(0); 
            clip.start(); 
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); 
        }
    }
}