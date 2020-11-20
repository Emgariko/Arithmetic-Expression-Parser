package expression;

import expression.exceptions.*;

public class CheckedMultiply extends AbstractOperation {
    public CheckedMultiply(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    public static boolean checkOverflow(int c, int d) throws OverflowMultiplyException {
        return c == Integer.MIN_VALUE && d == -1|| c != 0 && d != 0 && (c * d) / d != c;
    }

    @Override
    public int makeOperation(int c, int d) {
        if (checkOverflow(c, d)) {
            throw new OverflowMultiplyException(Integer.toString(c) + " * " + Integer.toString(d));
        }
        return c * d;
    }

    @Override
    public String operationSym() {
        return "*";
    }

    @Override
    protected double makeOperation(double c, double d) {
        return c * d;
    }

}
