package seng201.team67.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.enums.TourType;

/**
 * Stores configuration values that control game setup and balance.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class GameConfig {

    /** Numeric value for the starting credits. */
    public final int startingCredits;

    /** Numeric value for the artists roster limit. */
    public final int artistsRosterLimit;

    /** Numeric value for the line up limit. */
    public final int lineUpLimit;

    /** Numeric value for the gacha standard cost. */
    public final int gachaStandardCost;

    /** Numeric value for the gacha golden cost. */
    public final int gachaGoldenCost;

    /** Numeric value for the gacha platinum cost. */
    public final int gachaPlatinumCost;

    /** Numeric value for the artist reroll cost. */
    public final int artistRerollCost;

    /** Numeric value for the item sellback rate. */
    public final double itemSellbackRate;

    /** Numeric value for the mini game trigger chance. */
    public final double miniGameTriggerChance;

    /** Numeric value for the question common chance. */
    public final double questionCommonChance;

    /** Numeric value for the random event trigger chance. */
    public final double randomEventTriggerChance;

    /** Numeric value for the max sp in starting selection. */
    public final int maxSPInStartingSelection;

    /** Numeric value for the min tour count. */
    public final int minTourCount;

    /** Numeric value for the max tour count. */
    public final int maxTourCount;

    /** Numeric value for the default tour count. */
    public final int defaultTourCount;

    /** Numeric value for the starting artist pool size. */
    public final int startingArtistPoolSize;

    /** Numeric value for the max starting artists. */
    public final int maxStartingArtists;

    /** Numeric value for the required lineup size. */
    public final int requiredLineupSize;

    /** Numeric value for the gacha pool size. */
    public final int gachaPoolSize;

    /** Numeric value for the max gacha picks. */
    public final int maxGachaPicks;

    /** Numeric value for the concert questions count. */
    public final int concertQuestionsCount;

    /** Numeric value for the gacha min clicks. */
    public final int gachaMinClicks;

    /** Numeric value for the gacha max clicks. */
    public final int gachaMaxClicks;

    /** Numeric value for the gacha stage count. */
    public final int gachaStageCount;

    /** Numeric value for the sound engineer target min. */
    public final double soundEngineerTargetMin;

    /** Numeric value for the sound engineer target range. */
    public final double soundEngineerTargetRange;

    /** Numeric value for the sound engineer flash duration seconds. */
    public final double soundEngineerFlashDurationSeconds;

    /** Numeric value for the sound engineer match tolerance. */
    public final double soundEngineerMatchTolerance;

    /** Numeric value for the sound engineer slider default. */
    public final double soundEngineerSliderDefault;

    /** Numeric value for the label name min length. */
    public final int labelNameMinLength;

    /** Numeric value for the label name max length. */
    public final int labelNameMaxLength;

    /** Numeric value for the local tour cancel penalty. */
    public final int localTourCancelPenalty;

    /** Numeric value for the country tour cancel penalty. */
    public final int countryTourCancelPenalty;

    /** Numeric value for the world tour cancel penalty. */
    public final int worldTourCancelPenalty;

    /** Numeric value for the ticket sales amount. */
    public final double ticketSalesAmount;

    /** Numeric value for the local tour artist pay multiplier. */
    public final double localTourArtistPayMultiplier;

    /** Numeric value for the country tour artist pay multiplier. */
    public final double countryTourArtistPayMultiplier;

    /** Numeric value for the world tour artist pay multiplier. */
    public final double worldTourArtistPayMultiplier;

    /** Numeric value for the retirement chance increase per three tours without break. */
    public final int retirementChanceIncreasePerThreeToursWithoutBreak;

    //Score tuning
    /** Numeric value for the concert completion score. */
    public final int concertCompletionScore;

    /** Numeric value for the question answered score. */
    public final int questionAnsweredScore;

    /** Numeric value for the crowd hype score multiplier. */
    public final double crowdHypeScoreMultiplier;

    /** Numeric value for the net earnings score divisor. */
    public final double netEarningsScoreDivisor;

    /** Numeric value for the local tour completion score. */
    public final int localTourCompletionScore;

    /** Numeric value for the country tour completion score. */
    public final int countryTourCompletionScore;

    /** Numeric value for the world tour completion score. */
    public final int worldTourCompletionScore;

    /** Numeric value for the exhaustion score penalty. */
    public final int exhaustionScorePenalty;

    /** Numeric value for the main volume. */
    public double mainVolume;

    /** Numeric value for the music volume. */
    public double musicVolume;

    /** Numeric value for the sound effects volume. */
    public double soundEffectsVolume;

    /** Whether moving background enabled. */
    public boolean movingBackgroundEnabled;


    /**
     * Creates a new game config.
     * It initializes the state needed for the surrounding game flow.
     * @param startingCredits the numeric value for the starting credits
     * @param artistsRosterLimit the numeric value for the artists roster limit
     * @param lineUpLimit the numeric value for the line up limit
     * @param gachaStandardCost the numeric value for the gacha standard cost
     * @param gachaGoldenCost the numeric value for the gacha golden cost
     * @param gachaPlatinumCost the numeric value for the gacha platinum cost
     * @param artistRerollCost the numeric value for the artist reroll cost
     * @param itemSellbackRate the numeric value for the item sellback rate
     * @param miniGameTriggerChance the numeric value for the mini game trigger chance
     * @param questionCommonChance the numeric value for the question common chance
     * @param randomEventTriggerChance the numeric value for the random event trigger chance
     * @param maxSPInStartingSelection the numeric value for the max sp in starting selection
     * @param minTourCount the numeric value for the min tour count
     * @param maxTourCount the numeric value for the max tour count
     * @param defaultTourCount the numeric value for the default tour count
     * @param startingArtistPoolSize the numeric value for the starting artist pool size
     * @param maxStartingArtists the numeric value for the max starting artists
     * @param requiredLineupSize the numeric value for the required lineup size
     * @param gachaPoolSize the numeric value for the gacha pool size
     * @param maxGachaPicks the numeric value for the max gacha picks
     * @param concertQuestionsCount the numeric value for the concert questions count
     * @param gachaMinClicks the numeric value for the gacha min clicks
     * @param gachaMaxClicks the numeric value for the gacha max clicks
     * @param gachaStageCount the numeric value for the gacha stage count
     * @param soundEngineerTargetMin the numeric value for the sound engineer target min
     * @param soundEngineerTargetRange the numeric value for the sound engineer target range
     * @param soundEngineerFlashDurationSeconds the numeric value for the sound engineer flash duration seconds
     * @param soundEngineerMatchTolerance the numeric value for the sound engineer match tolerance
     * @param soundEngineerSliderDefault the numeric value for the sound engineer slider default
     * @param labelNameMinLength the numeric value for the label name min length
     * @param labelNameMaxLength the numeric value for the label name max length
     * @param localTourCancelPenalty the numeric value for the local tour cancel penalty
     * @param countryTourCancelPenalty the numeric value for the country tour cancel penalty
     * @param worldTourCancelPenalty the numeric value for the world tour cancel penalty
     * @param ticketSalesAmount the numeric value for the ticket sales amount
     * @param localTourArtistPayMultiplier the numeric value for the local tour artist pay multiplier
     * @param countryTourArtistPayMultiplier the numeric value for the country tour artist pay multiplier
     * @param worldTourArtistPayMultiplier the numeric value for the world tour artist pay multiplier
     * @param retirementChanceIncreasePerThreeToursWithoutBreak the numeric value for the retirement chance increase per three tours without break
     * @param concertCompletionScore the numeric value for the concert completion score
     * @param questionAnsweredScore the numeric value for the question answered score
     * @param crowdHypeScoreMultiplier the numeric value for the crowd hype score multiplier
     * @param netEarningsScoreDivisor the numeric value for the net earnings score divisor
     * @param localTourCompletionScore the numeric value for the local tour completion score
     * @param countryTourCompletionScore the numeric value for the country tour completion score
     * @param worldTourCompletionScore the numeric value for the world tour completion score
     * @param exhaustionScorePenalty the numeric value for the exhaustion score penalty
     * @param mainVolume the numeric value for the main volume
     * @param musicVolume the numeric value for the music volume
     * @param soundEffectsVolume the numeric value for the sound effects volume
     * @param movingBackgroundEnabled whether moving background enabled
     */
    @JsonCreator
    public GameConfig(
            // Economy
            //double artistBasePay,
            //double artistBaseHiringCost,
            @JsonProperty("startingCredits") int    startingCredits,
            @JsonProperty("artistsRosterLimit") int    artistsRosterLimit,
            @JsonProperty("lineUpLimit") int    lineUpLimit,
            @JsonProperty("gachaStandardCost") int    gachaStandardCost,
            @JsonProperty("gachaGoldenCost") int    gachaGoldenCost,
            @JsonProperty("gachaPlatinumCost") int    gachaPlatinumCost,
            @JsonProperty("artistRerollCost") int    artistRerollCost,
            @JsonProperty("itemSellbackRate") double itemSellbackRate,
            @JsonProperty("miniGameTriggerChance") double miniGameTriggerChance,
            @JsonProperty("questionCommonChance") double questionCommonChance,
            @JsonProperty("randomEventTriggerChance") double randomEventTriggerChance,
            @JsonProperty("maxSPInStartingSelection") int maxSPInStartingSelection,
            // Expedition Parameters
            @JsonProperty("minTourCount") int    minTourCount,
            @JsonProperty("maxTourCount") int    maxTourCount,
            @JsonProperty("defaultTourCount") int    defaultTourCount,
            @JsonProperty("startingArtistPoolSize") int    startingArtistPoolSize,
            @JsonProperty("maxStartingArtists") int    maxStartingArtists,
            @JsonProperty("requiredLineupSize") int    requiredLineupSize,
            @JsonProperty("gachaPoolSize") int    gachaPoolSize,
            @JsonProperty("maxGachaPicks") int    maxGachaPicks,
            @JsonProperty("concertQuestionsCount") int    concertQuestionsCount,
            @JsonProperty("gachaMinClicks") int    gachaMinClicks,
            @JsonProperty("gachaMaxClicks") int    gachaMaxClicks,
            @JsonProperty("gachaStageCount") int    gachaStageCount,
            @JsonProperty("soundEngineerTargetMin") double soundEngineerTargetMin,
            @JsonProperty("soundEngineerTargetRange") double soundEngineerTargetRange,
            @JsonProperty("soundEngineerFlashDurationSeconds") double soundEngineerFlashDurationSeconds,
            @JsonProperty("soundEngineerMatchTolerance") double soundEngineerMatchTolerance,
            @JsonProperty("soundEngineerSliderDefault") double soundEngineerSliderDefault,
            // UI / Validation
            @JsonProperty("labelNameMinLength") int labelNameMinLength,
            @JsonProperty("labelNameMaxLength") int labelNameMaxLength,
            @JsonProperty("localTourCancelPenalty") Integer localTourCancelPenalty,
            @JsonProperty("countryTourCancelPenalty") Integer countryTourCancelPenalty,
            @JsonProperty("worldTourCancelPenalty") Integer worldTourCancelPenalty,
            @JsonProperty("cancelTourPenalty") Integer cancelTourPenalty,
            @JsonProperty("ticketSalesAmount") double ticketSalesAmount,
            @JsonProperty("localTourArtistPayMultiplier") double localTourArtistPayMultiplier,
            @JsonProperty("countryTourArtistPayMultiplier") double countryTourArtistPayMultiplier,
            @JsonProperty("worldTourArtistPayMultiplier") double worldTourArtistPayMultiplier,
            @JsonProperty("retirementChanceIncreasePerThreeToursWithoutBreak") int retirementChanceIncreasePerThreeToursWithoutBreak,
            @JsonProperty("concertCompletionScore") int concertCompletionScore,
            @JsonProperty("questionAnsweredScore") int questionAnsweredScore,
            @JsonProperty("crowdHypeScoreMultiplier") double crowdHypeScoreMultiplier,
            @JsonProperty("netEarningsScoreDivisor") double netEarningsScoreDivisor,
            @JsonProperty("localTourCompletionScore") int localTourCompletionScore,
            @JsonProperty("countryTourCompletionScore") int countryTourCompletionScore,
            @JsonProperty("worldTourCompletionScore") int worldTourCompletionScore,
            @JsonProperty("exhaustionScorePenalty") int exhaustionScorePenalty,
            //Game settings
            @JsonProperty("mainVolume") double mainVolume,
            @JsonProperty("musicVolume") double musicVolume,
            @JsonProperty("soundEffectsVolume") double soundEffectsVolume,
            @JsonProperty("movingBackgroundEnabled") boolean movingBackgroundEnabled
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
        int fallbackCancelPenalty = cancelTourPenalty == null ? 0 : cancelTourPenalty;
        this.localTourCancelPenalty           = localTourCancelPenalty == null ? fallbackCancelPenalty : localTourCancelPenalty;
        this.countryTourCancelPenalty         = countryTourCancelPenalty == null ? fallbackCancelPenalty : countryTourCancelPenalty;
        this.worldTourCancelPenalty           = worldTourCancelPenalty == null ? fallbackCancelPenalty : worldTourCancelPenalty;
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

    /**
     * Returns the artist pay multiplier.
     * @param tourType the tour type
     * @return The artist pay multiplier.
     */
    public double getArtistPayMultiplier(TourType tourType)
    {
        return switch (tourType)
        {
            case LOCAL -> localTourArtistPayMultiplier;
            case COUNTRY -> countryTourArtistPayMultiplier;
            case WORLD -> worldTourArtistPayMultiplier;
        };
    }

    /**
     * Returns the cancellation refund amount for the given tour type.
     * @param tourType the tour type
     * @return The cancellation refund amount.
     */
    public int getCancelTourPenalty(TourType tourType)
    {
        return switch (tourType)
        {
            case LOCAL -> localTourCancelPenalty;
            case COUNTRY -> countryTourCancelPenalty;
            case WORLD -> worldTourCancelPenalty;
        };
    }

    /**
     * Stores the easy difficulty.
     * It updates related state as needed while performing the operation.
     * @return The game config.
     */
    public static GameConfig easy() {
        return new GameConfig(
                // Economy
                500,  // startingCredits
                12, 3,
                150, 1500, 3000, 100,
                0.7,
                // Probabilities
                0.15, 0.5, 0.25, 3,
                // Expedition
                5, 15, 5,
                5, 3, 1, 3, 1,
                5,
                7, 10, 4,
                20.0, 60.0, 1.0, 15.0, 50.0,
                // UI
                3, 15, 100, 200, 300, 300, 250,
                1.0, 1.5, 2.0, 5,
                // Score tuning
                25, 5, 0.5, 20.0, 40, 90, 150, 50,
                //Game config
                30.0, 30.0, 30.0, true
        );
    }

    /**
     * Stores the A challenge difficulty.
     * It updates related state as needed while performing the operation.
     * @return The game config.
     */
    public static GameConfig aChallenge() {
        return new GameConfig(
                // Economy
                300,  // startingCredits
                12, 3,
                150, 1500, 3000, 120,
                0.7,
                // Probabilities
                0.15, 0.5, 0.30, 2,
                // Expedition
                5, 15, 5,
                5, 3, 1, 3, 1,
                7,
                7, 10, 4,
                20.0, 60.0, 1.0, 10.0, 50.0,
                // UI
                3, 15, 300, 450, 600, 300, 250,
                1.0, 1.5, 2.0, 5,
                // Score tuning
                30, 5, 0.6, 18.0, 50, 110, 180, 75,
                //Game config
                30.0, 30.0, 30.0, true
        );
    }

    /**
     * Stores the hard difficulty.
     * It updates related state as needed while performing the operation.
     * @return The game config.
     */
    public static GameConfig hard() {
        return new GameConfig(
                // Economy
                150,  // startingCredits
                12, 3,
                150, 1500, 3000, 150,
                0.7,
                // Probabilities
                0.15, 0.5, 0.40, 1,
                // Expedition
                5, 15, 5,
                5, 3, 1, 3, 1,
                9,
                7, 10, 4,
                20.0, 60.0, 1.0, 7.0, 50.0,
                // UI
                3, 15, 500, 700, 900, 300, 250,
                1.0, 1.5, 2.0, 5,
                // Score tuning
                35, 6, 0.75, 15.0, 60, 130, 220, 100,
                //Game config
                30.0, 30.0, 30.0, true
        );
    }
}
