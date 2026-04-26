package seng201.team67.services;

import seng201.team67.models.enums.Minigame;
import seng201.team67.models.minigames.SoundEngineerStandoff;

public class MinigamesService {

    public SoundEngineerStandoff soundEngineerStandoff;

    public Minigame minigame;

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
