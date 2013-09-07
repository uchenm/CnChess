/**
 * Copyright 2013  Ming Chen<uchenm@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package chess.utils;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicUtil {

    public static synchronized void playSound(final String sound) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    File soundFile = new File("res/sound/" + sound + ".wav");
                    // AudioInputStream inputStream =
                    // AudioSystem.getAudioInputStream(
                    // Main.class.getResourceAsStream("/path/to/sounds/" +
                    // url));
                    AudioInputStream inputStream = AudioSystem
                            .getAudioInputStream(soundFile);
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static synchronized void playBackGroundMusic(final String sound) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    File soundFile = new File("res/sound/" + sound + ".mid");
                    // AudioInputStream inputStream =
                    // AudioSystem.getAudioInputStream(
                    // Main.class.getResourceAsStream("/path/to/sounds/" +
                    // url));
                    AudioInputStream inputStream = AudioSystem
                            .getAudioInputStream(soundFile);
                    clip.open(inputStream);
                    clip.loop(-1);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        MusicUtil.playSound("go");
    }

}
