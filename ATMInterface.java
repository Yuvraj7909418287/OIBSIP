import java.util.Scanner;

public class ATMInterface {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int userId = 1234;
        int userPin = 4321;

        double balance = 10000;
        String transactionHistory = "";

        System.out.println("===== ATM INTERFACE =====");

        System.out.print("Enter User ID: ");
        int id = sc.nextInt();

        System.out.print("Enter PIN: ");
        int pin = sc.nextInt();

        if (id == userId && pin == userPin) {

            System.out.println("Login Successful!");

            while (true) {

                System.out.println("\n===== ATM MENU =====");
                System.out.println("1. Transaction History");
                System.out.println("2. Withdraw");
                System.out.println("3. Deposit");
                System.out.println("4. Transfer");
                System.out.println("5. Check Balance");
                System.out.println("6. Quit");

                System.out.print("Choose Option: ");
                int choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        System.out.println("\nTransaction History:");
                        if (transactionHistory.equals("")) {
                            System.out.println("No Transactions Yet.");
                        } else {
                            System.out.println(transactionHistory);
                        }
                        break;

                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        double withdraw = sc.nextDouble();

                        if (withdraw <= balance) {
                            balance -= withdraw;
                            transactionHistory += "Withdraw: " + withdraw + "\n";
                            System.out.println("Withdrawal Successful!");
                        } else {
                            System.out.println("Insufficient Balance!");
                        }
                        break;

                    case 3:
                        System.out.print("Enter amount to deposit: ");
                        double deposit = sc.nextDouble();

                        balance += deposit;
                        transactionHistory += "Deposit: " + deposit + "\n";

                        System.out.println("Deposit Successful!");
                        break;

                    case 4:
                        System.out.print("Enter Receiver Account Number: ");
                        int accNo = sc.nextInt();

                        System.out.print("Enter amount to transfer: ");
                        double transfer = sc.nextDouble();

                        if (transfer <= balance) {
                            balance -= transfer;
                            transactionHistory += "Transferred: " + transfer + " to A/C " + accNo + "\n";

                            System.out.println("Transfer Successful!");
                        } else {
                            System.out.println("Insufficient Balance!");
                        }
                        break;

                    case 5:
                        System.out.println("Current Balance: " + balance);
                        break;

                    case 6:
                        System.out.println("Thank You for Using ATM!");
                        System.exit(0);

                    default:
                        System.out.println("Invalid Option!");
                }
            }

        } else {
            System.out.println("Invalid User ID or PIN!");
        }

        sc.close();
    }
}