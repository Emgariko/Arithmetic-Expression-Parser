package expression;

import expression.exceptions.InvalidLogArgumentsException;

public class CheckedLog extends AbstractOperation {
    public CheckedLog(CommonExpression c, CommonExpression d) {
        super(c, d);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    public String operationSym() {
        return "//";
    }

    @Override
    protected double makeOperation(double c, double d) {
        return 0;
    }

    @Override
    protected int makeOperation(int c, int d) {
        if (c <= 0 || d == 1 || d <= 0) {
            throw new InvalidLogArgumentsException("Log_(d)(" + Integer.toString(c) + ")");
        }
        return calcLog(c, d);
    }

    public static int calcLog(int c, int d) {
        int x = c, res = 0;
        while (x > 0) {
            x /= d;
            res++;
        }
        return res - 1;
    }
}
