package expression;

import expression.exceptions.*;

public class CheckedPow extends AbstractOperation {
    public CheckedPow(CommonExpression c, CommonExpression d) {
        super(c, d);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected double makeOperation(double c, double d) {
        return 0;
    }

    @Override
    protected int makeOperation(int c, int d) {
        if (c == 0 && d == 0 || d < 0) {
            throw new InvalidPowArgumentsException("(" + Integer.toString(c) +  " ** " + Integer.toString(d) + ")");
        }
        return caclPow(c, d);
    }

    private int caclPow(int c, int d) {
        int y = d;
        int res = 1, x = c;
        while (y > 0) {
            if (y % 2 == 0) {
                if (CheckedMultiply.checkOverflow(x, x)) {
                    throw new OverflowPowException(Integer.toString(c) + " ** " + Integer.toString(d));
                }
                x *= x;
                y /= 2;
            }  else {
                if (CheckedMultiply.checkOverflow(res, x)) {
                    throw new OverflowPowException(Integer.toString(c) + " ** " + Integer.toString(d));
                }
                res *= x;
                y--;
            }
        }
        return res;
    }

    @Override
    protected String operationSym() {
        return "**";
    }
}
