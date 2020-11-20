package expression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class DoubleExpressionTest extends ExpressionTest {
    public DoubleExpressionTest(final boolean checkMini) {
        super(checkMini);
    }

    @Override
    protected void test() {
        super.test();

        handmade();
        generated();
    }

    private void handmade() {
        testExpression("10", "10", new Const(10), x -> 10);
        testExpression("x", "x", new Variable("x"), x -> x);
        testExpression("(x + 2)", "x + 2", new CheckedAdd(new Variable("x"), new Const(2)), x -> x + 2);
        testExpression("(2 - x)", "2 - x", new CheckedSubtract(new Const(2), new Variable("x")), x -> 2 - x);
        testExpression("(3 * x)", "3 * x", new CheckedMultiply(new Const(3), new Variable("x")), x -> 3*x);
        testExpression("(x + x)", "x + x", new CheckedAdd(new Variable("x"), new Variable("x")), x -> x + x);
        testExpression("(x / -2)", "x / -2", new CheckedDivide(new Variable("x"), new Const(-2)), x -> -x / 2);
        testExpression("(x + x)", "x + x", new CheckedAdd(new Variable("x"), new Variable("x")), x -> x + x);
        testExpression("(2 + x)", "2 + x", new CheckedAdd(new Const(2), new Variable("x")), x -> 2 + x);
        testExpression("(x + 2)", "x + 2", new CheckedAdd(new Variable("x"), new Const(2)), x -> x + 2);
        testExpression("((1 + 2) + 3)", "1 + 2 + 3", new CheckedAdd(new CheckedAdd(new Const(1), new Const(2)), new Const(3)), x -> 6);
        testExpression("(1 + (2 + 3))", "1 + 2 + 3", new CheckedAdd(new Const(1), new CheckedAdd(new Const(2), new Const(3))), x -> 6);
        testExpression("((1 - 2) - 3)", "1 - 2 - 3", new CheckedSubtract(new CheckedSubtract(new Const(1), new Const(2)), new Const(3)), x -> -4);
        testExpression("(1 - (2 - 3))", "1 - (2 - 3)", new CheckedSubtract(new Const(1), new CheckedSubtract(new Const(2), new Const(3))), x -> 2);
        testExpression("((1 * 2) * 3)", "1 * 2 * 3", new CheckedMultiply(new CheckedMultiply(new Const(1), new Const(2)), new Const(3)), x -> 6);
        testExpression("(1 * (2 * 3))", "1 * 2 * 3", new CheckedMultiply(new Const(1), new CheckedMultiply(new Const(2), new Const(3))), x -> 6);
        testExpression("((10 / 2) / 3)", "10 / 2 / 3", new CheckedDivide(new CheckedDivide(new Const(10), new Const(2)), new Const(3)), x -> 5.0 / 3);
        testExpression("(10 / (3 / 2))", "10 / (3 / 2)", new CheckedDivide(new Const(10), new CheckedDivide(new Const(3), new Const(2))), x -> 20.0 / 3);
        testExpression(
                "((x * x) + ((x - 1) / 10))",
                "x * x + (x - 1) / 10",
                new CheckedAdd(
                        new CheckedMultiply(new Variable("x"), new Variable("x")),
                        new CheckedDivide(new CheckedSubtract(new Variable("x"), new Const(1)), new Const(10))
                ),
                x -> x * x + (x - 1) / 10
        );
        testExpression("(x * -1000000000)", "x * -1000000000", new CheckedMultiply(new Variable("x"), new Const(-1_000_000_000)), x -> x * -1_000_000_000);
        testExpression("(10 / x)", "10 / x", new CheckedDivide(new Const(10), new Variable("x")), x -> 10 / x);
        testExpression("(x / x)", "x / x", new CheckedDivide(new Variable("x"), new Variable("x")), x -> x / x);
    }

    private void generated() {
        final Variable vx = new Variable("x");
        final Const c1 = new Const(1.1);
        final Const c2 = new Const(2.1);

        testExpression("(x + x)", "x + x", new CheckedAdd(vx, vx), x -> x + x);
        testExpression("(x - x)", "x - x", new CheckedSubtract(vx, vx), x -> x - x);
        testExpression("(1.1 * x)", "1.1 * x", new CheckedMultiply(c1, vx), x -> 1.1 * x);
        testExpression("(1.1 / 2.1)", "1.1 / 2.1", new CheckedDivide(c1, c2), x -> 1.1 / 2.1);
        testExpression("(1.1 + (1.1 / 2.1))", "1.1 + 1.1 / 2.1", new CheckedAdd(c1, new CheckedDivide(c1, c2)), x -> 1.1 + 1.1 / 2.1);
        testExpression("(2.1 - (1.1 * x))", "2.1 - 1.1 * x", new CheckedSubtract(c2, new CheckedMultiply(c1, vx)), x -> 2.1 - 1.1 * x);
        testExpression("(2.1 * (x + x))", "2.1 * (x + x)", new CheckedMultiply(c2, new CheckedAdd(vx, vx)), x -> 2.1 * (x + x));
        testExpression("(x / (x - x))", "x / (x - x)", new CheckedDivide(vx, new CheckedSubtract(vx, vx)), x -> x / (x - x));
        testExpression("((x - x) + 1.1)", "x - x + 1.1", new CheckedAdd(new CheckedSubtract(vx, vx), c1), x -> x - x + 1.1);
        testExpression("((1.1 / 2.1) - x)", "1.1 / 2.1 - x", new CheckedSubtract(new CheckedDivide(c1, c2), vx), x -> 1.1 / 2.1 - x);
        testExpression("((1.1 / 2.1) * x)", "1.1 / 2.1 * x", new CheckedMultiply(new CheckedDivide(c1, c2), vx), x -> 1.1 / 2.1 * x);
        testExpression("((x - x) / 2.1)", "(x - x) / 2.1", new CheckedDivide(new CheckedSubtract(vx, vx), c2), x -> (x - x) / 2.1);
        testExpression("(x + ((1.1 / 2.1) * x))", "x + 1.1 / 2.1 * x", new CheckedAdd(vx, new CheckedMultiply(new CheckedDivide(c1, c2), vx)), x -> x + 1.1 / 2.1 * x);
        testExpression("(2.1 - (2.1 - (1.1 * x)))", "2.1 - (2.1 - 1.1 * x)", new CheckedSubtract(c2, new CheckedSubtract(c2, new CheckedMultiply(c1, vx))), x -> 2.1 - (2.1 - 1.1 * x));
        testExpression("(1.1 * ((x - x) + 1.1))", "1.1 * (x - x + 1.1)", new CheckedMultiply(c1, new CheckedAdd(new CheckedSubtract(vx, vx), c1)), x -> 1.1 * (x - x + 1.1));
        testExpression("(x / (2.1 - (1.1 * x)))", "x / (2.1 - 1.1 * x)", new CheckedDivide(vx, new CheckedSubtract(c2, new CheckedMultiply(c1, vx))), x -> x / (2.1 - 1.1 * x));
        testExpression("((1.1 * x) + (1.1 / 2.1))", "1.1 * x + 1.1 / 2.1", new CheckedAdd(new CheckedMultiply(c1, vx), new CheckedDivide(c1, c2)), x -> 1.1 * x + 1.1 / 2.1);
        testExpression("((x + x) - (1.1 * x))", "x + x - 1.1 * x", new CheckedSubtract(new CheckedAdd(vx, vx), new CheckedMultiply(c1, vx)), x -> x + x - 1.1 * x);
        testExpression("((1.1 * x) * (1.1 / 2.1))", "1.1 * x * (1.1 / 2.1)", new CheckedMultiply(new CheckedMultiply(c1, vx), new CheckedDivide(c1, c2)), x -> 1.1 * x * (1.1 / 2.1));
        testExpression("((1.1 * x) / (x + x))", "1.1 * x / (x + x)", new CheckedDivide(new CheckedMultiply(c1, vx), new CheckedAdd(vx, vx)), x -> 1.1 * x / (x + x));
        testExpression("(((x - x) / 2.1) + 2.1)", "(x - x) / 2.1 + 2.1", new CheckedAdd(new CheckedDivide(new CheckedSubtract(vx, vx), c2), c2), x -> (x - x) / 2.1 + 2.1);
        testExpression("((x / (x - x)) - 1.1)", "x / (x - x) - 1.1", new CheckedSubtract(new CheckedDivide(vx, new CheckedSubtract(vx, vx)), c1), x -> x / (x - x) - 1.1);
        testExpression("((2.1 - (1.1 * x)) * 1.1)", "(2.1 - 1.1 * x) * 1.1", new CheckedMultiply(new CheckedSubtract(c2, new CheckedMultiply(c1, vx)), c1), x -> (2.1 - 1.1 * x) * 1.1);
        testExpression("((x / (x - x)) / x)", "x / (x - x) / x", new CheckedDivide(new CheckedDivide(vx, new CheckedSubtract(vx, vx)), vx), x -> x / (x - x) / x);
    }

    private void testExpression(final String full, final String mini, final DoubleExpression actual, final DoubleExpression expected) {
        System.out.println("Testing " + mini);
        System.out.println(actual.evaluate(0) +  " " + expected.evaluate(0));
        for (int i = 0; i < 10; i++) {
            check(i, actual, expected);
            check(-i, actual, expected);
        }
        checkEqualsAndToString(full, mini, actual);
    }

    private static void check(final int x, final DoubleExpression actual, final DoubleExpression expected) {
        assertEquals(String.format("f(%d)", x), expected.evaluate(x), actual.evaluate(x));
    }

    public static void main(final String[] args) {
        new DoubleExpressionTest(mode(args)).run();
    }
}