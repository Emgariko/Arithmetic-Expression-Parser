package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Source {
    boolean hasNext();
    char next();
    int getPos();
    RuntimeException error(final String message);
}
