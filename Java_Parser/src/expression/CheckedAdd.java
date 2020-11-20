package expression;

import expression.exceptions.*;

public class CheckedAdd extends AbstractOperation {
    public CheckedAdd(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected int getPriority() {
        return 1;
    }
    @Override
    public int makeOperation(int c, int d) {
        int res = c + d;
        /* if (d > 0 && res < c || d < 0 && res > c) {
            throw new OverflowAddException(Integer.toString(c) + " + " + Integer.toString(d));
        } */
        if (((d > 0 && Integer.MAX_VALUE - d < c) || (d < 0 && Integer.MIN_VALUE - d > c))) {
            throw new OverflowAddException(Integer.toString(c) + " + " + Integer.toString(d));
        }
        return c + d;
    }

    @Override
    public String operationSym() {
        return "+";
    }
    @Override
    protected double makeOperation(double c, double d) {
        return c + d;
    }
}
