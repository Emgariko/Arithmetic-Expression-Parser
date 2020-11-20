package expression;

import expression.exceptions.OverflowNegateExcpetpion;

public class CheckedNegate extends UnaryOperation {

    public CheckedNegate(CommonExpression exp) {

        super(exp);
    }

    @Override
    public int evaluate(int x) {
        int val = exp.evaluate(x);
        if (val == Integer.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Integer.toString(val) + ")");
        }
        return -val;
    }

    @Override
    public double evaluate(double x) {
        return -exp.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int val = exp.evaluate(x, y, z);
        if (val == Integer.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Integer.toString(val) + ")");
        }
        if (val == Integer.MIN_VALUE) {
            throw new OverflowNegateExcpetpion("-(" + Integer.toString(val) + ")");
        }
        return -val;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("-");
        res.append("(");
        res.append(exp.toString());
        res.append(")");
        return res.toString();
    }
}
