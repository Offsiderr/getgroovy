package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.GameConfig;
import seng201.team67.models.enums.TourType;

public class ScoreService {

    public int calculateConcertScore(GameEnvironment gameEnvironment, ConcertService concertService, ConcertResults results)
    {
        GameConfig config = gameEnvironment.getConfig();
        int questionScore = concertService.getAnsweredQuestionCount() * config.questionAnsweredScore;
        int crowdScore = (int) Math.round(results.crowdHype * config.crowdHypeScoreMultiplier);
        int earningsScore = (int) Math.round(results.total / config.netEarningsScoreDivisor);
        return config.concertCompletionScore + questionScore + crowdScore + earningsScore;
    }

    public int calculateTourScore(GameEnvironment gameEnvironment, TourService tourService)
    {
        int score = getTourCompletionBonus(gameEnvironment.getConfig(), tourService.getTourType());
        if (tourService.isEndedByExhaustion())
        {
            score -= gameEnvironment.getConfig().exhaustionScorePenalty;
        }
        return score;
    }

    public int applyConcertScore(GameEnvironment gameEnvironment, ConcertService concertService, ConcertResults results)
    {
        int score = calculateConcertScore(gameEnvironment, concertService, results);
        gameEnvironment.addGameScore(score);
        return score;
    }

    public int applyTourScore(GameEnvironment gameEnvironment, TourService tourService)
    {
        int score = calculateTourScore(gameEnvironment, tourService);
        gameEnvironment.addGameScore(score);
        return score;
    }

    private int getTourCompletionBonus(GameConfig config, TourType tourType)
    {
        return switch (tourType)
        {
            case LOCAL -> config.localTourCompletionScore;
            case COUNTRY -> config.countryTourCompletionScore;
            case WORLD -> config.worldTourCompletionScore;
        };
    }
}
