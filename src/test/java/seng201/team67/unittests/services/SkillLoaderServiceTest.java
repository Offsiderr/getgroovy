package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.models.Skill;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.enums.GameplayEffect;
import seng201.team67.models.enums.questions.PayoutType;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.services.data.SkillLoaderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SkillLoaderServiceTest {

    @Test
    void loadAllUsesExplicitEffectTypesForSpecialPayoutSkills() {
        SkillLoaderService service = new SkillLoaderService();

        assertTrue(findSkill(service, "QUICK_LEARNER").hasEffect(GameplayEffect.OK_PAYOUT_MULTIPLIER));
        assertTrue(findSkill(service, "HEADLINER").hasEffect(GameplayEffect.SOLO_PAYOUT_MULTIPLIER));
        assertTrue(findSkill(service, "COLLAB_KING").hasEffect(GameplayEffect.COLLAB_PAYOUT_MULTIPLIER));
        assertTrue(findSkill(service, "ENCORE_MACHINE").hasEffect(GameplayEffect.ENCORE_PAYOUT_MULTIPLIER));
        assertTrue(findSkill(service, "AMP_IT_UP").hasEffect(GameplayEffect.TOUR_PROGRESS_PAYOUT_MULTIPLIER));
    }

    @Test
    void loadAllBuildsQuickLearnerPayoutModifierFromEffectEnum() {
        Skill skill = findSkill(new SkillLoaderService(), "QUICK_LEARNER");
        Artist artist = new Popstar("Test Pop", 3, "Test");
        Outcome okOutcome = new Outcome(1, "Solid show", PayoutType.MINOR_REWARD, 0, 0, false);
        Outcome greatOutcome = new Outcome(1, "Huge show", PayoutType.MAJOR_REWARD, 0, 0, false);

        assertEquals(110, skill.getPayoutModifier().apply(artist, 100, okOutcome, List.of(artist), 50, 0, 1, 5));
        assertEquals(100, skill.getPayoutModifier().apply(artist, 100, greatOutcome, List.of(artist), 50, 0, 1, 5));
    }

    @Test
    void loadAllBuildsSpecialPayoutModifiersWithoutSkillIdChecks() {
        SkillLoaderService service = new SkillLoaderService();
        Artist popstar = new Popstar("Test Pop", 3, "Test");
        Artist rapper = new Rapper("Test Rap", 3, "Test");
        Outcome ongoingOutcome = new Outcome(1, "Solid show", PayoutType.MINOR_REWARD, 0, 0, false);
        Outcome finaleOutcome = new Outcome(1, "Finale", PayoutType.MINOR_REWARD, 0, 0, true);

        assertEquals(115, findSkill(service, "HEADLINER")
                .getPayoutModifier()
                .apply(popstar, 100, ongoingOutcome, List.of(popstar), 50, 0, 1, 5));
        assertEquals(115, findSkill(service, "COLLAB_KING")
                .getPayoutModifier()
                .apply(rapper, 100, ongoingOutcome, List.of(popstar, rapper), 50, 0, 1, 5));
        assertEquals(125, findSkill(service, "ENCORE_MACHINE")
                .getPayoutModifier()
                .apply(popstar, 100, finaleOutcome, List.of(popstar, rapper), 75, 0, 5, 5));
        assertEquals(120, findSkill(service, "AMP_IT_UP")
                .getPayoutModifier()
                .apply(popstar, 100, ongoingOutcome, List.of(popstar, rapper), 50, 2, 3, 5));
    }

    private Skill findSkill(SkillLoaderService service, String skillId)
    {
        return service.getAllSkills().stream()
                .filter(skill -> skillId.equals(skill.getId()))
                .findFirst()
                .orElseThrow();
    }
}
