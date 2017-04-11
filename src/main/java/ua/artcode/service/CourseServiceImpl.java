package ua.artcode.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by v21k on 09.04.17.
 */

@Service
@Component
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
            throw new CourseNotFoundException(String.format("Coruse with id %d not found", id));
        }
    }

    @Override
    public boolean runTask(String mainClass, int courseId) {
        String projectPath = courseDB.getCoursePath(courseDB.get(courseId));

        try {
            // compiling all .java files
            Files.walk(Paths.get(projectPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().contains(".java"))
                    .forEach(path -> {
                        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                        compiler.run(null, null, null, path.toAbsolutePath().toString());
                    });

            // run .class file with <mainClass> name
            Files.walk(Paths.get(projectPath))
                    .filter(path -> path.toString().contains(mainClass + ".class"))
                    .limit(1)
                    .forEach((Path path) -> {
                        try {
                            File root = new File(path.getParent().toString());
                            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});

                            Class<?> cls = Class.forName(mainClass, true, classLoader);
                            Method main = cls.getMethod("main", String[].class);
                            String[] arguments = null;
                            main.invoke(null, (Object) arguments);
                        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}
