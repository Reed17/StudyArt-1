package ua.artcode.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.CourseDB;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.Course;
import ua.artcode.utils.IO.IOUtils;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Created by v21k on 09.04.17.
 */

@Service
public class CourseServiceImpl implements CourseService {

    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    @Autowired
    private CourseDB courseDB;

    @Override
    public boolean addCourse(Course course) throws IOException {

        File courseDir = IOUtils.createDir(course);

        try {
            Git git = Git.cloneRepository()
                    .setURI(course.getGitURL())
                    .setDirectory(courseDir)
                    .call();
            courseDB.add(course, courseDir.getPath());
            return true;
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Course getCourse(int id) throws CourseNotFoundException {
        Course course = courseDB.get(id);

        if (course != null) {
            return course;
        } else {
            throw new CourseNotFoundException(String.format("Course with id %d not found", id));
        }
    }

    @Override
    public boolean runTask(String mainClass, int courseId) {
        String projectPath = courseDB.getCoursePath(courseDB.get(courseId));

        try {
            // get all .java files
            String[] sourceJavaFilesPaths = getSourceJavaFilesPaths(projectPath);

            // compile
            COMPILER.run(null, null, null, sourceJavaFilesPaths);

            // get root directory (src)
            File rootDirectoryPath = getRootDirectory(projectPath, "src");

            // load all classes from root
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{rootDirectoryPath.toURI().toURL()});

            // run mainClass
            runMain(mainClass, classLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void runMain(String mainClass, URLClassLoader classLoader) {
        Class<?> cls = null;
        String mainMethod = "main";
        String[] argumentsForMain = null;

        try {
            cls = Class.forName(mainClass, true, classLoader);
            Method clsMethod = cls.getMethod(mainMethod, String[].class);
            clsMethod.invoke(null, (Object) argumentsForMain);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private String[] getSourceJavaFilesPaths(String projectPath) throws IOException {
        return Files.walk(Paths.get(projectPath))
                .filter(path -> path.toString().contains(".java"))
                .map(Path::toString)
                .toArray(String[]::new);
    }

    private File getRootDirectory(String projectPath, String packageName) throws IOException {
        String root = Files.walk(Paths.get(projectPath))
                .filter(path -> path.toString().endsWith(packageName))
                .map(Path::toString)
                .collect(Collectors.joining());
        return new File(root);
    }

}
