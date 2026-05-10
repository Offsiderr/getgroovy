package seng201.team67.services.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import seng201.team67.behaviours.SkillBehaviours;
import seng201.team67.interfaces.PayoutModifier;
import seng201.team67.interfaces.StatModifier;
import seng201.team67.models.Skill;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;
import seng201.team67.models.enums.Rarity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SkillLoaderService {

    private static final String resourcePath = "/data/skills.json";
    private final List<Skill> allSkills;

    public SkillLoaderService()
    {
        this.allSkills = loadAll();
    }

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

        StatModifier statModifier = buildStatModifier(effectsNode, multiplier);
        PayoutModifier payoutModifier = buildPayoutModifier(effectsNode, multiplier);

        return new Skill(id, name, description, artistType, rarity, statModifier, payoutModifier);
    }

    private StatModifier buildStatModifier(JsonNode effectsNode, double multiplier)
    {
        StatModifier statModifier = null;

        for (JsonNode effectNode : effectsNode)
        {
            StatModifier nextModifier = mapStatModifier(effectNode.asText(), multiplier);
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
            statModifier = artist -> currentModifier.apply(artist) + nextModifier.apply(artist);
        }

        return statModifier;
    }

    private PayoutModifier buildPayoutModifier(JsonNode effectsNode, double multiplier)
    {
        PayoutModifier payoutModifier = null;

        for (JsonNode effectNode : effectsNode)
        {
            PayoutModifier nextModifier = mapPayoutModifier(effectNode.asText(), multiplier);
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
            payoutModifier = (artist, basePayout) -> nextModifier.apply(artist, currentModifier.apply(artist, basePayout));
        }

        return payoutModifier;
    }

    private StatModifier mapStatModifier(String effect, double multiplier)
    {
        return switch (effect) {
            case "FLAT_STAMINA_BOOST" -> SkillBehaviours.flatStaminaBoost((int) multiplier);
            case "FLAT_STAR_POWER_BOOST" -> SkillBehaviours.flatStarPowerBoost((int) multiplier);
            case "STAMINA_COST_REDUCTION" -> SkillBehaviours.staminaCostReduction(multiplier);
            case "RETIREMENT_RISK" -> SkillBehaviours.retirementRisk();
            case "FLAT_CREDIT_BONUS", "PAYOUT_MULTIPLIER", "GREAT_PAYOUT_MULTIPLIER",
                    "TERRIBLE_PAYOUT_REDUCTION" -> null;
            default -> throw new IllegalArgumentException("Unknown skill effect: " + effect);
        };
    }

    private PayoutModifier mapPayoutModifier(String effect, double multiplier)
    {
        return switch (effect) {
            case "FLAT_CREDIT_BONUS" -> SkillBehaviours.flatCreditBonus((int) multiplier);
            case "PAYOUT_MULTIPLIER" -> SkillBehaviours.payoutMultiplier(multiplier);
            case "GREAT_PAYOUT_MULTIPLIER" -> SkillBehaviours.greatPayoutMultiplier(multiplier);
            case "TERRIBLE_PAYOUT_REDUCTION" -> SkillBehaviours.terriblePayoutReduction(multiplier);
            case "FLAT_STAMINA_BOOST", "FLAT_STAR_POWER_BOOST", "STAMINA_COST_REDUCTION",
                    "RETIREMENT_RISK" -> null;
            default -> throw new IllegalArgumentException("Unknown skill effect: " + effect);
        };
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
