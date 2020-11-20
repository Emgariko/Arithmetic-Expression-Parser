package expression.exceptions;

import expression.CheckedNegate;
import expression.CommonExpression;
import expression.parser.ExpressionParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ParsingException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ExpressionParser parser = new ExpressionParser();
        CommonExpression exp = parser.parse(scanner.nextLine());
        System.out.println(exp.toMiniString());
        System.out.println(exp.evaluate(2, 4, 8));
    }
}
