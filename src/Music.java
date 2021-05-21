import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
public class Music
{
	Long currentFrame;
	Clip clip;
	String status;
	AudioInputStream audioInputStream;
	public void play(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
		clip = AudioSystem.getClip();
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		status = "play";
	}
	public void stop() {
		clip.stop();
		status="paused";
	}
}