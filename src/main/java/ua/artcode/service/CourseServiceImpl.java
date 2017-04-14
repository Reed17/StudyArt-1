package ua.artcode.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.CourseDB;
import ua.artcode.exception.CourseDirectoryCreatingExcpetion;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.exception.NoSuchDirectoryException;
import ua.artcode.model.CheckResult;
import ua.artcode.model.Course;
import ua.artcode.model.GeneralResponse;
import ua.artcode.model.SolutionModel;
import ua.artcode.utils.CheckUtils;
import ua.artcode.utils.IOUtils;
import ua.artcode.utils.StringUtils;

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

    // todo think about static initialization
    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    @Autowired
    private CourseDB courseDB;

    @Override
    public boolean addCourseFromGit(Course course) throws CourseDirectoryCreatingExcpetion, GitAPIException {
        try {
            File courseDir = IOUtils.createCourseDirectory(course);
            course.setCourseLocalPath(courseDir.getAbsolutePath());

            Git.cloneRepository()
                    .setURI(course.getGitURL())
                    .setDirectory(courseDir)
                    .call();

            if (!IOUtils.checkDirectoryIsEmpty(courseDir)) {
                courseDB.add(course);
            } else {
                throw new CourseDirectoryCreatingExcpetion("Can't create directory for course");
            }

            return true;
        } catch (IOException e) {
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
    public CheckResult runClass(String packageName, String className, int courseId)
            throws NoSuchDirectoryException, CourseNotFoundException, ClassNotFoundException {
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
            File rootDirectoryPath = IOUtils.getRootDirectory(projectPath, packageName);
            // load all classes from root
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{rootDirectoryPath.toURI().toURL()});
            // run className and save results
            checkResult = CheckUtils.runCheckMethod(className, classLoader, packageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return checkResult;
    }

    @Override
    public CheckResult sendSolution(String packageName,
                                    String className,
                                    int courseId,
                                    SolutionModel solution)
            throws NoSuchDirectoryException, ClassNotFoundException, CourseNotFoundException {

        // replace class body with <solution> (append), then call runClass() for it
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
                    .orElseThrow(() -> new ClassNotFoundException("No class with name: " + className));

            // save original content of class
            sourceClassContentOriginal = Files.readAllLines(sourceClassPath)
                    .stream()
                    .collect(Collectors.joining());

            // append our solution at the end of sourceClassContentOriginal string
            String sourceClassContentWithSolution = StringUtils.addSoulution(sourceClassContentOriginal, solution);

            // write sourceClassContentWithSolution to source class
            Files.write(sourceClassPath, sourceClassContentWithSolution.getBytes(), StandardOpenOption.CREATE);

            // run task
            checkResult = runClass(packageName, className, courseId);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sourceClassContentOriginal != null) {
                try (PrintWriter pw = new PrintWriter(new File(sourceClassPath.toString()))) {
                    // reset changes - to original state
                    IOUtils.writeToFile(pw, sourceClassPath, sourceClassContentOriginal);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return checkResult;
    }
}
