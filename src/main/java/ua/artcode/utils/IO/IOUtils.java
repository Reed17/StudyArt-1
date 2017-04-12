package ua.artcode.utils.IO;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Component;
import ua.artcode.model.Course;

import java.io.File;
import java.io.IOException;

/**
 * Created by v21k on 10.04.17.
 */
@Component
public class IOUtils {

    private static final String PATH_FOR_GIT_PROJECTS =
            "/home/v21k/Java/dev/StudyArtNew/src/main/resources/courses";

    public static File createDir(Course course) throws IOException {

        File courseDir = new File(generatePath(course));
        courseDir.mkdir();
        FileUtils.cleanDirectory(courseDir);

        return courseDir;
    }

    private static String generatePath(Course course) {
        return PATH_FOR_GIT_PROJECTS + "/" + course.getAuthor() + "/";
    }
}
