package expression.exceptions;

public class OverflowException extends ExpressionException {
    public OverflowException(String msg) {
        super("Overflow in " + msg + " operation.");
    }
}
