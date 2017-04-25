package _01_lesson;

/**
 * Created by v21k on 15.04.17.
 */
public class TestClass1 {
    public static List<String> check(){
    List<String> tests = new ArrayList<>();

    tests.add(String.format("Result: %b, expected: %d, actual: %d",
            sum(2, 2) == 4, 4, sum(2, 2)));

    tests.add(String.format("Result: %b, expected: %d, actual: %d",
            sum(3, 6) == 9, 9, sum(3, 6)));

    tests.add(String.format("Result: %b, expected: %d, actual: %d",
            sum(100, 200) == 300, 300, sum(100, 200)));

    tests.add(String.format("Result: %b, expected: %d, actual: %d",
            sum(555, 0) == 555, 555, sum(555, 0)));

    tests.add(String.format("Result: %b, expected: %d, actual: %d",
            sum(16, 9) == 25, 25, sum(16, 9)));

    tests.add(String.format("Result: %b, expected: %d, actual: %d",
            sum(15, -15) == 0, 0, sum(15, -15)));

    return tests;
}
}
