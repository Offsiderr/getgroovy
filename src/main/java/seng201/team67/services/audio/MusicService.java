package seng201.team67.services.audio;

import seng201.team67.GameEnvironment;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;


/**
 * Provides the music for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MusicService {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;

    /** Text value for the studio music path. */
    private final String studioMusicPath = "/sound/Music/MarketThemePlaceholder.wav";
    /** Text value for the local tour music path. */
    private final String localTourMusicPath = "/sound/Music/Local_tour.wav";

    /** Whether currently playing. */
    private Boolean currentlyPlaying = false;

    /** Constant that defines the max volume percent. */
    private static final double MAX_VOLUME_PERCENT = 100.0;

    /** The current clip. */
    private static Clip currentClip = null;

    /**
     * Creates a new music service.
     * @param gameEnvironment the active game environment
     */
    public MusicService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Plays the the studio music.
     */
    public void playTheStudioMusic()
    {
        if (!currentlyPlaying) {
            currentlyPlaying = true;
            play(studioMusicPath, getConfiguredMusicVolume());
        }
    }

    /**
     * Plays the local tour music.
     */
    public void playLocalTourMusic()
    {
        stop();
        currentlyPlaying = true;
        play(localTourMusicPath, getConfiguredMusicVolume());
    }

    /**
     * Stops the music.
     */
    public static void stop()
    {
        if (currentClip != null && currentClip.isRunning())
        {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }

    /**
     * Stops the music and sets currentlyPlaying to false
     */
    public void stopAndReset()
    {
        stop();
        currentlyPlaying = false;
    }

    /**
     * Plays music
     * @param resourcePath the text value for the resource path
     */
    public static void play(String resourcePath) {
        play(resourcePath, MAX_VOLUME_PERCENT);
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

    private double getConfiguredMusicVolume() {
        return calculateEffectiveVolume(
                gameEnvironment.getConfig().mainVolume,
                gameEnvironment.getConfig().musicVolume
        );
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
     * Plays music
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
            currentClip = clip;
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Failed to play audio: " + e.getMessage());
        }
    }

    /**
     * Applies the selected volume settings set by the player to the music
     * @param clip
     * @param volumePercent
     */
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