import model.Operation;
import service.CalculatorService;
import utils.InputValidator;

import java.util.Scanner;

/**
 * Entry point for the Calculator Application.
 * Handles the main program loop and user interaction.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CalculatorService calculatorService = new CalculatorService();
        boolean running = true;

        System.out.println("--- Java Console Calculator ---");

        while (running) {
            displayMenu();
            int choice = InputValidator.getValidMenuChoice(scanner);
            Operation operation = Operation.fromInt(choice);

            if (operation == Operation.EXIT) {
                System.out.println("Exiting... Goodbye!");
                running = false;
                continue;
            }

            if (operation == null) {
                System.out.println("Invalid operation selected.");
                continue;
            }

            // Retrieve validated numbers
            double num1 = InputValidator.getValidDouble(scanner, "Enter first number: ");
            double num2 = InputValidator.getValidDouble(scanner, "Enter second number: ");

            try {
                double result = 0;
                // Execute calculation based on operation
                switch (operation) {
                    case ADD:
                        result = calculatorService.add(num1, num2);
                        break;
                    case SUBTRACT:
                        result = calculatorService.subtract(num1, num2);
                        break;
                    case MULTIPLY:
                        result = calculatorService.multiply(num1, num2);
                        break;
                    case DIVIDE:
                        result = calculatorService.divide(num1, num2);
                        break;
                }
                
                // Format output to remove trailing zeros if it's a whole number, 
                // or display up to 4 decimal places.
                System.out.println("Result: " + formatResult(result));

            } catch (ArithmeticException e) {
                // Catch division by zero logic mapped from CalculatorService
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Displays the standard user menu.
     */
    private static void displayMenu() {
        System.out.println("\nSelect operation:");
        System.out.println("1. Add");
        System.out.println("2. Subtract");
        System.out.println("3. Multiply");
        System.out.println("4. Divide");
        System.out.println("5. Exit");
    }

    /**
     * Helper method to format double results beautifully.
     */
    private static String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.format("%s", result);
        }
    }
}
