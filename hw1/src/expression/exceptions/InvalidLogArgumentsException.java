package expression.exceptions;

public class InvalidLogArgumentsException extends ExpressionException{
    public InvalidLogArgumentsException(String msg) {
        super("Invalid arguments in " + msg);
    }
}
