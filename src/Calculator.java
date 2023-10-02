import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Calculator {
    public static final String THROWS_EXCEPTION = "throws Exception";

    public static void main(String[] args) throws IOException {
        Operation operation;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.println("Input:");
        String input = reader.readLine().replaceAll("\\s", "");

        try {
            validateInputOperands(input);
            operation = checkOperation(input);
        } catch (Exception e) {
            System.out.println(THROWS_EXCEPTION);
            return;
        }

        String regularExpression = String.format("\\%s", operation.getOperation());
        String[] digits = input.split(regularExpression);

        int[] intDigits = castToIntDigits(digits);
        if (intDigits.length > 0) {
            doIntScenario(operation, intDigits);
            return;
        }

        doRomanianScenario(operation, digits);
    }

    private static void doIntScenario(Operation operation, int[] intDigits) {
        int firstInt = intDigits[0];
        int secondInt = intDigits[1];

        try {
            validateIntBounds(firstInt, secondInt);
        } catch (Exception e) {
            System.out.println(THROWS_EXCEPTION);
            return;
        }

        int result = doOperation(operation, firstInt, secondInt);

        System.out.printf("\nOutput: \n%d", result);
    }

    private static void doRomanianScenario(Operation operation, String[] digits) {
        int firstInt;
        int secondInt;
        try {
            firstInt = RomanIntegerConverter.convertRomanToInt(digits[0]);
            secondInt = RomanIntegerConverter.convertRomanToInt(digits[1]);
            validateIntBounds(firstInt, secondInt);
        } catch (Exception e) {
            System.out.println(THROWS_EXCEPTION);
            return;
        }

        int result = doOperation(operation, firstInt, secondInt);

        String romanianResult;

        try {
            romanianResult = RomanIntegerConverter.intToRoman(result);
        } catch (Exception e) {
            System.out.println(THROWS_EXCEPTION);
            return;
        }

        System.out.printf("\nOutput: \n%s", romanianResult);
    }

    private static int doOperation(Operation operation, int firstInt, int secondInt) {
        int result = 0;

        switch (operation) {
            case MINUS -> result = firstInt - secondInt;
            case PLUS -> result = firstInt + secondInt;
            case DIVIDE -> result = firstInt / secondInt;
            case MULTIPLY -> result = firstInt * secondInt;
        }

        return result;
    }

    private static Operation checkOperation(String input) throws Exception {
        if (input.contains(Operation.PLUS.operation)) {
            return Operation.PLUS;
        } else if (input.contains(Operation.MINUS.operation)) {
            return Operation.MINUS;
        } else if (input.contains(Operation.DIVIDE.operation)) {
            return Operation.DIVIDE;
        } else if (input.contains(Operation.MULTIPLY.operation)) {
            return Operation.MULTIPLY;
        } else {
            throw new Exception();
        }
    }

    private static int[] castToIntDigits(String[] digits) {
        int[] intDigits = new int[2];
        try {
            intDigits[0] = Integer.parseInt(digits[0]);
            intDigits[1] = Integer.parseInt(digits[1]);
            return intDigits;
        } catch (NumberFormatException e) {
            return new int[0];
        }
    }

    private static void validateInputOperands(String input) throws Exception {
        int numberOfOperands = 0;
        for (Operation operation : Operation.values()) {
            if (input.contains(operation.getOperation())) {
                numberOfOperands++;
            }
        }

        if (numberOfOperands != 1)
            throw new Exception();
    }

    private static void validateIntBounds(int firstInt, int secondInt) throws Exception {
        if (firstInt <= 0 || firstInt > 10 || secondInt <= 0 || secondInt > 10) {
            throw new Exception();
        }
    }

    public enum Operation {
        PLUS("+"), MINUS("-"), DIVIDE("/"), MULTIPLY("*");

        private final String operation;

        Operation(String operation) {
            this.operation = operation;
        }

        public String getOperation() {
            return operation;
        }
    }

    public class RomanIntegerConverter {
        public static int convertRomanToInt(String input) throws Exception {
            Map<Character, Integer> map = new HashMap<>();
            map.put('I', 1);
            map.put('V', 5);
            map.put('X', 10);
            map.put('L', 50);
            map.put('C', 100);
            map.put('D', 500);
            map.put('M', 1000);
            input = input.replace("IV", "IIII");
            input = input.replace("IX", "VIIII");
            input = input.replace("XL", "XXXX");
            input = input.replace("XC", "LXXXX");
            input = input.replace("CD", "CCCC");
            input = input.replace("CM", "DCCCC");
            int number = 0;
            for (int i = 0; i < input.length(); i++) {
                number = number + (map.get(input.charAt(i)));
            }
            return number;
        }

        public static String intToRoman(int number) throws Exception {

            if (number < 1) {
                throw new Exception();
            }

            String[] thousands = {"", "M", "MM", "MMM"};
            String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
            String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
            String[] units = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
            return thousands[number / 1000] + hundreds[(number % 1000) / 100] + tens[(number % 100) / 10] + units[number % 10];
        }
    }
}



