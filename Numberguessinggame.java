import java.util.Random;
import java.util.Scanner;

public class Numberguessinggame {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int number = random.nextInt(100) + 1;
        int guess = 0;
        int attempts = 0;

        System.out.println("=== NUMBER GUESSING GAME ===");
        System.out.println("Guess a number between 1 and 100");

        while (guess != number) {

            System.out.print("Enter your guess: ");
            guess = sc.nextInt();

            attempts++;

            if (guess > number) {
                System.out.println("Too High!");
            } 
            else if (guess < number) {
                System.out.println("Too Low!");
            } 
            else {
                System.out.println("Correct Number!");
                System.out.println("Attempts: " + attempts);
            }
        }

        sc.close();
    }
}
