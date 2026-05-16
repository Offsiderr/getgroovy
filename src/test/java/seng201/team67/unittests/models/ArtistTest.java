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
    void popstarConstructorAndGettersReturnExpectedValues() {
        Popstar artist = new Popstar("Dua Lipa", 3, "Pop artist");

        assertEquals("Dua Lipa", artist.getName());
        assertEquals(3, artist.getStarPower());
        assertEquals(3, artist.getSkillLevel());
        assertEquals(100, artist.getHealth());
        assertEquals(100, artist.getStamina());
        assertEquals("Pop artist", artist.getDescription());
        assertEquals("Popstar", artist.getType());
        assertEquals(15.0, artist.getPay());
        assertEquals(21.0, artist.getCost());
        assertEquals("/images/Artists/Dua Lipa.png", artist.getImagePath());
    }

    @Test
    void rapperConstructorAndGettersReturnExpectedValues()
    {
        Rapper artist = new Rapper("Louie Campion", 5, "The author of these tests");

        assertEquals("Louie Campion", artist.getName());
        assertEquals(5, artist.getStarPower());
        assertEquals(5, artist.getSkillLevel());
        assertEquals(100, artist.getHealth());
        assertEquals(120, artist.getStamina());
        assertEquals("The author of these tests", artist.getDescription());
        assertEquals("Rapper", artist.getType());
        assertEquals(25.0, artist.getPay());
        assertEquals(35.0, artist.getCost());
        assertEquals("/images/Artists/Louie Campion.png", artist.getImagePath());

    }

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
    void setHealthUpdatesArtistHealth() {
        Popstar artist = new Popstar("Olivia Rodrigo", 2, "Pop artist");

        artist.setHealth(45);

        assertEquals(45, artist.getHealth());
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
    void skillFlatStarPowerBoostAppliesToDisplayedStarPower() {
        Rockstar artist = new Rockstar("Muse", 3, "Rock artist");
        Skill skill = new Skill(
                "RIFF_LORD",
                "Riff Lord",
                "Boosts star power",
                "ROCKSTAR",
                Rarity.RARE,
                10,
                List.of(GameplayEffect.FLAT_STAR_POWER_BOOST),
                SkillEffectBehaviours.flatStarPowerBoost(10),
                null
        );

        artist.setSkill(skill);

        assertEquals(13, artist.getStarPower());
    }

    @Test
    void skillFlatStaminaBoostAppliesWhenSkillIsAssigned() {
        Rapper artist = new Rapper("Yeat", 2, "Rap artist");
        Skill skill = new Skill(
                "GRIND_MENTALITY",
                "Grind Mentality",
                "Boosts stamina",
                "RAPPER",
                Rarity.COMMON,
                10,
                List.of(GameplayEffect.FLAT_STAMINA_BOOST),
                SkillEffectBehaviours.flatStaminaBoost(10),
                null
        );

        artist.setSkill(skill);

        assertEquals(130, artist.getBaseStamina());
        assertEquals(130, artist.getStamina());
    }
}
