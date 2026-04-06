package service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorServiceTest {

    CalculatorService service = new CalculatorService();

    @Test
    void testAddition() {
        assertEquals(5.0, service.add(2, 3));
        assertEquals(-1.0, service.add(2, -3));
    }

    @Test
    void testSubtraction() {
        assertEquals(1.0, service.subtract(3, 2));
        assertEquals(-5.0, service.subtract(-2, 3));
    }

    @Test
    void testMultiplication() {
        assertEquals(6.0, service.multiply(2, 3));
        assertEquals(0.0, service.multiply(5, 0));
    }

    @Test
    void testDivision() {
        assertEquals(2.0, service.divide(4, 2));
        assertEquals(-2.5, service.divide(-5, 2));
    }

    @Test
    void testDivideByZero() {
        Exception exception = assertThrows(ArithmeticException.class, () -> {
            service.divide(5, 0);
        });

        assertEquals("Cannot divide by zero", exception.getMessage());
    }
}
