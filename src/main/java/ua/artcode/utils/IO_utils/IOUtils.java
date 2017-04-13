package ua.artcode.utils.IO_utils;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Component;
import ua.artcode.model.Course;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Created by v21k on 10.04.17.
 */
@Component
public class IOUtils {

    private static final String PATH_FOR_GIT_PROJECTS =
            "/home/v21k/Java/dev/StudyArtNew/src/main/resources/courses";

    public static File createCourseDirectory(Course course) throws IOException {

        File courseDir = new File(generatePath(course));
        courseDir.mkdir();
        FileUtils.cleanDirectory(courseDir);

        return courseDir;
    }

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
        return PATH_FOR_GIT_PROJECTS + "/" + course.getAuthor() + "/";
    }
}
