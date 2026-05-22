package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;

/**
 * Provides game status operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class GameStatusService {
    /** Balance where the player loses from debt. */
    public static final double BANKRUPTCY_LIMIT = -500.0;

    /**
     * Returns whether game lost.
     * @param gameEnvironment the active game environment
     * @return True if game lost, otherwise false.
     */
    public boolean isGameLost(GameEnvironment gameEnvironment)
    {
        if (gameEnvironment.getLabelService().getMoney() <= BANKRUPTCY_LIMIT)
        {
            return true;
        }

        boolean noArtists = gameEnvironment.getLabelService().getAllArtists().isEmpty();
        if (!noArtists)
        {
            return false;
        }

        return !canAffordArtistRecovery(gameEnvironment);
    }

    private boolean canAffordArtistRecovery(GameEnvironment gameEnvironment)
    {
        double money = gameEnvironment.getLabelService().getMoney();
        boolean hasAvailableArtist = gameEnvironment.getArtistPool().stream().anyMatch(artist -> !artist.owned);

        if (!hasAvailableArtist)
        {
            return false;
        }

        boolean canAffordHire = gameEnvironment.getArtistPool().stream()
                .filter(artist -> !artist.owned)
                .mapToDouble(Artist::getCost)
                .anyMatch(cost -> cost <= money);

        boolean canAffordGacha = money >= gameEnvironment.getConfig().gachaStandardCost
                || money >= gameEnvironment.getConfig().gachaGoldenCost
                || money >= gameEnvironment.getConfig().gachaPlatinumCost;

        return canAffordHire || canAffordGacha;
    }
}
