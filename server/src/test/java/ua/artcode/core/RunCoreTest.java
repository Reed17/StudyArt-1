package ua.artcode.core;

import com.google.common.io.Resources;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.artcode.Application;
import ua.artcode.core.method_checker.MethodCheckers;
import ua.artcode.core.method_runner.Runners;
import ua.artcode.core.post_processor.ResultsProcessors;
import ua.artcode.core.pre_processor.PreProcessors;
import ua.artcode.model.response.RunResults;
import ua.artcode.utils.IO_utils.CourseIOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by v21k on 27.04.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class RunCoreTest {

    @Value("${test.paths.core}")
    private String projectRoot;
    private String sourcesRoot;
    private String sourcesTestRoot;

    @Autowired
    private RunCore core;

    @Autowired
    private CourseIOUtils courseIOUtils;

    @Before
    public void setUp() throws Exception {


        List<String> directoryTree = new ArrayList<>();
        directoryTree.add(projectRoot + "/src/main/java");
        directoryTree.add(projectRoot + "/src/main/resources");
        directoryTree.add(projectRoot + "/src/test/java");
        directoryTree.add(projectRoot + "/src/test/resources");
        directoryTree.add(projectRoot + "/src/main/java/_01_lesson");
        directoryTree.add(projectRoot + "/src/main/java/_02_lesson");
        directoryTree.add(projectRoot + "/src/main/java/_03_lesson");
        directoryTree.add(projectRoot + "/src/test/java/_01_lesson");
        directoryTree.add(projectRoot + "/src/test/java/_02_lesson");
        directoryTree.add(projectRoot + "/src/test/java/_03_lesson");

        directoryTree.stream()
                .map(path -> path.replace("/", File.separator))
                .map(Paths::get)
                .forEach(path -> {
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        sourcesRoot = projectRoot + "/src/main/java".replace("/", File.separator);
        sourcesTestRoot = projectRoot + "/src/test/java".replace("/", File.separator);
    }

    @After
    public void tearDown() throws Exception {
        File coreTestDir = new File(this.projectRoot);
        if (coreTestDir.exists()) {
            FileUtils.deleteDirectory(coreTestDir);
        }
    }

    @Test
    public void testRunClassWithMainPositive() throws Exception {
        String classBody = insertInMain("System.out.print(\"Some text\");");
        String classPath = generateAndSaveClass(1, "", "Main", classBody, false);

        RunResults results = core.run(
                new String[]{sourcesRoot},
                new String[]{classPath},
                new String[]{},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        assertEquals("Some text", results.getMethodResult().getSystemOut());
    }

    @Test(expected = NoSuchMethodException.class)
    public void testRunClassWithNoMain() throws Exception {
        String classBody = "";
        String classPath = generateAndSaveClass(1, "", "Main", classBody, false);

        core.run(
                new String[]{sourcesRoot},
                new String[]{classPath},
                new String[]{},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);
    }

    @Test
    public void testRunClassWithMainCompilationError() throws Exception {
        String classBody = insertInMain("System.out.println(\"Some text\";");
        String classPath = generateAndSaveClass(1, "", "Main", classBody, false);

        RunResults results = core.run(
                new String[]{sourcesRoot},
                new String[]{classPath},
                new String[]{},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        assertTrue(results.getGeneralResponse().getMessage().contains("error"));
    }

    @Test
    public void testRunClassWithMainRuntimeError() throws Exception {
        String classBody = insertInMain("System.out.println(2/0);");
        String classPath = generateAndSaveClass(1, "", "Main", classBody, false);

        RunResults results = core.run(
                new String[]{sourcesRoot},
                new String[]{classPath},
                new String[]{},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        assertTrue(results.getMethodResult().getSystemErr().contains("/ by zero"));
    }

    @Test
    public void testRunClassNonExistingClass() throws Exception {
        String classPath = sourcesRoot + File.separator + "_01_lesson";

        RunResults results = core.run(
                new String[]{sourcesRoot},
                new String[]{classPath},
                new String[]{},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        assertTrue(results.getGeneralResponse().getMessage().length() > 0);
    }

    @Test
    public void testRunClassWithMainWithDependencies() throws Exception {
        String classBody = insertInMain("CmdLineParser parser = new CmdLineParser(null);" +
                "\nSystem.out.println(parser);");

        copyTestPom();

        String classPath = generateAndSaveClass(1,
                "import org.kohsuke.args4j.CmdLineParser;",
                "Main",
                classBody,
                false);

        courseIOUtils.saveMavenDependenciesLocally(projectRoot);
        final String[] dependencies = courseIOUtils.copyDependencies(projectRoot);


        RunResults results = core.run(
                new String[]{sourcesRoot},
                new String[]{classPath},
                dependencies,
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        assertTrue(results.getMethodResult().getSystemOut().contains("CmdLineParser"));
    }

    @Test
    public void testRunTests() throws Exception {
        String test1 = "assertEquals(5, 5);";
        String test2 = "assertTrue(\"string\".equals(\"string\"));";
        String test3 = "assertEquals(5, 3);";

        String tests = insertInTests(test1, test2, test3);

        String imports = "import org.junit.Test;\n" +
                "import static org.junit.Assert.assertEquals;\n" +
                "import static org.junit.Assert.assertTrue;";

        String className = "Tests";
        copyTestPom();

        String classPath = generateAndSaveClass(1, imports, className, tests, true);

        courseIOUtils.saveMavenDependenciesLocally(projectRoot);
        courseIOUtils.copyDependencies(projectRoot);


        RunResults results = core.run(
                new String[]{sourcesTestRoot},
                new String[]{classPath},
                new String[]{"junit-4.12.jar", "args4j-2.33.jar"},
                PreProcessors.lessonsTests,
                MethodCheckers.testChecker,
                Runners.test,
                ResultsProcessors.main);

        assertEquals(3, results.getMethodStats().getOverallTests());
        assertEquals(2, results.getMethodStats().getPassedTests());
        assertEquals(1, results.getMethodStats().getFailedTests());
        assertTrue(results.getMethodStats().getFailures().size() > 0);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testRunNonExistingTests() throws Exception {
        String tests = "";

        String imports = "import org.junit.Test;\n" +
                "import static org.junit.Assert.assertEquals;\n" +
                "import static org.junit.Assert.assertTrue;";

        String className = "Tests";
        copyTestPom();

        String classPath = generateAndSaveClass(1, imports, className, tests, true);

        courseIOUtils.saveMavenDependenciesLocally(projectRoot);
        courseIOUtils.copyDependencies(projectRoot);

        RunResults results = core.run(
                new String[]{sourcesTestRoot},
                new String[]{classPath},
                new String[]{"junit-4.12.jar", "args4j-2.33.jar"},
                PreProcessors.lessonsTests,
                MethodCheckers.testChecker,
                Runners.test,
                ResultsProcessors.main);
    }


    private String generateAndSaveClass(int lessonNumber,
                                        String imports,
                                        String className,
                                        String classBody,
                                        boolean test) throws IOException {
        String classContent = String.format("package _0%d_lesson;" +
                        "%s" +
                        "public class %s{" +
                        "%s" +
                        "}",
                lessonNumber,
                imports,
                className,
                classBody);

        String fileName = String.format("%s/_0%d_lesson/%s.java",
                test ? sourcesTestRoot : sourcesRoot,
                lessonNumber,
                className)
                .replace("/", File.separator);

        File classFile = new File(fileName);

        Files.write(Paths.get(classFile.getAbsolutePath()), classContent.getBytes(), StandardOpenOption.CREATE);

        return classFile.getPath();
    }

    private void copyTestPom() throws IOException, URISyntaxException {
        Path sourcePomPath = Paths.get(Resources.getResource("run_core_tests" + File.separator + "testpom.xml").toURI());
        Path pomPath = Paths.get(projectRoot + File.separator + "pom.xml");

        Files.copy(sourcePomPath, pomPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private String insertInMain(String content) {
        return String.format("public static void main(String[] args){" +
                "%s}", content);
    }

    private String insertInTests(String test1, String test2, String test3) {
        return String.format(" @Test\n" +
                "    public void test1() {\n" +
                "        %s\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void test2(){\n" +
                "        %s\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void test3(){\n" +
                "        %s\n" +
                "    }", test1, test2, test3);
    }
}

