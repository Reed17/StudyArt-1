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
        String classContent = "package _01_lesson;" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Some text\");\n" +
                "    }\n" +
                "}";

        File classFile = generateJavaFile(1, "Main");
        Files.write(Paths.get(classFile.getAbsolutePath()), classContent.getBytes(), StandardOpenOption.CREATE);

        RunResults results = core.runMethod(projectRoot,
                sourcesRoot,
                new String[]{classFile.getAbsolutePath()},
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        assertEquals("Some text\n", results.getMethodResult().getSystemOut());
    }

    private File generateJavaFile(int lessonNumber, String className) {
        String fileName = String.format("%s/_0%d_lesson/%s.java",
                sourcesRoot,
                lessonNumber,
                className)
                .replace("/", File.separator);
        return new File(fileName);
    }
}

