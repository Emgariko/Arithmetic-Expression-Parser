package expression.exceptions;

public class InvalidOrMissedExpressionException extends ParsingException {
    public InvalidOrMissedExpressionException(String s, int pos) {
        super(s, pos);
    }
}
