package expression;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Expression exp = new CheckedAdd(
                new CheckedSubtract(new CheckedMultiply(new Variable("x"), new Variable("x")),
                        new CheckedMultiply(new Variable("x"), new Const(2))
                            ),
                            new Const(1)
                                );
        System.out.println(exp.evaluate(5));
        System.out.println(exp.toString());
    }
}
