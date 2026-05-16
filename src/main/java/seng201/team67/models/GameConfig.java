package seng201.team67.models;

import seng201.team67.models.enums.TourType;

public class GameConfig {

    // ── 1. Economy ────────────────────────────────────────────────────────────

    //Per-star-power multiplier applied to an artist's pay
    //public final double artistBasePay; removed as the Json importing means we cannot use this structure w the game config

    //Per-star-power multiplier applied to an artist's hiring cost
    //public final double artistBaseHiringCost;

    //Credits the label starts with
    public final int startingCredits;

    //Maximum number of artists the label roster can hold
    public final int artistsRosterLimit;

    //Maximum number of artists that may be in the active line-up
    public final int lineUpLimit;

    //Cost of a Standard gacha pull
    public final int gachaStandardCost;

    //Cost of a Golden gacha pull
    public final int gachaGoldenCost;

    //Cost of a Platinum gacha pull
    public final int gachaPlatinumCost;

    //Cost to reroll the studio artist pool
    public final int artistRerollCost;

    //Fraction of an item's original price returned when sold unused
    public final double itemSellbackRate;

    // ── 2. Probabilities & RNG ────────────────────────────────────────────────

    //Probability that a minigame triggers at each concert stop
    public final double miniGameTriggerChance;

    //Probability that a generated question is a generic "common" question vs tour-type
    public final double questionCommonChance;

    //Probability that a random event occurs after a tour ends
    public final double randomEventTriggerChance;

    //Maximum star power that an artist from the starting selection can be
    public final int maxSPInStartingSelection;

    // ── 4. Expedition Parameters ──────────────────────────────────────────────

    //Minimum number of tours selectable at setup
    public final int minTourCount;

    //Maximum number of tours selectable at setup
    public final int maxTourCount;

    //Default spinner value for tour count at setup
    public final int defaultTourCount;

    //Number of artist cards shown during the starting selection
    public final int startingArtistPoolSize;

    //Number of artists the player must pick at game start
    public final int maxStartingArtists;

    //Number of artist slots in the active concert lineup
    public final int requiredLineupSize;

    //Number of artists shown per gacha pull
    public final int gachaPoolSize;

    //Maximum artists the player may select from a single gacha pull
    public final int maxGachaPicks;

    //Number of questions generated per concert
    public final int concertQuestionsCount;

    //Minimum clicks required to open a gacha record
    public final int gachaMinClicks;

    //Maximum clicks required to open a gacha record
    public final int gachaMaxClicks;

    //Number of visual stages the gacha record animates through
    public final int gachaStageCount;

    //Lower bound of the randomly generated target slider values
    public final double soundEngineerTargetMin;

    //Width of the random range above soundEngineerTargetMin
    public final double soundEngineerTargetRange;

    //Seconds the target slider values are displayed before fading
    public final double soundEngineerFlashDurationSeconds;

    //Maximum distance (±units) a slider may be from target and still count as a match
    public final double soundEngineerMatchTolerance;

    //Value all sliders reset to after the flash phase
    public final double soundEngineerSliderDefault;


    // ── 5. UI / Validation Limits ─────────────────────────────────────────────

    //Minimum character count for the label name
    public final int labelNameMinLength;

    //Maximum character count for the label name
    public final int labelNameMaxLength;

    public final int cancelTourPenalty;

    public final double ticketSalesAmount;
    public final double localTourArtistPayMultiplier;
    public final double countryTourArtistPayMultiplier;
    public final double worldTourArtistPayMultiplier;
    public final int retirementChanceIncreasePerThreeToursWithoutBreak;

    //Score tuning
    public final int concertCompletionScore;
    public final int questionAnsweredScore;
    public final double crowdHypeScoreMultiplier;
    public final double netEarningsScoreDivisor;
    public final int localTourCompletionScore;
    public final int countryTourCompletionScore;
    public final int worldTourCompletionScore;
    public final int exhaustionScorePenalty;

    // - 6. Game Config (sound etc) ─────────────────────────────────────────────

    public double mainVolume;
    public double musicVolume;
    public double soundEffectsVolume;
    public boolean movingBackgroundEnabled;


    public GameConfig(
            // Economy
            //double artistBasePay,
            //double artistBaseHiringCost,
            int    startingCredits,
            int    artistsRosterLimit,
            int    lineUpLimit,
            int    gachaStandardCost,
            int    gachaGoldenCost,
            int    gachaPlatinumCost,
            int    artistRerollCost,
            double itemSellbackRate,
            double miniGameTriggerChance,
            double questionCommonChance,
            double randomEventTriggerChance,
            int maxSPInStartingSelection,
            // Expedition Parameters
            int    minTourCount,
            int    maxTourCount,
            int    defaultTourCount,
            int    startingArtistPoolSize,
            int    maxStartingArtists,
            int    requiredLineupSize,
            int    gachaPoolSize,
            int    maxGachaPicks,
            int    concertQuestionsCount,
            int    gachaMinClicks,
            int    gachaMaxClicks,
            int    gachaStageCount,
            double soundEngineerTargetMin,
            double soundEngineerTargetRange,
            double soundEngineerFlashDurationSeconds,
            double soundEngineerMatchTolerance,
            double soundEngineerSliderDefault,
            // UI / Validation
            int labelNameMinLength,
            int labelNameMaxLength,
            int cancelTourPenalty,
            double ticketSalesAmount,
            double localTourArtistPayMultiplier,
            double countryTourArtistPayMultiplier,
            double worldTourArtistPayMultiplier,
            int retirementChanceIncreasePerThreeToursWithoutBreak,
            int concertCompletionScore,
            int questionAnsweredScore,
            double crowdHypeScoreMultiplier,
            double netEarningsScoreDivisor,
            int localTourCompletionScore,
            int countryTourCompletionScore,
            int worldTourCompletionScore,
            int exhaustionScorePenalty,
            //Game settings
            double mainVolume,
            double musicVolume,
            double soundEffectsVolume,
            boolean movingBackgroundEnabled
    ) {
        //this.artistBasePay                    = artistBasePay;
        //this.artistBaseHiringCost             = artistBaseHiringCost;
        this.startingCredits                  = startingCredits;
        this.artistsRosterLimit               = artistsRosterLimit;
        this.lineUpLimit                      = lineUpLimit;
        this.gachaStandardCost                = gachaStandardCost;
        this.gachaGoldenCost                  = gachaGoldenCost;
        this.gachaPlatinumCost                = gachaPlatinumCost;
        this.artistRerollCost                 = artistRerollCost;
        this.itemSellbackRate                 = itemSellbackRate;
        this.miniGameTriggerChance            = miniGameTriggerChance;
        this.questionCommonChance             = questionCommonChance;
        this.randomEventTriggerChance        = randomEventTriggerChance;
        this.maxSPInStartingSelection         = maxSPInStartingSelection;
        this.minTourCount                     = minTourCount;
        this.maxTourCount                     = maxTourCount;
        this.defaultTourCount                 = defaultTourCount;
        this.startingArtistPoolSize           = startingArtistPoolSize;
        this.maxStartingArtists               = maxStartingArtists;
        this.requiredLineupSize               = requiredLineupSize;
        this.gachaPoolSize                    = gachaPoolSize;
        this.maxGachaPicks                    = maxGachaPicks;
        this.concertQuestionsCount            = concertQuestionsCount;
        this.gachaMinClicks                   = gachaMinClicks;
        this.gachaMaxClicks                   = gachaMaxClicks;
        this.gachaStageCount                  = gachaStageCount;
        this.soundEngineerTargetMin           = soundEngineerTargetMin;
        this.soundEngineerTargetRange         = soundEngineerTargetRange;
        this.soundEngineerFlashDurationSeconds = soundEngineerFlashDurationSeconds;
        this.soundEngineerMatchTolerance      = soundEngineerMatchTolerance;
        this.soundEngineerSliderDefault       = soundEngineerSliderDefault;
        this.labelNameMinLength               = labelNameMinLength;
        this.labelNameMaxLength               = labelNameMaxLength;
        this.cancelTourPenalty                = cancelTourPenalty;
        this.ticketSalesAmount                = ticketSalesAmount;
        this.localTourArtistPayMultiplier     = localTourArtistPayMultiplier;
        this.countryTourArtistPayMultiplier   = countryTourArtistPayMultiplier;
        this.worldTourArtistPayMultiplier     = worldTourArtistPayMultiplier;
        this.retirementChanceIncreasePerThreeToursWithoutBreak = retirementChanceIncreasePerThreeToursWithoutBreak;
        this.concertCompletionScore           = concertCompletionScore;
        this.questionAnsweredScore            = questionAnsweredScore;
        this.crowdHypeScoreMultiplier         = crowdHypeScoreMultiplier;
        this.netEarningsScoreDivisor          = netEarningsScoreDivisor;
        this.localTourCompletionScore         = localTourCompletionScore;
        this.countryTourCompletionScore       = countryTourCompletionScore;
        this.worldTourCompletionScore         = worldTourCompletionScore;
        this.exhaustionScorePenalty           = exhaustionScorePenalty;
        this.mainVolume                       = mainVolume;
        this.musicVolume                      = musicVolume;
        this.soundEffectsVolume               = soundEffectsVolume;
        this.movingBackgroundEnabled          = movingBackgroundEnabled;
    }

    public double getArtistPayMultiplier(TourType tourType)
    {
        return switch (tourType)
        {
            case LOCAL -> localTourArtistPayMultiplier;
            case COUNTRY -> countryTourArtistPayMultiplier;
            case WORLD -> worldTourArtistPayMultiplier;
        };
    }

    // ── Difficulty presets ────────────────────────────────────────────────────

    //Easy Values
    public static GameConfig easy() {
        return new GameConfig(
                // Economy
                500,  // startingCredits
                12, 3,
                100, 500, 1000, 100,
                0.7,
                // Probabilities
                0.15, 0.5, 0.25, 3,
                // Expedition
                5, 15, 5,
                5, 3, 1, 3, 1,
                5,          // concertQuestionsCount (EASY)
                7, 10, 4,
                20.0, 60.0, 1.0, 15.0, 50.0,
                // UI
                3, 15, 100, 250,
                1.0, 1.5, 2.0, 5,
                // Score tuning
                25, 5, 0.5, 20.0, 40, 90, 150, 50,
                //Game config
                100.0, 100.0, 100.0, true
        );
    }

    //Medium values ('A challenge')
    public static GameConfig aChallenge() {
        return new GameConfig(
                // Economy
                300,  // startingCredits
                12, 3,
                100, 500, 1000, 100,
                0.7,
                // Probabilities
                0.15, 0.5, 0.30, 2,
                // Expedition
                5, 15, 5,
                5, 3, 1, 3, 1,
                7,          // concertQuestionsCount (A_CHALLENGE)
                7, 10, 4,
                20.0, 60.0, 1.0, 10.0, 50.0,
                // UI
                3, 15, 300, 250,
                1.0, 1.5, 2.0, 5,
                // Score tuning
                30, 5, 0.6, 18.0, 50, 110, 180, 75,
                //Game config
                100.0, 100.0, 100.0, true
        );
    }

    //Hard values
    public static GameConfig hard() {
        return new GameConfig(
                // Economy
                150,  // startingCredits
                12, 3,
                100, 500, 1000, 100,
                0.7,
                // Probabilities
                0.15, 0.5, 0.40, 1,
                // Expedition
                5, 15, 5,
                5, 3, 1, 3, 1,
                9,          // concertQuestionsCount (HEARTLESS)
                7, 10, 4,
                20.0, 60.0, 1.0, 7.0, 50.0,
                // UI
                3, 15, 500, 250,
                1.0, 1.5, 2.0, 5,
                // Score tuning
                35, 6, 0.75, 15.0, 60, 130, 220, 100,
                //Game config
                100.0, 100.0, 100.0, true
        );
    }
}
