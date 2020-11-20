package expression;

public class Variable implements CommonExpression {
    private String name;
    public Variable (String x) {
        this.name = x;
    }
    @Override
    public int evaluate(int x) {
        return x;
    }
    @Override
    public String toString() {
        return name;
    }
    @Override
    public boolean equals(Object object) {
        if (object != null && object.getClass() == getClass())
            return this.name.equals(((Variable) object).name);
        return false;
    }
    @Override
    public String toMiniString() {
        return this.name;
    }
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int ans = -1;
        if (name.equals("x")) {
            ans = x;
        } else if (name.equals("y")) {
            ans = y;
        } else if (name.equals("z")) {
            ans = z;
        }
        return ans;
    }
}
