package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.GameConfig;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.EventType;
import seng201.team67.models.enums.RandomEvent;

import java.util.ArrayList;
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

    public Artist getRandomAffectedArtist(GameEnvironment gameEnvironment, RandomEvent randomEvent) {
        return getRandomAffectedArtist(randomEvent, gameEnvironment.getLabelService().getAllArtists());
    }

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

    public Artist getRandomAffectedArtist(List<Artist> artists) {
        if (artists == null || artists.isEmpty()) {
            return null;
        }

        return artists.get(random.nextInt(artists.size()));
    }

    public Artist getRandomAffectedArtist(RandomEvent randomEvent, List<Artist> artists) {
        List<Artist> eligibleArtists = getEligibleArtists(randomEvent, artists);
        if (eligibleArtists.isEmpty()) {
            return null;
        }

        return eligibleArtists.get(random.nextInt(eligibleArtists.size()));
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
                affectedArtist.setBaseStamina(before + randomEvent.getValue());
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

    public RandomEvent getWeightedRandomEvent(GameEnvironment gameEnvironment) {
        return getWeightedRandomEvent(gameEnvironment.getLabelService().getAllArtists());
    }

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
            case SKILL -> new ArrayList<>(artists);
        };
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
}
