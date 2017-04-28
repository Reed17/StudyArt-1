package ua.artcode.core;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.artcode.core.method_checker.MethodCheckers;
import ua.artcode.core.method_runner.Runners;
import ua.artcode.core.post_processor.ResultsProcessors;
import ua.artcode.core.pre_processor.PreProcessors;
import ua.artcode.dao.CourseDB;
import ua.artcode.model.response.RunResults;
import ua.artcode.utils.IO_utils.CommonIOUtils;
import ua.artcode.utils.IO_utils.CourseIOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by v21k on 27.04.17.
 */
@RunWith(SpringRunner.class)
// todo ask Serhii how to simplify
@SpringBootTest(classes = {RunCore.class, CourseIOUtils.class, CommonIOUtils.class, CourseDB.class})
public class RunCoreTest {

    @Value("${coreTestDir}")
    private String projectRoot;
    private String sourcesRoot;

    @Autowired
    RunCore core;

    @Before
    public void setUp() throws Exception {
        List<String> directoryTree = new ArrayList<>();
        directoryTree.add(projectRoot + "/src/main/java");
        directoryTree.add(projectRoot + "/src/test/resources");
        directoryTree.add(projectRoot + "/src/test/resources");
        directoryTree.add(projectRoot + "/src/main/resources");
        directoryTree.add(projectRoot + "/src/main/java/_01_lesson");
        directoryTree.add(projectRoot + "/src/main/java/_02_lesson");
        directoryTree.add(projectRoot + "/src/main/java/_03_lesson");

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
        String mainMethodBody = "System.out.println(\"Some text\");";
        String classPath = generateAndSaveClassWithMainMethod(1, "Main", mainMethodBody);

        RunResults results = core.runMethod(projectRoot,
                sourcesRoot,
                new String[]{classPath},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        assertEquals("Some text\n", results.getMethodResult().getSystemOut());
    }

    @Test
    public void testRunClassWithMainCompilationError() throws Exception {
        String mainMethodBody = "System.out.println(\"Some text\";";
        String classPath = generateAndSaveClassWithMainMethod(1, "Main", mainMethodBody);

        RunResults results = core.runMethod(projectRoot,
                sourcesRoot,
                new String[]{classPath},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        assertTrue(results.getGeneralResponse().getMessage().contains("error"));
    }

    @Test
    public void testRunClassWithMainRuntimeError() throws Exception {
        String mainMethodBody = "System.out.println(2/0);";
        String classPath = generateAndSaveClassWithMainMethod(1, "Main", mainMethodBody);

        RunResults results = core.runMethod(projectRoot,
                sourcesRoot,
                new String[]{classPath},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        assertTrue(results.getMethodResult().getSystemErr().contains("/ by zero"));
    }

    @Test
    public void testRunClassWithMainNoClass() throws Exception {
        String classPath = sourcesRoot + File.separator + "_01_lesson";

        RunResults results = core.runMethod(projectRoot,
                sourcesRoot,
                new String[]{classPath},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        System.out.println(results.getGeneralResponse().getMessage());
        assertTrue(results.getGeneralResponse().getMessage().length() > 0);
    }


    private String generateAndSaveClassWithMainMethod(int lessonNumber, String className, String mainMethodBody) throws IOException {
        String classContent = String.format("package _0%d_lesson;" +
                        "public class %s{" +
                        "public static void main(String[] args){" +
                        "%s}" +
                        "}",
                lessonNumber,
                className,
                mainMethodBody);

        String fileName = String.format("%s/_0%d_lesson/%s.java",
                sourcesRoot,
                lessonNumber,
                className)
                .replace("/", File.separator);

        File classFile = new File(fileName);

        Files.write(Paths.get(classFile.getAbsolutePath()), classContent.getBytes(), StandardOpenOption.CREATE);

        return classFile.getPath();
    }
}

