import java.util.Random;
import java.util.Scanner;

/**
 * Console Number Guessing Game with difficulty levels, attempt limits,
 * play-again, and multi-round scoring.
 */
public class NumberGuessingGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rng = new Random();
        int round = 0;
        boolean playing = true;

        System.out.println("=== Number Guessing Game ===");
        while (playing) {
            round++;
            int[] cfg = chooseDifficulty(sc); // {max, maxAttempts}
            int max = cfg[0], maxAttempts = cfg[1];
            int target = rng.nextInt(max) + 1;
            int attempts = 0;
            boolean won = false;

            System.out.printf("Round %d: Guess a number between 1 and %d (%d attempts).%n", round, max, maxAttempts);
            while (attempts < maxAttempts) {
                System.out.printf("Attempt %d/%d - enter your guess: ", attempts + 1, maxAttempts);
                if (!sc.hasNextInt()) { System.out.println("Please enter a whole number."); sc.next(); continue; }
                int g = sc.nextInt();
                attempts++;
                if (g == target) { System.out.println("Correct!"); won = true; break; }
                else if (g > target) System.out.println("Too High!");
                else System.out.println("Too Low!");
            }
            if (won) System.out.printf("Round %d - guessed in %d attempts.%n", round, attempts);
            else System.out.printf("You Lost! The number was %d.%n", target);

            System.out.print("Play again? (y/n): ");
            playing = sc.next().trim().equalsIgnoreCase("y");
        }
        System.out.println("Thanks for playing!");
        sc.close();
    }

    static int[] chooseDifficulty(Scanner sc) {
        System.out.println("Difficulty: 1=Easy(1-50,10 tries) 2=Medium(1-100,7) 3=Hard(1-200,5)");
        System.out.print("Choose (1-3): ");
        String c = sc.next().trim();
        switch (c) {
            case "1": return new int[]{50, 10};
            case "3": return new int[]{200, 5};
            default:  return new int[]{100, 7};
        }
    }
}
