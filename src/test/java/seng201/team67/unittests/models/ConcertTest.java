package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.Concert;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConcertTest {

    @Test
    void constructorInitialisesTicketSalesFromConfig() {
        GameEnvironment gameEnvironment = new GameEnvironment();

        Concert concert = new Concert(gameEnvironment);

        assertEquals(gameEnvironment.getConfig().artistRerollCost, concert.getTicketSales());
    }

    @Test
    void addEnergyAccumulatesEnergyAcrossCalls() {
        Concert concert = new Concert(new GameEnvironment());

        concert.addEnergy(4);
        concert.addEnergy(3);

        assertEquals(7, concert.getEnergy());
    }
}
