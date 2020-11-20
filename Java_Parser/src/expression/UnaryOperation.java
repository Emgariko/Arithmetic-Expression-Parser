package expression;

public abstract class UnaryOperation implements CommonExpression {
    CommonExpression exp;
    public UnaryOperation (CommonExpression expression) {
        exp = expression;
    }

}
