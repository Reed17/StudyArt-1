package ua.artcode.utils;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by v21k on 15.04.17.
 */
@Component
public class IOUtils {

    private static String localPathForProjects;
    private static String localPathForExternalCode;

    /**
     * Downloading project from Git and save it locally
     * <p>
     * 1.Create directory for project.
     * 2.Clone git project to dir.
     * 3.Return path.
     *
     * @return path where project has been saved
     */
    public static String saveLocally(Course course) throws DirectoryCreatingException, GitAPIException {
        String projectPath = generatePath(course);
        File projectDirectory = new File(projectPath);
        try {
            if (projectDirectory.exists()) {
                FileUtils.cleanDirectory(new File(projectPath));
            }
            Files.createDirectories(Paths.get(projectPath));
            Git.cloneRepository()
                    .setURI(course.getUrl())
                    .setDirectory(projectDirectory)
                    .call();
        } catch (IOException e) {
            throw new DirectoryCreatingException("Unable to create a directory for course: " + course.getName());
        }
        return projectPath;
    }

    /**
     * Parse lessons from project folder and save them in corresponding course field (as List)
     * <p>
     * 1.Walk through project directory and look for folders contains "lesson"
     * 3.Generate lessons from results (parse name and all .java files)
     * 2.Save lessons as list
     *
     * @return List of lessons
     **/
    public static List<Lesson> getLessons(Course course) throws IOException {
        String courseLocalPath = course.getLocalPath();

        return Files.walk(Paths.get(courseLocalPath))
                .map(Path::toString)
                .filter(path -> path.endsWith("lesson"))
                .map(path -> {
                    String lessonName = path.substring(path.lastIndexOf("/") + 1);
                    List<String> classPaths = null;
                    try {
                        classPaths = Files.walk(Paths.get(path))
                                .map(Path::toString)
                                .sorted(String::compareTo)
                                .filter(classPath -> classPath.endsWith(".java"))
                                .collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new Lesson(lessonName, path, classPaths);
                })
                .sorted()
                .collect(Collectors.toList());
    }

    public static String saveExternalCodeLocally(String code) throws IOException {
        String className = code.split(" ")[2];
        Path classPathDirectory = Paths.get(localPathForExternalCode);

        Files.createDirectories(classPathDirectory);
        FileUtils.cleanDirectory(new File(localPathForExternalCode));

        String javaClassName = className + ".java";
        File sourceFile = new File(localPathForExternalCode, javaClassName);

        Files.write(sourceFile.toPath(), code.getBytes(), StandardOpenOption.CREATE);

        return localPathForExternalCode + "/" + javaClassName;
    }


    private static String generatePath(Course course) {
        return localPathForProjects + "/" + course.getId() + course.getName() + "/";
    }

    @Value("${pathForGitProjects}")
    public void setLocalPathForProjects(String path) {
        localPathForProjects = path;
    }

    @Value("${pathForExternalCodeCompiling}")
    public void setLocalPathForExternalCode(String path) {
        localPathForExternalCode = path;
    }
}
