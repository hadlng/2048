package util;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import game.GameBoard;

public class AudioManager {

  GameBoard board;

  public void play(String fileName, int loopCount) {
    try {
      // using .wav files with bit-rate of 1411kbps
      // other formats might not work

      // https://stackoverflow.com/a/11001150 for why using File
      // File soundFile = new File("src/main/resources/sound/" + fileName + ".wav");
      InputStream is = AudioManager.class.getResourceAsStream("/sounds/" + fileName + ".wav");
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
      Clip clip = AudioSystem.getClip(); // get a sound clip resourse
      // AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile); // open an audio input stream

      // prevent clip from being blocked https://stackoverflow.com/a/17277981
      // not using clip.drain() because it will freeze the UI
      clip.addLineListener(new LineListener() {
        @Override
        public void update(LineEvent event) {
          if (event.getType() == LineEvent.Type.STOP)
            clip.close();
        }
      });

      clip.open(inputStream); // open audio clip and load samples from the audio input stream

      // 0 ==> loop once
      // -1 ==> loop continuously
      clip.loop(loopCount);
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }
  }

}
