package ua.artcode.core.method_checker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class MethodCheckersTest {
    Class classWithMain;
    Class classWithTest;
    Class[] classes;

    @Before
    public void beforeTest() {
        classWithMain = ClassWithMain.class;
        classWithTest = MethodCheckersTest.class;
        classes = new Class[1];

    }

    @Test
    public void testPositiveCheckerMain() {
        classes[0] = classWithMain;
        try {
            MethodCheckers.main.checkClasses(classes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("Main Method Not Found!");
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNegativeCheckerMain() throws NoSuchMethodException {
        classes[0] = classWithTest;
        MethodCheckers.main.checkClasses(classes);

    }

    @Test
    public void testPositiveCheckerTest() {
        classes[0] = classWithTest;
        try {
            MethodCheckers.testChecker.checkClasses(classes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("@Test Method Not Found!");
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNegativeCheckerTest() throws NoSuchMethodException {
        classes[0] = classWithMain;
        MethodCheckers.testChecker.checkClasses(classes);

    }

    //Class with main method for test
    private class ClassWithMain {
        public void main() {
            System.out.println("Test");
        }
    }

}
