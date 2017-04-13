package ua.artcode.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.CourseDB;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.CheckResult;
import ua.artcode.model.Course;
import ua.artcode.model.GeneralResponse;
import ua.artcode.model.SolutionModel;
import ua.artcode.utils.IO_utils.IOUtils;
import ua.artcode.utils.check_utils.CheckUtils;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by v21k on 09.04.17.
 */

@Service
public class CourseServiceImpl implements CourseService {

    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();
    private static final String STANDARD_ROOT_PACKAGE = "src";

    @Autowired
    private CourseDB courseDB;

    @Override
    public boolean addCourse(Course course) {
        try {
            File courseDir = IOUtils.createCourseDirectory(course);
            course.setCourseLocalPath(courseDir.getAbsolutePath());

            Git git = Git.cloneRepository()
                    .setURI(course.getGitURL())
                    .setDirectory(courseDir)
                    .call();

            courseDB.add(course);

            return true;
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Course getCourse(int id) {
        Course course = null;
        try {
            course = courseDB.getCourseByID(id);
        } catch (CourseNotFoundException e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public Collection<Course> getAllCourses() {
        return courseDB.getAllCourses();
    }

    @Override
    public CheckResult runTask(String className, int courseId) {
        String projectPath;
        CheckResult checkResult = new CheckResult(GeneralResponse.FAILED);
        try {
            // getting path for course
            projectPath = courseDB.getCoursePath(courseDB.getCourseByID(courseId));
            // get all .java files
            String[] sourceJavaFilesPaths = IOUtils.getSourceJavaFilesPaths(projectPath);
            // compile
            COMPILER.run(null, null, null, sourceJavaFilesPaths);
            // get root directory (src)
            File rootDirectoryPath = IOUtils.getRootDirectory(projectPath, STANDARD_ROOT_PACKAGE);
            // load all classes from root
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{rootDirectoryPath.toURI().toURL()});
            // run className and save results
            checkResult = CheckUtils.runCheckMethod(className, classLoader);
        } catch (IOException | CourseNotFoundException e) {
            e.printStackTrace();
        }
        return checkResult;
    }

    @Override
    public CheckResult checkSolution(String className, int courseId, SolutionModel solution) {

        // replace class body with <solution> (append), then call runTask() for it
        // after this - reset changes (get back to original state)

        // get path for project
        String projectPath;
        String sourceClassContentOriginal = null;
        Path sourceClassPath = null;
        CheckResult checkResult = new CheckResult(GeneralResponse.FAILED);
        try {
            projectPath = courseDB.getCoursePath(courseDB.getCourseByID(courseId));
            // path for source class with tests (which we have to run)
            sourceClassPath = Files.walk(Paths.get(projectPath))
                    .filter(path -> path.toString().contains(className + ".java"))
                    .findFirst()
                    .get();

            // save original content of class
            sourceClassContentOriginal = Files.readAllLines(sourceClassPath)
                    .stream()
                    .collect(Collectors.joining());

            // append our solution at the end of sourceClassContentOriginal string
            String sourceClassContentWithSolution =
                    sourceClassContentOriginal.substring(0, sourceClassContentOriginal.lastIndexOf("}"))
                            + solution.getSolution() + "}";

            // write sourceClassContentWithSolution to source class
            Files.write(sourceClassPath, sourceClassContentWithSolution.getBytes(), StandardOpenOption.CREATE);

            // run task
            checkResult = runTask(className, courseId);

        } catch (IOException | CourseNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (sourceClassContentOriginal != null) {
                try (PrintWriter pw = new PrintWriter(new File(sourceClassPath.toString()))) {
                    // reset changes - to original state
                    // write empty string to file
                    pw.write("");
                    pw.flush();
                    // write original content
                    Files.write(sourceClassPath, sourceClassContentOriginal.getBytes(), StandardOpenOption.CREATE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return checkResult;
    }
}
