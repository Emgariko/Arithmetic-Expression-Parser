package expression;

import java.util.Objects;

public abstract class AbstractOperation implements CommonExpression {
    private CommonExpression a, b;
    public AbstractOperation(CommonExpression c, CommonExpression d) {
        a = c;
        b = d;
    }
    protected abstract int getPriority();
    protected abstract double makeOperation(double c, double d);
    protected abstract int makeOperation(int c, int d);
    public int evaluate(int x) {
        return makeOperation(a.evaluate(x), b.evaluate(x));
    }
    public double evaluate(double x) {
        return makeOperation(a.evaluate(x), b.evaluate(x));
    }
    public int evaluate(int x, int y, int z) {
        return makeOperation(a.evaluate(x, y, z), b.evaluate(x,y, z));
    }

    protected abstract String operationSym();

    public String toString() {
        StringBuilder res = new StringBuilder("(");
        res.append(a.toString());
        res.append(" ");
        res.append(operationSym());
        res.append(" ");
        res.append(b.toString());
        res.append(")");
        return res.toString();
    }
    private boolean checkPrior(Expression e) {
        return (e instanceof AbstractOperation && ((AbstractOperation) e).getPriority() < this.getPriority());
    }
    private boolean isImportant(Expression e) { // special cases
        return  (e instanceof AbstractOperation &&
                ((this instanceof CheckedSubtract && ((AbstractOperation) e).getPriority() == 1)
                || this instanceof CheckedDivide)) ||
                (e instanceof CheckedDivide && this.getPriority() == 2);
    }
    private String getExpression(Expression e, boolean needBrackets) { // получаем выражение и мб оборачиваем его в скобки
        if (needBrackets) {
            return "(" + e.toMiniString() + ")";
        } else {
            return e.toMiniString();
        }
    }

    public String toMiniString() {
        StringBuilder res = new StringBuilder();
        res.append(getExpression(a, checkPrior(a)));
        res.append(" ");
        res.append(this.operationSym());
        res.append(" ");
        res.append(getExpression(b, checkPrior(b) || isImportant(b)));
        return res.toString();
    }
    @Override
    public boolean equals(Object object) {
        if (object != null && object.getClass() == getClass()) {
            return (((AbstractOperation) object).a.equals(this.a) &&
                    ((AbstractOperation) object).b.equals(this.b) &&
                    ((AbstractOperation) object).operationSym().equals(this.operationSym()));
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(a.hashCode(), b.hashCode(), operationSym().hashCode());
    }
}
