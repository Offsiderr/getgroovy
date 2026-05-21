package seng201.team67.services.audio;

import seng201.team67.GameEnvironment;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Provides sound effects operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class SoundEffectsService {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;

    /** Constant that defines the max volume percent. */
    private static final double MAX_VOLUME_PERCENT = 100.0;

    /** Text value for the yes path. */
    private final String yesPath = "/sound/sfx/YES.wav";
    /** Text value for the no path. */
    private final String noPath = "/sound/sfx/NO.wav";
    private final String cardPath = "/sound/sfx/card.wav";
    // Added sound effect for opening the gacha crate //
    private final String openCratePath = "/sound/sfx/opencrate.wav";

    /**
     * Creates a new sound effects service.
     * @param gameEnvironment the active game environment
     */
    public SoundEffectsService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Plays the yes sfx.
     */
    public void playYes() {
        play(yesPath, getConfiguredEffectsVolume());
    }

    public void playCard() {
        System.out.println("playing card sound");
        play(cardPath, getConfiguredEffectsVolume());
    }

    // Plays when the gacha crate is clicked //
    public void playOpenCrate() {
        play(openCratePath, getConfiguredEffectsVolume());
    }

    public void playNo() {
        play(noPath, getConfiguredEffectsVolume());
    }

    /**
     * Plays the sound effect
     * @param resourcePath the text value for the resource path
     */
    public static void play(String resourcePath) {
        play(resourcePath, MAX_VOLUME_PERCENT);
        public void playOpenCrate() {
    }

    /**
     * Calculates the effective volume.
     * @param mainVolume the numeric value for the main volume
     * @param soundEffectsVolume the numeric value for the sound effects volume
     * @return The effective volume.
     */
    public static double calculateEffectiveVolume(double mainVolume, double soundEffectsVolume) {
        double clampedMainVolume = clampVolume(mainVolume);
        double clampedEffectsVolume = clampVolume(soundEffectsVolume);
        return (clampedMainVolume * clampedEffectsVolume) / MAX_VOLUME_PERCENT;
    }

    private double getConfiguredEffectsVolume() {
        return calculateEffectiveVolume(
                gameEnvironment.getConfig().mainVolume,
                gameEnvironment.getConfig().soundEffectsVolume
        );
    }

    private static double clampVolume(double volume) {
        return Math.max(0.0, Math.min(MAX_VOLUME_PERCENT, volume));
    }

    /**
     * Plays the sound effect
     * It updates related state as needed while performing the operation.
     * @param resourcePath the text value for the resource path
     * @param volumePercent the numeric value for the volume percent
     */
    public static void play(String resourcePath, double volumePercent) {
        if (clampVolume(volumePercent) <= 0.0) {
            return;
        }

        try {
            URL url = SoundEffectsService.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("Audio file not found: " + resourcePath);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            applyVolume(clip, volumePercent);
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

    private static void applyVolume(Clip clip, double volumePercent) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        double clampedVolume = clampVolume(volumePercent);
        float gain;

        if (clampedVolume <= 0.0) {
            gain = gainControl.getMinimum();
        } else {
            double gainDb = 20.0 * Math.log10(clampedVolume / MAX_VOLUME_PERCENT);
            gain = (float) Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), gainDb));
        }

        gainControl.setValue(gain);
    }
}
