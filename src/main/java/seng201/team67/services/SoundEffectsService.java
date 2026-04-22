package seng201.team67.services;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundEffectsService {

    private final String yesPath = "/sound/sfx/Yes.wav";
    private final String noPath = "/sound/sfx/No.wav";

    public void playYes() {
        play(yesPath);
    }

    public void playNo() {
        play(noPath);
    }

    public static void play(String resourcePath) {
        try {
            URL url = SoundEffectsService.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("Audio file not found: " + resourcePath);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Failed to play audio: " + e.getMessage());
        }
    }
}