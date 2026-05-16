package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.GameConfig;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.EventType;
import seng201.team67.models.enums.RandomEvent;

import java.util.List;
import java.util.Random;

public class RandomEventService {

    private final Random random;

    public RandomEventService() {
        this(new Random());
    }

    public RandomEventService(Random random) {
        this.random = random;
    }

    public boolean shouldTriggerRandomEvent(GameEnvironment gameEnvironment) {
        return shouldTriggerRandomEvent(gameEnvironment.getConfig(), Math.random());
    }

    public boolean shouldTriggerRandomEvent(GameConfig gameConfig, double roll) {
        return roll < gameConfig.randomEventTriggerChance;
    }

    public Artist getRandomAffectedArtist(GameEnvironment gameEnvironment) {
        return getRandomAffectedArtist(gameEnvironment.getLabelService().getAllArtists());
    }

    public Artist getRandomAffectedArtist(List<Artist> artists) {
        if (artists == null || artists.isEmpty()) {
            return null;
        }

        return artists.get(random.nextInt(artists.size()));
    }

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
}
