package expression.parser;

import expression.parser.Source;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    protected Source source;
    protected char ch;

    /* protected BaseParser(final Source source) {
        this.source = source;
    } */

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected boolean hasNextChar() { return source.hasNext(); }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean expect(final char c) {
        if (ch != c) {
            return false;
        }
        nextChar();
        return true;
    }

    protected boolean expect(final String value) {
        boolean res = true;
        for (char c : value.toCharArray()) {
            res &= expect(c);
        }
        return res;
    }

    protected RuntimeException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
