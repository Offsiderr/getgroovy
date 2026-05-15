package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.Concert;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.SkillEffects;
import seng201.team67.models.enums.Minigame;
import seng201.team67.models.minigames.MiniGameResult;
import seng201.team67.models.enums.PayoutType;
import seng201.team67.models.enums.items.Effect;
import seng201.team67.models.items.ConditionalItem;
import seng201.team67.models.items.Item;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.models.questionmodels.Question;
import java.util.ArrayList;
import java.util.List;

public class ConcertService {

    private Concert concert;
    private GameEnvironment gameEnvironment;
    private TourService tourService;
    private List<Question> concertQuestions;
    private int count = 0;
    private Double income = 0.0;
    private double staminaDrain;
    private boolean isEnded = false;
    private boolean minigameCheckResolved = false;
    private final QuestionService questionService = new QuestionService();
    private final PayoutService payoutService = new PayoutService();

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
        tourService.addCreditsEarned(calculateTicketRevenue());
        tourService.addCreditsEarned(income);
        tourService.addCreditsEarned(-gameEnvironment.getLabelService().getLineupTotalPay());
        int endConcertDrain = roundStaminaChange(calculateStaminaDrain());
        applySequentialLineupDrain(endConcertDrain);
    }

    public Question getNextQuestion()
    {
        if(count == concertQuestions.size())
        {
            endConcert();
            return null;
        }

        Question ques = concertQuestions.get(count);
        count += 1;
        return ques;
    }

    public Outcome handleAnswer(Answer answer)
    {
        Outcome selectedOutcome = rollOutcomes(answer.getOutcomes());
        applyOutcome(selectedOutcome);
        return selectedOutcome;
    }

    private Outcome rollOutcomes(List<Outcome> outcomes)
    {
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

    private void applyOutcome(Outcome outcome)
    {
        if (outcome.getConcertEnds())
        {
            endConcert();
            System.out.println("Concert ended");
        }

        if(outcome.getPayoutType() != PayoutType.NONE)
        {
            double basePayout = payoutService.getPayoutAmount(gameEnvironment, outcome.getPayoutType());
            income += applySkillPayoutModifiers(basePayout);
        }

        int staminaChange = roundStaminaChange(outcome.getStaminaChange());
        if (staminaChange < 0)
        {
            applySequentialLineupDrain(-staminaChange);
        }
        else if (staminaChange > 0)
        {
            gameEnvironment.getLabelService().applyStaminaChange(staminaChange);
        }

        concert.addEnergy((int) calculateCrowdGain(outcome.getCrowdEnergyChange()));
        applyConditionalItemEffects();
    }

    public void applyMiniGameResult(MiniGameResult result)
    {
        concert.addEnergy(result.getCrowdMeterResult());
        income += result.getCreditResult();
    }

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

    public boolean isEnded()
    {
        return isEnded;
    }

    public int getCrowdEnergyChange()
    {
        return concert.getEnergy();
    }

    public Double getIncome()
    {
        return income;
    }

    public TourService getTourService()
    {
        return tourService;
    }

    public int getAnsweredQuestionCount()
    {
        return count;
    }

    public ConcertResults createConcertResults()
    {
        double ticketSales = calculateTicketRevenue();
        double bonusMoney = getIncome();
        double drainedStamina = totalStaminaDrain();
        int crowdHype = getCrowdEnergyChange();
        double artistsPay = gameEnvironment.getLabelService().getLineupTotalPay();
        double total = ticketSales + bonusMoney - artistsPay;
        return new ConcertResults(ticketSales, bonusMoney, drainedStamina, crowdHype, artistsPay, total);
    }

    public double calculateCrowdGain(double baseGain)
    {
        double maxSp = gameEnvironment.getLabelService().getMaxSP();
        if (maxSp <= 0)
        {
            return baseGain * 0.5;
        }
        return baseGain * (0.5 + gameEnvironment.getLabelService().getAverageSP() / maxSp);
    }

    public double calculateTicketRevenue()
    {
        double maxSp = gameEnvironment.getLabelService().getMaxSP();
        double starPowerMultiplier = maxSp <= 0 ? 1 : 1 + gameEnvironment.getLabelService().getAverageSP() / maxSp;
        return (concert.getEnergy() / 100.0)
                * gameEnvironment.getConfig().ticketSalesAmount
                * tourService.getTourPayMultiplier()
                * starPowerMultiplier;
    }

    public double calculateStaminaDrain()
    {
        int baseDrain = tourService.getTourType().getBaseStaminaDrain();
        double crowdPenalty = baseDrain * Math.max(0, (50.0 -concert.getEnergy()) / 50.0);
        return baseDrain + crowdPenalty;
    }

    public double totalStaminaDrain()
    {
        return staminaDrain;
    }

    private int roundStaminaChange(double staminaChange)
    {
        return (int) Math.round(staminaChange);
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

                for (Effect effect : item.getEffects())
                {
                    int effectValue = artist.getEffectValue(item, effect);
                    if (!artist.calculateEffect(item, effect))
                    {
                        continue;
                    }

                    triggeredEffects.add(effect.getName() + " applied +" + effectValue
                            + " " + formatStatName(effect) + " to " + artist.getName());
                }
            }
        }

        tourService.setConditionalEffectText(String.join("\n", triggeredEffects));
    }

    private String formatStatName(Effect effect)
    {
        return effect.getTargetStat().toString().toLowerCase().replace('_', ' ');
    }

    private double applySkillPayoutModifiers(double basePayout)
    {
        int modifiedPayout = roundStaminaChange(basePayout);

        for (Artist artist : gameEnvironment.getLabelService().getLineup())
        {
            if (!artist.hasSkill() || !artist.getSkill().hasPayoutModifier())
            {
                continue;
            }

            modifiedPayout = artist.getSkill().getPayoutModifier().apply(artist, modifiedPayout);
        }

        return modifiedPayout;
    }

    private int applySkillStaminaModifiers(Artist artist, int drainAmount)
    {
        if (!artist.hasSkill()
                || !artist.getSkill().hasStatModifier()
                || !artist.getSkill().hasEffect(SkillEffects.STAMINA_COST_REDUCTION))
        {
            return drainAmount;
        }

        int staminaPercent = artist.getSkill().getStatModifier().apply(artist, drainAmount);
        return Math.max(0, roundStaminaChange(drainAmount * (staminaPercent / 100.0)));
    }
}