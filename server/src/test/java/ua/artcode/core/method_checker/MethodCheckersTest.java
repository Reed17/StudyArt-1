package ua.artcode.core.method_checker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class MethodCheckersTest {
    Class classWithMain;
    Class classWithTest;

    @Before
    public void beforeTest() {
        classWithMain = ClassWithMain.class;
        classWithTest = MethodCheckersTest.class;

    }

    @Test
    public void testPositiveCheckerMain() {
        try {
            MethodCheckers.main.checkClasses(new Class[]{classWithMain});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("Main Method Not Found!");
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNegativeCheckerMain() throws NoSuchMethodException {
        MethodCheckers.main.checkClasses(new Class[]{classWithTest});

    }

    @Test
    public void testPositiveCheckerTest() {
        try {
            MethodCheckers.testChecker.checkClasses(new Class[]{classWithTest});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("@Test Method Not Found!");
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNegativeCheckerTest() throws NoSuchMethodException {
        MethodCheckers.testChecker.checkClasses(new Class[]{classWithMain});

    }

    //Class with main method for test
    private class ClassWithMain {
        public void main() {
            System.out.println("Test");
        }
    }

}
