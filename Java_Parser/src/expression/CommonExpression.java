package expression;

public interface CommonExpression extends Expression, DoubleExpression, TripleExpression {
    int evaluate(int x);
    String toString();
}
