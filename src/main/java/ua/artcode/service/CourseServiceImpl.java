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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
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


    /**
     * @param course - Course to add
     *               Course will automatically verified before passing as argument
     *               Algorithm:
     *               1. Create new directory for course
     *               2. Clone Git Repo to this dir
     *               3. Add course to DB if everything is OK
     * @return true if course downloaded and added, false otherwise
     * @throws CourseDirectoryCreatingExcpetion if unable to create a directory
     */
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

    /**
     * @param id - id for course
     * @return Course if found, null otherwise
     * @throws CourseNotFoundException*
     */
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

    /**
     * @return Collection containing all courses in DB
     */
    @Override
    public Collection<Course> getAllCourses() {
        return courseDB.getAllCourses();
    }


    /**
     * @param packageName - root directory where mainClass located (and other dependencies too)
     * @param className   - name of the class with tests
     * @param methodName  - name of the method to run in className.class
     * @param courseId    - id of the corresponding course
     *                    <p>
     *                    1. Getting path for course
     *                    2. Get all .java files in projectPath folder
     *                    3. Compile all .java files
     *                    4. Get root directory with mainClass (packageName)
     *                    5. Load this directory to ClassLoader
     *                    6. Generate CheckResults (METHOD SHOULD RETURN LIST OF STRING)
     * @return CheckResult model with all info and stats
     */
    @Override
    public CheckResult runClass(String packageName, String className, String methodName, int courseId)
            throws NoSuchDirectoryException, CourseNotFoundException, ClassNotFoundException {
        String projectPath;
        CheckResult checkResult = new CheckResult(new GeneralResponse("FAILED"));

        try {
            // getting path for course
            projectPath = courseDB.getCoursePath(courseDB.getCourseByID(courseId));
            // get all .java files
            String[] sourceJavaFilesPaths = IOUtils.getSourceJavaFilesPaths(projectPath);
            // OS to redirect compilation errors
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // compile
            COMPILER.run(null, null, baos, sourceJavaFilesPaths);
            // save compilation results
            String compilationResult = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            // if not empty - return CheckResult with error message
            if(compilationResult.trim().length()>0){
                return new CheckResult(new GeneralResponse(compilationResult));
            }
            // get root directory (src)
            File rootDirectoryPath = IOUtils.getRootDirectory(projectPath, packageName);
            // load all classes from root
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{rootDirectoryPath.toURI().toURL()});
            // run className and save results
            checkResult = CheckUtils.runCheckMethod(className, classLoader, methodName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return checkResult;
    }

    /**
     * Same to runClass() method, but here we append solution to class at first,
     * then run this class and collect
     * CheckResult
     * At the end we reset all changes in original class
     *
     * @see CourseServiceImpl#runClass(String, String, String, int)
     */
    @Override
    public CheckResult sendSolution(String packageName,
                                    String className,
                                    String methodName,
                                    int courseId,
                                    SolutionModel solution)
            throws NoSuchDirectoryException, ClassNotFoundException, CourseNotFoundException {

        String projectPath;
        String sourceClassContentOriginal = null;
        Path sourceClassPath = null;
        CheckResult checkResult = new CheckResult(new GeneralResponse("FAILED"));
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
            String sourceClassContentWithSolution = StringUtils.addSolution(sourceClassContentOriginal, solution);

            // write sourceClassContentWithSolution to source class
            Files.write(sourceClassPath, sourceClassContentWithSolution.getBytes(), StandardOpenOption.CREATE);

            // run task
            checkResult = runClass(packageName, className, methodName, courseId);

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
