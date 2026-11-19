package lab9;

public class StandartTicket implements Ticket {
    @Override public String type() { return "Standard"; }
    @Override public double price() { return 100.0; }
