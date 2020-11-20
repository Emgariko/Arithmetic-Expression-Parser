package expression.exceptions;

public class InvalidPowArgumentsException extends ExpressionException {
    public InvalidPowArgumentsException(String msg) {
        super("Invalid arguments in " + msg);
    }
}
