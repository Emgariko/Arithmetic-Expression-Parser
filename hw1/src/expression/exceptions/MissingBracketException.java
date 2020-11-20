package expression.exceptions;

public class MissingBracketException extends ParsingException {
    public MissingBracketException(String s, int pos) {
        super("Missing '" + s + "' bracket", pos);
    }
}
