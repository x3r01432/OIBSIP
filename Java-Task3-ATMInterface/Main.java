import java.util.Scanner;
public class Main {
    public static void main(String[] args){
        System.out.println("=== Welcome to the ATM ===");
        System.out.println("(Demo accounts: 1001/1234, 1002/0000)");
        new ATM(new Bank(), new Scanner(System.in)).start();
    }
}
