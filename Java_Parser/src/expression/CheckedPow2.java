package expression;

import expression.exceptions.*;

public class CheckedPow2 extends UnaryOperation {

    public CheckedPow2(CommonExpression exp) {
        super(exp);
    }

    @Override
    public int evaluate(int x) {
        int val = exp.evaluate(x);
        if (val < 0) {
            throw new InvalidPowArgumentsException("Pow2(" + Integer.toString(val) + ")");
        }
        int res = calcPow2(val);
        return res;
    }

    private int calcPow2(int val) {
        if (val >= 31) {
            throw new OverflowPowException("2 ** " + Integer.toString(val));
        } else {
            return 1 << val;
        }
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int val = exp.evaluate(x, y, z);
        if (val < 0) {
            throw new InvalidPowArgumentsException("Pow2(" + Integer.toString(val) + ")");
        }
        int res = calcPow2(val);
        return res;
    }
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("pow2");
        res.append("(");
        res.append(exp.toString());
        res.append(")");
        return res.toString();
    }
}
