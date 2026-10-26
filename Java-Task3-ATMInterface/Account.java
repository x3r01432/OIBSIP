import java.util.ArrayList;import java.util.List;
public class Account {
    private final String id; private final String pin; private double balance;
    private final List<Transaction> history = new ArrayList<>();
    public Account(String id,String pin,double balance){this.id=id;this.pin=pin;this.balance=balance;}
    public String getId(){return id;}
    public boolean checkPin(String p){return pin.equals(p);}
    public double getBalance(){return balance;}
    public List<Transaction> getHistory(){return history;}
    public void deposit(double a){balance+=a;history.add(new Transaction("Deposit",a));}
    public boolean withdraw(double a){if(a>balance)return false;balance-=a;history.add(new Transaction("Withdraw",a));return true;}
    public void creditTransfer(double a){balance+=a;history.add(new Transaction("Transfer In",a));}
    public boolean debitTransfer(double a){if(a>balance)return false;balance-=a;history.add(new Transaction("Transfer Out",a));return true;}
}
