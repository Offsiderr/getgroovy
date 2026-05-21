package seng201.team67.models;

/**
 * Represents the concert results used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ConcertResults {

    /** Numeric value for the ticket sales. */
    public final Double ticketSales;
    /** Numeric value for the bonus money. */
    public final Double bonusMoney;
    /** Numeric value for the stamina change. */
    public final Double staminaChange;
    /** Numeric value for the crowd hype. */
    public final int crowdHype; //finishing crowd hype (crowd hype meter)
    /** Numeric value for the artists pay. */
    public final Double artistsPay;
    /** Numeric value for the total. */
    public final Double total;

    /**
     * Creates a new concert results.
     * @param ticketSales the numeric value for the ticket sales
     * @param bonusMoney the numeric value for the bonus money
     * @param staminaChange the numeric value for the stamina change
     * @param crowdHype the numeric value for the crowd hype
     * @param artistsPay the numeric value for the artists pay
     * @param total the numeric value for the total
     */
    public ConcertResults(Double ticketSales, Double bonusMoney, Double staminaChange, int crowdHype, Double artistsPay, Double total)
    {
        this.ticketSales = ticketSales;
        this.bonusMoney = bonusMoney;
        this.staminaChange = staminaChange;
        this.crowdHype = crowdHype;
        this.artistsPay = artistsPay;
        this.total = total;
    }
}
