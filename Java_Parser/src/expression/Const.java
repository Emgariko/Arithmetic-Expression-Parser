package expression;

public class Const implements CommonExpression {
    private int val;
    private double val1;
    private boolean isInt;
    public Const(int i) {
        val = i;
        val1 = i;
        isInt = true;
    }
    public Const(double i) {
        val1 = i;
        val = (int)i;
        isInt = false;
    }
    @Override
    public int evaluate(int x) {
        return val;
    }
    @Override
    public String toString() {
        if (isInt)
            return Integer.toString(val);
        else
            return Double.toString(val1);
    }
    @Override
    public String toMiniString() {
        if (isInt)
            return Integer.toString(val);
        else
            return Double.toString(val1);
    }
    @Override
    public boolean equals(Object object) {
        if (object != null && object.getClass() == getClass())
            return this.val == ((Const) object).val && this.val1 == ((Const) object).val1;
        return false;
    }
    @Override
    public int hashCode() {
        return this.val;
    }

    @Override
    public double evaluate(double x) {
        return this.val1;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.val;
    }
}
