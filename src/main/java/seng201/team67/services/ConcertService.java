package seng201.team67.services;

import javafx.scene.control.Button;
import seng201.team67.GameEnviroment;
import seng201.team67.models.Concert;
import seng201.team67.models.MiniGameResult;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.Difficulty;
import seng201.team67.models.enums.TourType;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.models.questionmodels.Question;

import java.util.ArrayList;
import java.util.List;

public class ConcertService {

    //TODO: Refactor questions to be in a dict

    private Concert concert;
    private GameEnviroment gameEnviroment;
    private TourService tourService;
    private List<Question> concertQuestions;
    private int count = 0;
    private Double income = 0.0; //pay for the concert
    private boolean isEnded = false;

    public ConcertService(GameEnviroment gameEnviroment, TourService tourService)
    {
        this.gameEnviroment = gameEnviroment;
        this.tourService = tourService;
        concert = new Concert();
        concertQuestions = generateConcertQuestions();
    }

    private int generateQuestionCount()
    {
        //TODO: get rid of this
        return gameEnviroment.getConfig().concertQuestionsCount;
    }

    private List<Question> generateConcertQuestions()
    {
        //50/50 chance question is either common or directly related to the tour.
        List<Question> concertQuestions = new ArrayList<>();

        for(int i = 1; i <= generateQuestionCount(); i++)
        {
            String type = "";
            boolean result = Math.random() < gameEnviroment.getConfig().questionCommonChance;
            if(result)
            {
                type = "common";
            }
            else
            {
                type = tourService.getTourType().toString();
            }
            concertQuestions.add(gameEnviroment.getQuestion(type));
        }
        return concertQuestions;
    }

    private void endConcert()
    {

    }

    public Question getNextQuestion()
    {
        //TODO: needs work
        if(count == concertQuestions.size())
        {
            isEnded = true;
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
        if(outcome.getCreditChange() > 0)
        {
            gameEnviroment.getLabelService().giveMoney(outcome.getCreditChange());
        }
        else
        {
            gameEnviroment.getLabelService().takeMoney(outcome.getCreditChange());
        }

        income += outcome.getCreditChange();
        tourService.addCreditsEarned(income);

        //stamina change goes here

        concert.addEnergy(outcome.getCrowdEnergyChange());


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
}
