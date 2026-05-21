package seng201.team67.services.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.Skill;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;
import seng201.team67.models.enums.GameplayEffect;
import seng201.team67.models.enums.Rarity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides skill loader operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class SkillLoaderService {

    /** Text value for the resource path. */
    private static final String resourcePath = "/data/skills.json";
    /** Collection that stores the all skills. */
    private final List<Skill> allSkills;

    /**
     * Creates a new skill loader service.
     */
    public SkillLoaderService()
    {
        this.allSkills = loadAll();
    }

    /**
     * Returns the eligible skills.
     * @param artist the artist
     * @return The eligible skills.
     */
    public List<Skill> getEligibleSkills(Artist artist)
    {
        List<Skill> eligibleSkills = new ArrayList<>();

        for (Skill skill : allSkills)
        {
            if (isEligibleArtistType(artist, skill.getArtistType()))
            {
                eligibleSkills.add(skill);
            }
        }

        return eligibleSkills;
    }

    /**
     * Returns the all skills.
     * @return The all skills.
     */
    public List<Skill> getAllSkills()
    {
        return new ArrayList<>(allSkills);
    }


    private List<Skill> loadAll()
    {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("skills.json not found at " + resourcePath);
            }

            List<Skill> skills = new ArrayList<>();
            JsonNode root = mapper.readTree(is);

            for (JsonNode skillNode : root)
            {
                skills.add(buildSkill(skillNode));
            }

            return skills;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load skills: " + e.getMessage(), e);
        }
    }


    private Skill buildSkill(JsonNode skillNode)
    {
        String id = skillNode.path("id").asText();
        String name = skillNode.path("name").asText();
        String description = skillNode.path("description").asText();
        String artistType = skillNode.path("artistType").asText();
        Rarity rarity = Rarity.valueOf(skillNode.path("rarity").asText());
        double multiplier = skillNode.path("multiplier").asDouble();
        JsonNode effectsNode = skillNode.path("effects");
        List<GameplayEffect> effects = buildEffects(effectsNode);

        StatModifier statModifier = buildStatModifier(effects, multiplier);
        PayoutModifier payoutModifier = buildPayoutModifier(effects, multiplier);

        return new Skill(id, name, description, artistType, rarity, multiplier, effects, statModifier, payoutModifier);
    }

    private List<GameplayEffect> buildEffects(JsonNode effectsNode)
    {
        List<GameplayEffect> effects = new ArrayList<>();

        for (JsonNode effectNode : effectsNode)
        {
            effects.add(GameplayEffect.valueOf(effectNode.asText()));
        }

        return effects;
    }

    private StatModifier buildStatModifier(List<GameplayEffect> effects, double multiplier)
    {
        StatModifier statModifier = null;

        for (GameplayEffect effect : effects)
        {
            StatModifier nextModifier = effect.createStatModifier(multiplier);
            if (nextModifier == null)
            {
                continue;
            }

            if (statModifier == null)
            {
                statModifier = nextModifier;
                continue;
            }

            StatModifier currentModifier = statModifier;
            statModifier = (artist, value) -> currentModifier.apply(artist, value) + nextModifier.apply(artist, value);
        }

        return statModifier;
    }

    private PayoutModifier buildPayoutModifier(List<GameplayEffect> effects, double multiplier)
    {
        PayoutModifier payoutModifier = null;

        for (GameplayEffect effect : effects)
        {
            PayoutModifier nextModifier = effect.createPayoutModifier(multiplier);
            if (nextModifier == null)
            {
                continue;
            }

            if (payoutModifier == null)
            {
                payoutModifier = nextModifier;
                continue;
            }

            PayoutModifier currentModifier = payoutModifier;
            payoutModifier = (artist, basePayout, outcome, lineup, crowdEnergy, completedConcerts, eventNumber, totalEvents) ->
                    nextModifier.apply(
                            artist,
                            currentModifier.apply(
                                    artist,
                                    basePayout,
                                    outcome,
                                    lineup,
                                    crowdEnergy,
                                    completedConcerts,
                                    eventNumber,
                                    totalEvents
                            ),
                            outcome,
                            lineup,
                            crowdEnergy,
                            completedConcerts,
                            eventNumber,
                            totalEvents
                    );
        }

        return payoutModifier;
    }

    private boolean isEligibleArtistType(Artist artist, String artistType)
    {
        return switch (artistType) {
            case "ANY" -> true;
            case "POPSTAR" -> artist instanceof Popstar;
            case "RAPPER" -> artist instanceof Rapper;
            case "ROCKSTAR" -> artist instanceof Rockstar;
            default -> throw new IllegalArgumentException("Unknown artist type: " + artistType);
        };
    }
}
