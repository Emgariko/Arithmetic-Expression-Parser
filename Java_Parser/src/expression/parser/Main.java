package expression.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import expression.CommonExpression;
import expression.TripleExpression;
import expression.exceptions.ParsingException;

public class Main {

    public static void main(String[] args) throws ParsingException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = scanner.nextLine();
        ExpressionParser parser = new ExpressionParser();
        TripleExpression exp = parser.parse(s);
        System.out.println(exp.toString());
    }
}
