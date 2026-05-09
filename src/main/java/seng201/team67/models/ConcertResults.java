package seng201.team67.models;

public class ConcertResults {

    public final Double ticketSales;
    public final Double bonusMoney;
    public final Double staminaChange;
    public final int crowdHype; //finishing crowd hype (crowd hype meter)
    public final Double artistsPay;
    public final Double total;

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
