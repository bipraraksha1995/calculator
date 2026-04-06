package utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class InputValidatorTest {

    @Test
    void testValidDoubleInput() {
        String input = "10.5\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        double result = InputValidator.getValidDouble(scanner, "Enter number: ");
        assertEquals(10.5, result);
    }

    @Test
    void testInvalidThenValidDoubleInput() {
        String input = "abc\n5.5\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        double result = InputValidator.getValidDouble(scanner, "Enter number: ");
        assertEquals(5.5, result);
    }

    @Test
    void testValidMenuChoice() {
        String input = "3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        int result = InputValidator.getValidMenuChoice(scanner);
        assertEquals(3, result);
    }

    @Test
    void testInvalidThenValidMenuChoice() {
        String input = "9\n2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        int result = InputValidator.getValidMenuChoice(scanner);
        assertEquals(2, result);
    }
}
