package model;

/**
 * Represents the mathematical operations available in the calculator.
 */
public enum Operation {
    ADD(1),
    SUBTRACT(2),
    MULTIPLY(3),
    DIVIDE(4),
    EXIT(5);

    private final int value;

    Operation(int value) {
        this.value = value;
    }

    /**
     * Maps an integer menu choice to the corresponding Operation enum.
     */
    public static Operation fromInt(int value) {
        for (Operation op : values()) {
            if (op.value == value) {
                return op;
            }
        }
        return null;
    }
}
