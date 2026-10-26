import java.util.Scanner;
public class ATM {
    private final Bank bank; private final Scanner sc;
    public ATM(Bank bank,Scanner sc){this.bank=bank;this.sc=sc;}
    public void start(){
        Account acc=authenticate();
        if(acc==null){System.out.println("Access denied after 3 attempts."); return;}
        menu(acc);
    }
    private Account authenticate(){
        for(int i=0;i<3;i++){
            System.out.print("User ID: "); String id=sc.next();
            System.out.print("PIN: "); String pin=sc.next();
            Account a=bank.login(id,pin);
            if(a!=null){System.out.println("Login successful.");return a;}
            System.out.println("Incorrect credentials. Attempts left: "+(2-i));
        }
        return null;
    }
    private void menu(Account acc){
        while(true){
            System.out.println("\n1.History 2.Withdraw 3.Deposit 4.Transfer 5.Quit");
            System.out.print("Choose: "); String c=sc.next();
            switch(c){
                case "1": acc.getHistory().forEach(System.out::println);
                          if(acc.getHistory().isEmpty())System.out.println("No transactions yet."); break;
                case "2": System.out.print("Amount: "); double w=readAmt();
                          System.out.println(acc.withdraw(w)?"Done. Balance $"+acc.getBalance():"Insufficient Funds"); break;
                case "3": System.out.print("Amount: "); acc.deposit(readAmt());
                          System.out.println("Done. Balance $"+acc.getBalance()); break;
                case "4": System.out.print("Recipient ID: "); String rid=sc.next();
                          Account r=bank.find(rid);
                          if(r==null){System.out.println("No such account.");break;}
                          System.out.print("Amount: "); double t=readAmt();
                          if(acc.debitTransfer(t)){r.creditTransfer(t);System.out.println("Transferred. Balance $"+acc.getBalance());}
                          else System.out.println("Insufficient Funds"); break;
                case "5": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid option.");
            }
        }
    }
    private double readAmt(){ while(!sc.hasNextDouble()){System.out.print("Enter a number: ");sc.next();} return sc.nextDouble(); }
}
