package expression;

import expression.exceptions.InvalidLogArgumentsException;

public class CheckedLog2 extends UnaryOperation{
    public CheckedLog2(CommonExpression expression) {
        super(expression);
    }

    @Override
    public int evaluate(int x) {
        int val = exp.evaluate(x);
        int res = CheckedLog.calcLog(val, 2);
        if (res <= 0) {
            throw new InvalidLogArgumentsException("Log2(" + Integer.toString(res) + ")");
        }
        return res;
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int val = exp.evaluate(x, y, z);
        if (val <= 0) {
            throw new InvalidLogArgumentsException("Log_(2)(" + Integer.toString(val) + ")");
        }
        int res = CheckedLog.calcLog(val, 2);
        return res;
    }
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("log2");
        res.append("(");
        res.append(exp.toString());
        res.append(")");
        return res.toString();
    }
}
