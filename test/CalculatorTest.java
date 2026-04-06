import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    void testAddition() {
        assertEquals(5, Calculator.add(2, 3));
    }

    @Test
    void testSubtraction() {
        assertEquals(1, Calculator.subtract(3, 2));
    }

    @Test
    void testMultiplication() {
        assertEquals(6, Calculator.multiply(2, 3));
    }

    @Test
    void testDivision() {
        assertEquals(2, Calculator.divide(4, 2));
    }

    @Test
    void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> {
            Calculator.divide(5, 0);
        });
    }
}
