import java.time.LocalDateTime;
public class Transaction {
    private final String type; private final double amount; private final LocalDateTime time;
    public Transaction(String type, double amount){this.type=type;this.amount=amount;this.time=LocalDateTime.now();}
    public String toString(){return String.format("%s | %-10s | $%.2f", time, type, amount);}
}
