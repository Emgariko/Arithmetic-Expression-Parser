package expression.exceptions;

public class ParsingException extends Exception {
    public ParsingException(String msg, int pos) {
        super("Parsing error at " + Integer.toString(pos) + ", " + msg + ".");
    }
}
