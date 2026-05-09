package seng201.team67.services;

import seng201.team67.GameEnvironment;
import seng201.team67.models.Concert;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.minigames.MiniGameResult;
import seng201.team67.models.enums.PayoutType;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.models.questionmodels.Question;

import java.util.ArrayList;
import java.util.List;

public class ConcertService {

    //TODO: Refactor questions to be in a dict

    private Concert concert;
    private GameEnvironment gameEnvironment;
    private TourService tourService;
    private List<Question> concertQuestions;
    private int count = 0;
    private Double income = 0.0; //pay for the concert
    private double staminaDrain;
    private boolean isEnded = false;

    public ConcertService(GameEnvironment gameEnvironment, TourService tourService)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
        concert = new Concert(gameEnvironment);
        concertQuestions = generateConcertQuestions();
    }

    private int generateQuestionCount()
    {
        //TODO: get rid of this
        return gameEnvironment.getConfig().concertQuestionsCount;
    }

    private List<Question> generateConcertQuestions()
    {
        //50/50 chance question is either common or directly related to the tour.
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
            concertQuestions.add(gameEnvironment.getQuestion(type));
        }
        return concertQuestions;
    }

    private void endConcert()
    {
        if (isEnded) return;
        isEnded = true;
        //add ticket revenue
        tourService.addCreditsEarned(calculateTicketRevenue());
        //take away lineup's pay
        tourService.addCreditsEarned(-gameEnvironment.getLabelService().getLineupTotalPay());
        //apply the stamina changes
        int endConcertDrain = roundStaminaChange(calculateStaminaDrain());
        applySequentialLineupDrain(endConcertDrain);
    }

    public Question getNextQuestion()
    {
        //TODO: needs work
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
        return  outcomes.getLast(); //TODO: Exception handling here
    }

    private void applyOutcome(Outcome outcome)
    {
        //I wrote this tired and sleep deprived so check later

        //Every variable in outcomes
        //    private final int weight;
        //    private final String description;
        //    private final int creditChange;
        //
        //    private final int staminaChange; //TODO: stamina needs to be implemented properly with artists.
        //    private final int crowdEnergyChange;
        //    private final boolean expeditionEnds;

        if (outcome.getConcertEnds())
        {
            endConcert();
            System.out.println("Concert ended");
            //TODO: implement concert ending
            //we don't break as the other effects still apply apart from the crowd energy change
        }

        //if statement not needed. fix later
        if(outcome.getPayoutType() != PayoutType.NONE)
        {
            gameEnvironment.getLabelService().giveMoney(gameEnvironment.getPayoutAmount(outcome.getPayoutType()));
        }

        //stamina change goes here
        int staminaChange = roundStaminaChange(outcome.getStaminaChange());
        if (staminaChange < 0)
        {
            applySequentialLineupDrain(-staminaChange);
        }
        else if (staminaChange > 0)
        {
            gameEnvironment.getLabelService().applyStaminaChange(staminaChange);
        }

        //Calculate concert crowd energy gain
        concert.addEnergy((int) calculateCrowdGain(outcome.getCrowdEnergyChange()));
    }

    public void applyMiniGameResult(MiniGameResult result)
    {
        concert.addEnergy(result.getCrowdMeterResult());
        income += result.getCreditResult();
    }

    public boolean isEnded()
    {
        return isEnded;
    }

    public int getCrowdEnergyChange()
    {
        return concert.getEnergy();
    }

    //move to class layer instead of service
    public Double getIncome()
    {
        return income;
    }

    public TourService getTourService()
    {
        return tourService;
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
        //concert ticket pay = (crowdMeter / 100) × baseTicketRevenue × tour multiplier × (1 + avgSP / maxSP)
        double maxSp = gameEnvironment.getLabelService().getMaxSP();
        double starPowerMultiplier = maxSp <= 0 ? 1 : 1 + gameEnvironment.getLabelService().getAverageSP() / maxSp;
        return (concert.getEnergy() / 100.0)
                * gameEnvironment.getConfig().ticketSalesAmount
                * tourService.getTourPayMultiplier()
                * starPowerMultiplier;
    }

    public double calculateStaminaDrain()
    {
        //base drain + crowd penalty
        //base drain
        int baseDrain = tourService.getTourType().getBaseStaminaDrain();
        //crowdPenalty = baseDrain × max(0, (50 - crowdMeter) / 50)
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

        gameEnvironment.getLabelService().applyStaminaChangeToLineupArtist(
                tourService.getCurrentLineupStaminaIndex(),
                -drainAmount
        );
        staminaDrain += drainAmount;
        tourService.addStamina(drainAmount);
        tourService.advanceLineupStaminaIndex();
    }
}
