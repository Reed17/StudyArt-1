package ua.artcode.utils;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.artcode.model.Course;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

/**
 * Created by v21k on 10.04.17.
 */
@Component
public class IOUtils {

    private static String pathForGitProjects;

    public static File createCourseDirectory(Course course) throws IOException {

        File courseDir = new File(generatePath(course));
        courseDir.mkdirs(); // todo do not ignore of the result of method
        FileUtils.cleanDirectory(courseDir);

        return courseDir;
    }

    public static boolean checkDirectoryIsEmpty(File directory) {
        File[] files = directory.listFiles();
        return (files != null && files.length == 0);
    }

    /**
     * Clears file's original content and writes input content into it
     */
    public static void writeToFile(Writer writer, Path path, String content) throws IOException {
        writer.write("");
        writer.flush();
        Files.write(path, content.getBytes(), StandardOpenOption.CREATE);
    }

    /**
     * Getting all .java files as String[]
     */
    public static String[] getSourceJavaFilesPaths(String projectPath) throws IOException {
        return Files.walk(Paths.get(projectPath))
                .filter(path -> path.toString().contains(".java"))
                .map(Path::toString)
                .toArray(String[]::new);
    }

    public static File getRootDirectory(String projectPath, String packageName) throws IOException {
        String root = Files.walk(Paths.get(projectPath))
                .filter(path -> path.toString().endsWith(packageName))
                .map(Path::toString)
                .collect(Collectors.joining());
        return new File(root);
    }

    private static String generatePath(Course course) {
        return pathForGitProjects + "/" + course.getAuthor() + "/";
    }

    @Value("${pathForGitProjects}")
    public void setPathForGitProjects(String path) {
        pathForGitProjects = path;
    }
}
