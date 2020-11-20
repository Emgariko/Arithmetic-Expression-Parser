package expression;

import expression.exceptions.*;

public class CheckedSubtract extends AbstractOperation {
    public CheckedSubtract(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected int getPriority() {
        return 1;
    }

    public int makeOperation(int c, int d) {
        int res = c - d;
        /* if (d > 0 && res > c || d < 0 && res < c) {
            throw new OverflowSubstractException(Integer.toString(c) + " - " + Integer.toString(d));
        } */
        if ((d < 0 && Integer.MAX_VALUE + d < c) || (d > 0 && Integer.MIN_VALUE + d > c)) {
            throw new OverflowSubstractException(Integer.toString(c) + " - " + Integer.toString(d));
        }
        return c - d;
    }

    @Override
    public String operationSym() {
        return "-";
    }

    @Override
    protected double makeOperation(double c, double d) {
        return c - d;
    }
}
