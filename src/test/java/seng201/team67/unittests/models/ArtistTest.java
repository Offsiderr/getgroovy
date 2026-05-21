package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.behaviours.SkillEffectBehaviours;
import seng201.team67.models.Skill;
import seng201.team67.models.enums.GameplayEffect;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArtistTest {

    @Test
    void rapperUsesItsDefaultStaminaValues() {
        Rapper artist = new Rapper("Polo G", 2, "Rap artist");

        assertEquals(120, artist.getStamina());
        assertEquals(100, artist.getHealth());
        assertEquals("Rapper", artist.getType());
    }

    @Test
    void rockstarUsesItsDefaultStaminaValues() {
        Rockstar artist = new Rockstar("MUSE", 1, "Rock artist");

        assertEquals(80, artist.getStamina());
        assertEquals(100, artist.getHealth());
        assertEquals("Rockstar", artist.getType());
    }

    @Test
    void setStaminaClampsAboveBaseStamina() {
        Rapper artist = new Rapper("Yeat", 2, "Rap artist");

        artist.setStamina(999);

        assertEquals(120, artist.getStamina());
    }

    @Test
    void setStaminaClampsBelowZero() {
        Rockstar artist = new Rockstar("Foo Fighters", 3, "Rock artist");

        artist.setStamina(-5);

        assertEquals(0, artist.getStamina());
    }

    @Test
    void changeStarPowerClampsWithinOneAndFive() {
        Popstar artist = new Popstar("Olivia Rodrigo", 3, "Pop artist");

        artist.changeStarPower(10);
        assertEquals(5, artist.getBaseStarPowerValue());

        artist.changeStarPower(-10);
        assertEquals(1, artist.getBaseStarPowerValue());
    }

    @Test
    void retirementRiskSkillReducesEffectiveToleranceByFortyPercent() {
        Rockstar artist = new Rockstar("Muse", 1, "Rock artist");
        Skill burnoutProne = new Skill(
                "BURNOUT_PRONE",
                "Burnout Prone",
                "Reduces tolerance.",
                "ROCKSTAR",
                Rarity.RARE,
                20,
                List.of(GameplayEffect.RETIREMENT_RISK),
                null,
                null
        );

        artist.setSkill(burnoutProne);

        assertEquals(60, artist.getTolerance());
        assertEquals(60, artist.getHealth());
        assertEquals(100, artist.getBaseHealthValue());
    }

}
