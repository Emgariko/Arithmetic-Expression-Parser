package expression;

import expression.exceptions.*;

public class CheckedDivide extends AbstractOperation {
    public CheckedDivide(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    public int makeOperation(int c, int d) {
        if (c == Integer.MIN_VALUE && d == -1) {
            throw new OverflowDivideExcpetpion(Integer.toString(c) + " / " + Integer.toString(d));
        }
        if (d == 0) {
            String msg = new String(Integer.toString(c) + " / " + Integer.toString(d));
            throw new DivisionByZeroException(msg);
        }
        return c / d;
    }

    @Override
    public String operationSym() {
        return "/";
    }
    @Override
    protected double makeOperation(double c, double d) {
        return c / d;
    }
}
