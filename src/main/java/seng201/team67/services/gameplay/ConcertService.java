package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.Concert;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.GameplayEffect;
import seng201.team67.models.enums.ItemEffects;
import seng201.team67.models.enums.Minigame;
import seng201.team67.models.minigames.MiniGameResult;
import seng201.team67.models.enums.questions.PayoutType;
import seng201.team67.models.items.ConditionalItem;
import seng201.team67.models.items.Item;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.models.questionmodels.Question;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides concert operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ConcertService {
    /** The concert. */
    private Concert concert;
    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;
    /** Service used to manage tour behaviour. */
    private TourService tourService;
    /** Collection that stores the concert questions. */
    private List<Question> concertQuestions;
    /** Numeric value for the answered question count. */
    private int answeredQuestionCount = 0;
    /** Numeric value for the income. */
    private Double income = 0.0;
    /** Numeric value for the income multiplier. */
    private double incomeMultiplier = 1.0;
    /** Numeric value for the stamina drain. */
    private double staminaDrain;
    /** Numeric value for the win streak. */
    private int winStreak = 0;
    /** Whether last event won. */
    private boolean lastEventWon = false;
    /** Whether force best outcome next event. */
    private boolean forceBestOutcomeNextEvent = false;
    /** Whether ended. */
    private boolean isEnded = false;
    /** Whether minigame check resolved. */
    private boolean minigameCheckResolved = false;
    /** Whether concert modifier triggered. */
    private boolean concertModifierTriggered = false;
    /** Collection that stores the consumed conditional effects. */
    private final Set<String> consumedConditionalEffects = new HashSet<>();
    /** Service used to manage question behaviour. */
    private final QuestionService questionService = new QuestionService();
    /** Service used to manage payout behaviour. */
    private final PayoutService payoutService = new PayoutService();
    /** Service used to manage stamina behaviour. */
    private final StaminaService staminaService = new StaminaService();

    /**
     * Creates a new concert service.
     * @param gameEnvironment the active game environment
     * @param tourService the tour service for the current run
     */
    public ConcertService(GameEnvironment gameEnvironment, TourService tourService)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
        concert = new Concert(gameEnvironment);
        concertQuestions = generateConcertQuestions();
        this.tourService.setConditionalEffectText("");
    }

    private int generateQuestionCount()
    {
        return gameEnvironment.getConfig().concertQuestionsCount;
    }

    private List<Question> generateConcertQuestions()
    {
        List<Question> concertQuestions = new ArrayList<>();

        for(int i = 1; i <= generateQuestionCount(); i++)
        {
            String type = "";
            boolean result = Math.random() < gameEnvironment.getConfig().questionCommonChance;
            if(result)
            {
                type = "common";
            }
            else
            {
                type = tourService.getTourType().toString();
            }
            concertQuestions.add(questionService.getQuestion(gameEnvironment, type));
        }
        return concertQuestions;
    }

    private void endConcert()
    {
        if (isEnded) return;
        isEnded = true;
        double ticketRevenue = calculateTicketRevenue();
        double artistPay = tourService.getTourArtistPay();

        tourService.addCreditsEarned(ticketRevenue);
        tourService.addCreditsEarned(income);
        tourService.addAccruedArtistPay(artistPay);

        gameEnvironment.getLabelService().giveMoney(ticketRevenue);

        int endConcertDrain = roundStaminaChange(calculateStaminaDrain());
        applySequentialLineupDrain(endConcertDrain);
    }

    /**
     * Returns the next question.
     * @return The resolved next question, or `null` if no value is available.
     */
    public Question getNextQuestion()
    {
        if(answeredQuestionCount == concertQuestions.size())
        {
            endConcert();
            return null;
        }

        Question ques = concertQuestions.get(answeredQuestionCount);
        answeredQuestionCount += 1;
        return ques;
    }

    /**
     * Resolves the selected answer for the current concert event.
     * It applies the chosen outcome and updates the concert state accordingly.
     * @param answer the selected answer to resolve
     * @return The resulting outcome.
     */
    public Outcome handleAnswer(Answer answer)
    {
        Artist answeringArtist = getCurrentAnsweringArtist();
        Outcome selectedOutcome = rollOutcomes(answer.getOutcomes());
        applyOutcome(selectedOutcome, answeringArtist);
        return selectedOutcome;
    }

    private Outcome rollOutcomes(List<Outcome> outcomes)
    {
        if (forceBestOutcomeNextEvent)
        {
            forceBestOutcomeNextEvent = false;
            return outcomes.stream()
                    .max(Comparator
                            .comparingInt(this::scoreOutcomeForAutoWin)
                            .thenComparingInt(Outcome::getCrowdEnergyChange)
                            .thenComparingInt(Outcome::getWeight))
                    .orElse(outcomes.getLast());
        }

        int totalWeight = outcomes.stream().mapToInt(Outcome::getWeight).sum();

        int roll = (int) (Math.random() * totalWeight);
        int sum = 0;

        for (Outcome outcome : outcomes)
        {
            sum += outcome.getWeight();
            if (roll < sum)
            {
                return outcome;
            }
        }
        return  outcomes.getLast();
    }

    private void applyOutcome(Outcome outcome, Artist answeringArtist)
    {
        if (outcome.getConcertEnds())
        {
            endConcert();
            System.out.println("Concert ended");
        }

        if(outcome.getPayoutType() != PayoutType.NONE)
        {
            double basePayout = payoutService.getPayoutAmount(gameEnvironment, outcome.getPayoutType());
            double adjustedPayout = applySkillPayoutModifiers(basePayout, outcome, answeringArtist);
            income += adjustedPayout;

            if (adjustedPayout >= 0)
            {
                gameEnvironment.getLabelService().giveMoney(adjustedPayout);
            }
            else
            {
                gameEnvironment.getLabelService().takeMoney(-adjustedPayout);
            }
        }

        int staminaChange = roundStaminaChange(resolveStaminaChange(outcome));
        if (staminaChange < 0)
        {
            applySequentialLineupDrain(-staminaChange);
        }
        else if (staminaChange > 0)
        {
            gameEnvironment.getLabelService().applyStaminaChange(staminaChange);
        }

        concert.addEnergy((int) calculateCrowdGain(outcome.getCrowdEnergyChange()));
        applyAnsweringArtistOutcomeSkills(answeringArtist, outcome);
        updateConcertWinState(outcome);
        applyItemConcertModifiers();
        applyConditionalItemEffects();
    }

    /**
     * Applies the result of a completed minigame to the concert state.
     * It updates both crowd energy and label money based on the minigame outcome.
     * @param result the result
     */
    public void applyMiniGameResult(MiniGameResult result)
    {
        concert.addEnergy(result.getCrowdMeterResult());
        income += result.getCreditResult();
        if (result.getCreditResult() >= 0)
        {
            gameEnvironment.getLabelService().giveMoney(result.getCreditResult());
        }
        else
        {
            gameEnvironment.getLabelService().takeMoney(-result.getCreditResult());
        }
    }

    /**
     * Returns the concert minigame.
     * @return The resolved concert minigame, or `null` if no value is available.
     */
    public Minigame getConcertMinigame()
    {
        if (minigameCheckResolved)
        {
            return null;
        }

        Minigame result = tourService.rollMiniGameTrigger(concert.getEnergy());

        if (result != null)
        {
            minigameCheckResolved = true;
        }

        return result;
    }

    /**
     * Returns whether ended.
     * @return True if ended, otherwise false.
     */
    public boolean isEnded()
    {
        return isEnded;
    }

    /**
     * Returns the crowd energy.
     * @return The crowd energy.
     */
    public int getCrowdEnergy()
    {
        return concert.getEnergy();
    }

    /**
     * Returns the income.
     * @return The income.
     */
    public Double getIncome()
    {
        return income;
    }

    /**
     * Returns the tour service.
     * @return The tour service.
     */
    public TourService getTourService()
    {
        return tourService;
    }

    /**
     * Returns the answered question count.
     * @return The answered question count.
     */
    public int getAnsweredQuestionCount()
    {
        return answeredQuestionCount;
    }

    /**
     * Sets the crowd energy for debug.
     * @param crowd the numeric value for the crowd
     */
    public void setCrowdEnergyForDebug(int crowd)
    {
        concert.setEnergy(crowd);
    }

    /**
     * Sets the answered question count for debug.
     * @param answeredQuestionCount the numeric value for the answered question count
     */
    public void setAnsweredQuestionCountForDebug(int answeredQuestionCount)
    {
        this.answeredQuestionCount = Math.max(0, answeredQuestionCount);
    }

    /**
     * Sets the win streak for debug.
     * @param winStreak the numeric value for the win streak
     */
    public void setWinStreakForDebug(int winStreak)
    {
        this.winStreak = Math.max(0, winStreak);
    }

    /**
     * Sets the last event won for debug.
     * @param lastEventWon whether last event won
     */
    public void setLastEventWonForDebug(boolean lastEventWon)
    {
        this.lastEventWon = lastEventWon;
    }

    /**
     * Applies the item concert modifiers for debug.
     */
    public void applyItemConcertModifiersForDebug()
    {
        applyItemConcertModifiers();
    }

    /**
     * Creates the concert results.
     * It updates related state as needed while performing the operation.
     * @return The resulting concert results.
     */
    public ConcertResults createConcertResults()
    {
        double ticketSales = calculateTicketRevenue();
        double bonusMoney = getIncome();
        double drainedStamina = totalStaminaDrain();
        int crowdHype = getCrowdEnergy();
        double artistsPay = tourService.getTourArtistPay();
        double total = ticketSales + bonusMoney - artistsPay;
        return new ConcertResults(ticketSales, bonusMoney, drainedStamina, crowdHype, artistsPay, total);
    }

    /**
     * Calculates the crowd gain.
     * @param baseGain the numeric value for the base gain
     * @return The crowd gain.
     */
    public double calculateCrowdGain(double baseGain)
    {
        double maxSp = gameEnvironment.getLabelService().getMaxSP();
        if (maxSp <= 0)
        {
            return baseGain * 0.5;
        }
        return baseGain * (0.5 + gameEnvironment.getLabelService().getAverageSP() / maxSp);
    }

    /**
     * Calculates the ticket revenue.
     * It uses the current game state and supplied context when producing the result.
     * @return The ticket revenue.
     */
    public double calculateTicketRevenue()
    {
        double maxSp = gameEnvironment.getLabelService().getMaxSP();
        double starPowerMultiplier = maxSp <= 0 ? 1 : 1 + gameEnvironment.getLabelService().getAverageSP() / maxSp;
        return (concert.getEnergy() / 100.0)
                * gameEnvironment.getConfig().ticketSalesAmount
                * tourService.getTourPayMultiplier()
                * starPowerMultiplier
                * incomeMultiplier;
    }

    /**
     * Calculates the stamina drain.
     * @return The stamina drain.
     */
    public double calculateStaminaDrain()
    {
        int baseDrain = tourService.getTourType().getBaseStaminaDrain();
        double crowdPenalty = baseDrain * Math.max(0, (50.0 -concert.getEnergy()) / 50.0);
        return baseDrain + crowdPenalty;
    }

    /**
     * Processes the total stamina drain.
     * @return The total stamina drain.
     */
    public double totalStaminaDrain()
    {
        return staminaDrain;
    }

    private int roundStaminaChange(double staminaChange)
    {
        return (int) Math.round(staminaChange);
    }

    /**
     * Resolves the stamina change.
     * @param outcome the outcome to apply
     * @return The stamina change.
     */
    public double resolveStaminaChange(Outcome outcome)
    {
        if (outcome.hasExplicitStaminaChange())
        {
            return outcome.getStaminaChange();
        }
        return staminaService.getStaminaChangeAmount(gameEnvironment, outcome.getOutcomeType());
    }

    private void applyItemConcertModifiers()
    {
        concertModifierTriggered = false;

        for (Artist artist : gameEnvironment.getLabelService().getLineup())
        {
            for (Item item : artist.getItems())
            {
                for (ItemEffects itemEffects : item.getEffects())
                {
                    if (isOneTimeConditionalEffect(itemEffects) && consumedConditionalEffects.contains(buildConsumedEffectKey(artist, item, itemEffects)))
                    {
                        continue;
                    }

                    if (itemEffects.getConcertModifier() != null)
                    {
                        itemEffects.getConcertModifier().apply(this);
                        if (consumeConcertModifierTriggered() && isOneTimeConditionalEffect(itemEffects))
                        {
                            consumedConditionalEffects.add(buildConsumedEffectKey(artist, item, itemEffects));
                        }
                    }
                }
            }
        }
    }

    private void updateConcertWinState(Outcome outcome)
    {
        boolean isWin = outcome.getPayoutType() != PayoutType.NONE
                && payoutService.getPayoutAmount(gameEnvironment, outcome.getPayoutType()) > 0;

        lastEventWon = isWin;
        winStreak = isWin ? winStreak + 1 : 0;
    }

    private void applySequentialLineupDrain(int drainAmount)
    {
        if (drainAmount <= 0)
        {
            return;
        }

        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();
        if (lineup.isEmpty())
        {
            return;
        }

        Artist targetArtist = lineup.get(Math.floorMod(tourService.getCurrentLineupStaminaIndex(), lineup.size()));
        int adjustedDrain = applySkillStaminaModifiers(targetArtist, drainAmount);

        gameEnvironment.getLabelService().applyStaminaChangeToLineupArtist(
                tourService.getCurrentLineupStaminaIndex(),
                -adjustedDrain
        );
        staminaDrain += adjustedDrain;
        tourService.addStamina(adjustedDrain);
        tourService.advanceLineupStaminaIndex();
    }

    private void applyConditionalItemEffects()
    {
        ArrayList<String> triggeredEffects = new ArrayList<>();

        for (var artist : gameEnvironment.getLabelService().getLineup())
        {
            for (Item item : artist.getItems())
            {
                if (!(item instanceof ConditionalItem))
                {
                    continue;
                }

                for (ItemEffects itemEffects : item.getEffects())
                {
                    int effectValue = artist.getEffectValue(item, itemEffects);
                    if (!artist.calculateEffect(item, itemEffects))
                    {
                        continue;
                    }

                    triggeredEffects.add(itemEffects.getName() + " applied +" + effectValue
                            + " " + formatStatName(itemEffects) + " to " + artist.getName());
                }
            }
        }

        tourService.setConditionalEffectText(String.join("\n", triggeredEffects));
    }

    private String formatStatName(ItemEffects itemEffects)
    {
        return itemEffects.getTargetStat().toString().toLowerCase().replace('_', ' ');
    }

    private double applySkillPayoutModifiers(double basePayout, Outcome outcome, Artist answeringArtist)
    {
        int modifiedPayout = roundStaminaChange(basePayout);
        int completedConcerts = tourService.getConcertResults().size();
        int totalEvents = gameEnvironment.getConfig().concertQuestionsCount;

        if (answeringArtist == null
                || !answeringArtist.hasSkill()
                || !answeringArtist.getSkill().hasPayoutModifier())
        {
            return modifiedPayout;
        }

        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();
        modifiedPayout = answeringArtist.getSkill().getPayoutModifier().apply(
                answeringArtist,
                modifiedPayout,
                outcome,
                lineup,
                concert.getEnergy(),
                completedConcerts,
                answeredQuestionCount,
                totalEvents
        );

        return modifiedPayout;
    }

    private Artist getCurrentAnsweringArtist()
    {
        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();
        if (lineup.isEmpty())
        {
            return null;
        }

        int answeringArtistIndex = Math.floorMod(tourService.getCurrentLineupStaminaIndex(), lineup.size());
        return lineup.get(answeringArtistIndex);
    }

    private void applyAnsweringArtistOutcomeSkills(Artist answeringArtist, Outcome outcome)
    {
        if (answeringArtist == null || !answeringArtist.hasSkill() || outcome == null)
        {
            return;
        }
        if (answeringArtist.getSkill().hasEffect(GameplayEffect.FLAT_STAR_POWER_BOOST)
                && outcome.getOutcomeType().name().equals("GREAT"))
        {
            int starPowerBoost = GameplayEffect.FLAT_STAR_POWER_BOOST
                    .createStatModifier(answeringArtist.getSkill().getMultiplier())
                    .apply(answeringArtist, 0);
            answeringArtist.changeStarPower(starPowerBoost);
        }
    }

    private int applySkillStaminaModifiers(Artist artist, int drainAmount)
    {
        if (!artist.hasSkill()
                || !artist.getSkill().hasStatModifier()
                || !artist.getSkill().hasEffect(GameplayEffect.STAMINA_COST_REDUCTION))
        {
            return drainAmount;
        }

        int staminaPercent = GameplayEffect.STAMINA_COST_REDUCTION
                .createStatModifier(artist.getSkill().getMultiplier())
                .apply(artist, drainAmount);
        return Math.max(0, roundStaminaChange(drainAmount * (staminaPercent / 100.0)));
    }

    private int scoreOutcomeForAutoWin(Outcome outcome)
    {
        return switch (outcome.getOutcomeType()) {
            case GREAT -> 5;
            case GOOD -> 4;
            case OK -> 3;
            case BAD -> 2;
            case TERRIBLE -> 1;
            case NONE -> 0;
        };
    }

    private boolean isOneTimeConditionalEffect(ItemEffects itemEffects)
    {
        return switch (itemEffects) {
            case AUTO_WIN_IF_CROWD_BELOW_20_AFTER_EVENT_3,
                 STAMINA_RECOVER_ALL_IF_LOWEST_BELOW_35 -> true;
            default -> false;
        };
    }

    private String buildConsumedEffectKey(Artist artist, Item item, ItemEffects itemEffects)
    {
        return artist.getName() + ":" + item.getName() + ":" + itemEffects.name();
    }

    /**
     * Returns the lineup.
     * @return The lineup.
     */
    public List<Artist> getLineup()
    {
        return gameEnvironment.getLabelService().getLineup();
    }

    /**
     * Returns the win streak.
     * @return The win streak.
     */
    public int getWinStreak()
    {
        return winStreak;
    }

    /**
     * Processes the was last event won.
     * @return True if was last event won, otherwise false.
     */
    public boolean wasLastEventWon()
    {
        return lastEventWon;
    }

    /**
     * Returns the total concert events.
     * @return The total concert events.
     */
    public int getTotalConcertEvents()
    {
        return gameEnvironment.getConfig().concertQuestionsCount;
    }

    /**
     * Returns whether final concert event.
     * @return True if final concert event, otherwise false.
     */
    public boolean isFinalConcertEvent()
    {
        return getTotalConcertEvents() > 0 && answeredQuestionCount >= getTotalConcertEvents();
    }

    /**
     * Sets the crowd energy.
     * @param crowd the numeric value for the crowd
     */
    public void setCrowdEnergy(int crowd)
    {
        concert.setEnergy(crowd);
    }

    /**
     * Adds the crowd energy.
     * @param amount the amount to apply
     */
    public void addCrowdEnergy(double amount)
    {
        concert.addEnergy((int) Math.round(amount));
    }

    /**
     * Processes the multiply income multiplier.
     * @param multiplier the multiplier used by the calculation
     */
    public void multiplyIncomeMultiplier(double multiplier)
    {
        income *= multiplier;
        incomeMultiplier *= multiplier;
    }

    /**
     * Processes the request best outcome next event.
     */
    public void requestBestOutcomeNextEvent()
    {
        forceBestOutcomeNextEvent = true;
        concertModifierTriggered = true;
    }

    /**
     * Processes the mark concert modifier triggered.
     */
    public void markConcertModifierTriggered()
    {
        concertModifierTriggered = true;
    }

    /**
     * Processes the consume concert modifier triggered.
     * @return True if consume concert modifier triggered, otherwise false.
     */
    public boolean consumeConcertModifierTriggered()
    {
        boolean currentValue = concertModifierTriggered;
        concertModifierTriggered = false;
        return currentValue;
    }

    /**
     * Returns whether force best outcome next event requested.
     * @return True if force best outcome next event requested, otherwise false.
     */
    public boolean isForceBestOutcomeNextEventRequested()
    {
        return forceBestOutcomeNextEvent;
    }

    /**
     * Returns the current income multiplier.
     * @return The current income multiplier.
     */
    public double getCurrentIncomeMultiplier()
    {
        return incomeMultiplier;
    }
}
