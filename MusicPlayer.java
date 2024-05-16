import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
  private Clip clip;

  public void playMusic(String musicFilePath, float volume) {
    try {
      File musicFile = new File(musicFilePath);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
      clip = AudioSystem.getClip();
      clip.open(audioStream);
      setVolume(volume);
      clip.loop(Clip.LOOP_CONTINUOUSLY); // 循环播放
      clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }
  public void skillMusic(String musicFilePath, float volume) {
    try {
      File musicFile = new File(musicFilePath);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
      clip = AudioSystem.getClip();
      clip.open(audioStream);
      setVolume(volume);
      clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public void stopMusic() {
    if (clip != null && clip.isRunning()) {
      clip.stop();
      clip.close();
    }
  }

  private void setVolume(float volume) {
    if (clip != null) {
      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(volume); // 设置音量，值范围为 (min, max)
    }
  }
}
