package utils;

import java.util.Scanner;

/**
 * Handles all user input validation to prevent crashes from bad data.
 */
public class InputValidator {

    /**
     * Loops until the user enters a valid double.
     */
    public static double getValidDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Loops until the user enters a valid menu choice (1-5).
     */
    public static int getValidMenuChoice(Scanner scanner) {
        while (true) {
            System.out.print("Enter choice: ");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 5) {
                    return choice;
                } else {
                    System.out.println("Error: Please select an option between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input. Please enter a number.");
            }
        }
    }
}
