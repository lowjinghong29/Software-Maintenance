package oop_assignment.util;

import oop_assignment.constant.Messages;
import java.util.Scanner;

/**
 * Utility class for safe console input reading.
 */
public class InputUtils {

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } else {
                System.out.println(Messages.INVALID_NUMBER);
                scanner.nextLine(); // consume invalid input
            }
        }
    }

    public static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                return value;
            } else {
                System.out.println(Messages.INVALID_NUMBER);
                scanner.nextLine(); // consume invalid input
            }
        }
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty.");
            }
        }
    }
}
