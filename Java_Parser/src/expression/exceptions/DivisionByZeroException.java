package expression.exceptions;

public class DivisionByZeroException extends ExpressionException {
    public DivisionByZeroException(String msg) {
        super("Division by zero in " + msg + " operation.");
    }
}
