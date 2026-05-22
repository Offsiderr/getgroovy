package seng201.team67.services.management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.enums.ItemEffects;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;
import seng201.team67.services.setup.DifficultyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LabelServiceTest {

    private GameEnvironment gameEnvironment;
    private LabelService labelService;
    private Artist leadArtist;
    private Artist secondArtist;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        leadArtist = new Popstar("Lead Pop", 1, "Pop");
        secondArtist = new Rapper("Lead Rap", 2, "Rap");
        gameEnvironment.setLabel(new Label("Test Label", List.of(leadArtist, secondArtist), gameEnvironment));
        labelService = new LabelService(gameEnvironment);
    }

    @Test
    @DisplayName("Buying an item spends credits and places the purchased item in inventory")
    void buyItemSpendsCreditsAndAddsItemToInventory() {
        Item item = item("Stage Mic", 120);
        double startingCredits = labelService.getMoney();

        boolean bought = labelService.buyItem(item);

        assertTrue(bought);
        assertTrue(item.getOwned());
        assertTrue(labelService.getAllItems().contains(item));
        assertEquals(startingCredits - item.getCost(), labelService.getMoney(), 0.0001);
        assertEquals(item.getCost(), gameEnvironment.getTotalMoneySpent(), 0.0001);
    }

    @Test
    @DisplayName("Selling an inventory item refunds credits and removes ownership")
    void sellItemRefundsCreditsAndRemovesInventoryItem() {
        Item item = item("Stage Mic", 100);
        labelService.buyItem(item, 0);
        double startingCredits = labelService.getMoney();

        boolean sold = labelService.sellItem(item);

        assertTrue(sold);
        assertFalse(item.getOwned());
        assertFalse(labelService.getAllItems().contains(item));
        assertEquals(startingCredits + 70, labelService.getMoney(), 0.0001);
        assertEquals(70, gameEnvironment.getTotalMoneyEarnt(), 0.0001);
    }

    @Test
    @DisplayName("Hiring an artist spends their hiring cost and adds them to the roster")
    void hireArtistSpendsHiringCostAndAddsArtist() {
        Artist artist = new Popstar("New Hire", 2, "Pop");
        double startingCredits = labelService.getMoney();

        boolean hired = labelService.hireArtist(artist);

        assertTrue(hired);
        assertTrue(artist.owned);
        assertTrue(labelService.getAllArtists().contains(artist));
        assertEquals(startingCredits - artist.getCost(), labelService.getMoney(), 0.0001);
    }

    @Test
    @DisplayName("Hiring fails cleanly when the label cannot afford the artist")
    void hireArtistReturnsFalseWhenCreditsAreInsufficient() {
        Artist artist = new Popstar("Too Expensive", 5, "Pop");
        labelService.takeMoney(labelService.getMoney());

        boolean hired = labelService.hireArtist(artist);

        assertFalse(hired);
        assertFalse(artist.owned);
        assertFalse(labelService.getAllArtists().contains(artist));
    }

    @Test
    @DisplayName("Retiring artists respects roster safety and removes retired artists from lineup")
    void retireArtistRemovesArtistButNotLastRosterMember() {
        assertTrue(labelService.retireArtist(leadArtist));
        assertFalse(labelService.getAllArtists().contains(leadArtist));
        assertFalse(labelService.getLineup().contains(leadArtist));

        assertFalse(labelService.retireArtist(secondArtist));
        assertTrue(labelService.getAllArtists().contains(secondArtist));
        assertTrue(labelService.getLineup().contains(secondArtist));
    }

    @Test
    @DisplayName("A reserve artist can move into lineup without exceeding the configured lineup limit")
    void reserveArtistCanMoveIntoLineupWithinConfiguredLimit() {
        Artist thirdArtist = new Popstar("Third", 1, "Pop");
        Artist reserveArtist = new Rapper("Reserve", 1, "Rap");
        gameEnvironment.setLabel(new Label(
                "Test Label",
                List.of(leadArtist, secondArtist, thirdArtist, reserveArtist),
                gameEnvironment
        ));
        labelService = new LabelService(gameEnvironment);
        labelService.setLineUp(List.of(leadArtist, secondArtist, thirdArtist));

        labelService.setLineUp(List.of(leadArtist, secondArtist, reserveArtist));

        assertEquals(List.of(leadArtist, secondArtist, reserveArtist), labelService.getLineup());
        assertThrows(IllegalArgumentException.class,
                () -> labelService.setLineUp(List.of(leadArtist, secondArtist, thirdArtist, reserveArtist)));
    }

    private Item item(String name, int cost) {
        return new EquippedItem(
                name,
                "Useful stage gear",
                cost,
                Rarity.COMMON,
                List.of(ItemEffects.STAR_FUELLED)
        );
    }
}
