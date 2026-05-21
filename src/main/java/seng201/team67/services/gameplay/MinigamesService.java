package seng201.team67.services.gameplay;

import seng201.team67.models.enums.Minigame;
import seng201.team67.models.minigames.SoundEngineerStandoff;

/**
 * Provides minigames operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MinigamesService {

    /** The sound engineer standoff. */
    public SoundEngineerStandoff soundEngineerStandoff;

    /** The minigame. */
    public Minigame minigame;

    /**
     * Creates a new minigames service.
     * @param minigame the minigame
     */
    public MinigamesService(Minigame minigame)
    {
        this.minigame = minigame;

        switch (minigame)
        {
            case SOUNDENGINEER:
                soundEngineerStandoff = new SoundEngineerStandoff();
        }
    };
}
