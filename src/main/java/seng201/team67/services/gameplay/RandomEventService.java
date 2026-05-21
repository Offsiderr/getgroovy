package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.GameConfig;
import seng201.team67.models.Skill;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.EventType;
import seng201.team67.models.enums.RandomEvent;
import seng201.team67.services.data.SkillLoaderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Provides random event operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class RandomEventService {

    /** The random. */
    private final Random random;
    /** Service used to load eligible skills. */
    private final SkillLoaderService skillLoaderService = new SkillLoaderService();

    /**
     * Creates a new random event service.
     */
    public RandomEventService() {
        this(new Random());
    }

    /**
     * Creates a new random event service.
     * @param random the random
     */
    public RandomEventService(Random random) {
        this.random = random;
    }

    /**
     * Processes the trigger random event.
     * @param gameEnvironment the active game environment
     * @return True if trigger random event, otherwise false.
     */
    public boolean shouldTriggerRandomEvent(GameEnvironment gameEnvironment) {
        return shouldTriggerRandomEvent(gameEnvironment.getConfig(), Math.random());
    }

    /**
     * Processes the trigger random event.
     * @param gameConfig the game config
     * @param roll the numeric value for the roll
     * @return True if trigger random event, otherwise false.
     */
    public boolean shouldTriggerRandomEvent(GameConfig gameConfig, double roll) {
        return roll < gameConfig.randomEventTriggerChance;
    }

    /**
     * Returns the random affected artist.
     * @param gameEnvironment the active game environment
     * @return The random affected artist.
     */
    public Artist getRandomAffectedArtist(GameEnvironment gameEnvironment) {
        return getRandomAffectedArtist(gameEnvironment.getLabelService().getAllArtists());
    }

    /**
     * Returns the random affected artist.
     * @param gameEnvironment the active game environment
     * @param randomEvent the random event
     * @return The random affected artist.
     */
    public Artist getRandomAffectedArtist(GameEnvironment gameEnvironment, RandomEvent randomEvent) {
        return getRandomAffectedArtist(randomEvent, gameEnvironment.getLabelService().getAllArtists());
    }

    /**
     * Rolls the retirement artist.
     * It uses the current game state and supplied context when producing the result.
     * @param gameEnvironment the active game environment
     * @return The resulting artist, or `null` if no value is available.
     */
    public Artist rollRetirementArtist(GameEnvironment gameEnvironment) {
        List<Artist> artists = new ArrayList<>(gameEnvironment.getLabelService().getAllArtists());

        for (Artist artist : artists) {
            if (!doesArtistRetire(artist)) {
                continue;
            }

            if (gameEnvironment.getLabelService().retireArtist(artist)) {
                return artist;
            }
        }

        return null;
    }

    /**
     * Returns the random affected artist.
     * @param artists the list of artists
     * @return The resolved random affected artist, or `null` if no value is available.
     */
    public Artist getRandomAffectedArtist(List<Artist> artists) {
        if (artists == null || artists.isEmpty()) {
            return null;
        }

        return artists.get(random.nextInt(artists.size()));
    }

    /**
     * Returns the random affected artist.
     * @param randomEvent the random event
     * @param artists the list of artists
     * @return The resolved random affected artist, or `null` if no value is available.
     */
    public Artist getRandomAffectedArtist(RandomEvent randomEvent, List<Artist> artists) {
        List<Artist> eligibleArtists = getEligibleArtists(randomEvent, artists);
        if (eligibleArtists.isEmpty()) {
            return null;
        }

        return eligibleArtists.get(random.nextInt(eligibleArtists.size()));
    }

    /**
     * Applies the random event.
     * It updates related state as needed while performing the operation.
     * @param gameEnvironment the active game environment
     * @param randomEvent the random event
     * @param affectedArtist the affected artist
     * @return True if random event, otherwise false.
     */
    public boolean applyRandomEvent(GameEnvironment gameEnvironment, RandomEvent randomEvent, Artist affectedArtist) {
        if (randomEvent == null || affectedArtist == null) {
            return false;
        }

        if (randomEvent.getType() == EventType.RETIREMENT) {
            boolean retired = gameEnvironment.getLabelService().retireArtist(affectedArtist);
            if (!retired) {
                return false;
            }

            gameEnvironment.getLabelService().giveMoney(randomEvent.getValue());
            return true;
        }

        if (randomEvent.getType() == EventType.SKILL) {
            if (randomEvent == RandomEvent.BREAKTHROUGH_SESSION) {
                return applyNewSkillEvent(affectedArtist);
            }

            affectedArtist.changeSkillLevel(randomEvent.getValue());
            return true;
        }

        return applyStatChange(randomEvent, affectedArtist);
    }

    private boolean applyStatChange(RandomEvent randomEvent, Artist affectedArtist) {
        if (randomEvent.getStat() == null) {
            return false;
        }

        return switch (randomEvent.getStat()) {
            case "stamina" -> {
                int before = affectedArtist.getCurrentStaminaValue();
                affectedArtist.setStamina(before + randomEvent.getValue());
                yield affectedArtist.getCurrentStaminaValue() != before;
            }
            case "star_power" -> {
                int before = affectedArtist.getBaseStarPowerValue();
                affectedArtist.changeStarPower(randomEvent.getValue());
                yield affectedArtist.getBaseStarPowerValue() != before;
            }
            default -> false;
        };
    }

    private boolean doesArtistRetire(Artist artist) {
        if (artist == null) {
            return false;
        }

        int retirementChance = Math.max(0, artist.getRetirementChance());
        if (retirementChance <= 0) {
            return false;
        }

        if (retirementChance >= 100) {
            return true;
        }

        return random.nextInt(100) < retirementChance;
    }

    /**
     * Returns the weighted random event.
     * It derives the value from the current state before returning it.
     * @return The weighted random event.
     */
    public RandomEvent getWeightedRandomEvent() {
        RandomEvent[] events = RandomEvent.values();
        int totalWeight = 0;

        for (RandomEvent event : events) {
            totalWeight += event.getWeight();
        }

        int roll = random.nextInt(totalWeight);
        int cumulativeWeight = 0;
        for (RandomEvent event : events) {
            cumulativeWeight += event.getWeight();
            if (roll < cumulativeWeight) {
                return event;
            }
        }

        return events[events.length - 1];
    }

    /**
     * Returns a weighted random event.
     * @param gameEnvironment the active game environment
     * @return The weighted random event.
     */
    public RandomEvent getWeightedRandomEvent(GameEnvironment gameEnvironment) {
        return getWeightedRandomEvent(gameEnvironment.getLabelService().getAllArtists());
    }

    /**
     * Returns a weighted random event.
     * @param artists the list of artists
     * @return The resolved weighted random event, or `null` if no value is available.
     */
    public RandomEvent getWeightedRandomEvent(List<Artist> artists) {
        RandomEvent[] events = RandomEvent.values();
        List<RandomEvent> eligibleEvents = new ArrayList<>();
        int totalWeight = 0;

        for (RandomEvent event : events) {
            if (!isEventEligible(event, artists)) {
                continue;
            }

            eligibleEvents.add(event);
            totalWeight += event.getWeight();
        }

        if (eligibleEvents.isEmpty()) {
            return null;
        }

        int roll = random.nextInt(totalWeight);
        int cumulativeWeight = 0;
        for (RandomEvent event : eligibleEvents) {
            cumulativeWeight += event.getWeight();
            if (roll < cumulativeWeight) {
                return event;
            }
        }

        return eligibleEvents.get(eligibleEvents.size() - 1);
    }

    private boolean isEventEligible(RandomEvent randomEvent, List<Artist> artists) {
        return !getEligibleArtists(randomEvent, artists).isEmpty();
    }

    private List<Artist> getEligibleArtists(RandomEvent randomEvent, List<Artist> artists) {
        if (randomEvent == null || artists == null || artists.isEmpty()) {
            return List.of();
        }

        return switch (randomEvent.getType()) {
            case RETIREMENT -> artists.size() > 1 ? new ArrayList<>(artists) : List.of();
            case STAT -> getEligibleArtistsForStatEvent(randomEvent, artists);
            case SKILL -> getEligibleArtistsForSkillEvent(randomEvent, artists);
        };
    }

    private List<Artist> getEligibleArtistsForSkillEvent(RandomEvent randomEvent, List<Artist> artists) {
        if (randomEvent != RandomEvent.BREAKTHROUGH_SESSION) {
            return new ArrayList<>(artists);
        }

        List<Artist> eligibleArtists = new ArrayList<>();
        for (Artist artist : artists) {
            if (canArtistLearnNewSkill(artist)) {
                eligibleArtists.add(artist);
            }
        }
        return eligibleArtists;
    }

    private List<Artist> getEligibleArtistsForStatEvent(RandomEvent randomEvent, List<Artist> artists) {
        if (!"star_power".equals(randomEvent.getStat())) {
            return new ArrayList<>(artists);
        }

        List<Artist> eligibleArtists = new ArrayList<>();
        for (Artist artist : artists) {
            int baseStarPower = artist.getBaseStarPowerValue();
            if (baseStarPower > 1 && baseStarPower < 5) {
                eligibleArtists.add(artist);
            }
        }
        return eligibleArtists;
    }

    private boolean applyNewSkillEvent(Artist affectedArtist) {
        Skill learnedSkill = getRandomLearnableSkill(affectedArtist);
        if (learnedSkill == null) {
            return false;
        }

        affectedArtist.setSkill(learnedSkill);
        return true;
    }

    private boolean canArtistLearnNewSkill(Artist artist) {
        return getRandomLearnableSkill(artist) != null;
    }

    private Skill getRandomLearnableSkill(Artist artist) {
        if (artist == null) {
            return null;
        }

        List<Skill> eligibleSkills = new ArrayList<>(skillLoaderService.getEligibleSkills(artist));
        Skill currentSkill = artist.getSkill();
        if (currentSkill != null) {
            eligibleSkills.removeIf(skill -> skill.getId().equals(currentSkill.getId()));
        }

        if (eligibleSkills.isEmpty()) {
            return null;
        }

        return eligibleSkills.get(random.nextInt(eligibleSkills.size()));
    }
}
