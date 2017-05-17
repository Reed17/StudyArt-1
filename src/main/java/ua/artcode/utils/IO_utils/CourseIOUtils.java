package ua.artcode.utils.IO_utils;

import javafx.util.Pair;
import org.apache.maven.shared.invoker.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.exceptions.*;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;
import ua.artcode.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by v21k on 20.04.17.
 */
@Component
public class CourseIOUtils {

    @Value("${application.courses.paths.git}")
    private String localPathForProjects;
    @Value("${application.courses.paths.externalCode}")
    private String localPathForExternalCode;
    @Value("${maven.dependenciesPath}")
    private String mvnDependencyDownloadPath;
    @Value("${maven.goals.copyToDirectory}")
    private String mvnCopyToDirectory;
    @Value("${maven.home}")
    private String mvnHome;

    @Autowired
    private CommonIOUtils commonIOUtils;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Downloading project from Git and save it locally
     * <p>
     * 1.Create directory for project.
     * 2.Clone git project to dir.
     * 3.Return path.
     *
     * @return path where project has been saved
     */

    public String saveCourseLocally(String courseURL, String courseName, int courseID) throws DirectoryCreatingException, GitAPIException {
        String projectPath = generatePath(courseName, courseID);
        File projectDirectory = new File(projectPath);
        try {
            if (projectDirectory.exists()) {
                FileUtils.cleanDirectory(new File(projectPath));
            }
            Files.createDirectories(Paths.get(projectPath));
            Git.cloneRepository()
                    .setURI(courseURL)
                    .setDirectory(projectDirectory)
                    .call()
                    .getRepository()
                    .close();

        } catch (IOException e) {
            throw new DirectoryCreatingException("Unable to create a directory for course: " + courseName);
        }
        return projectPath;
    }

    /**
     * Parse lessons from project folder and save them in corresponding course field (as List)
     * <p>
     * 1.Walk through project directory and look for folders containsCourse "lesson"
     * 3.Generate lessons from results (parse name and all .java files)
     * 2.Save lessons as list
     *
     * @return List of lessons
     **/
    @Deprecated
    public List<Lesson> getLessons(Course course) throws IOException {
        String courseLocalPath = course.getLocalPath();

        return Files.walk(Paths.get(courseLocalPath))
                .map(Path::toString)
                .filter(path -> path.endsWith("lesson"))
                .map(path -> {
                    String lessonName = path.substring(path.lastIndexOf(File.separator) + 1);
                    return new Lesson(lessonName, path);
                })
                .sorted()
                .collect(Collectors.toList());
    }

    public Lesson getLessonByID(String courseLocalPath, int id) throws IOException {

        return Files.walk(Paths.get(courseLocalPath))
                .map(Path::toString)
                .filter(path -> path.endsWith("lesson"))
                .filter(path -> path.contains(String.valueOf(id)))
                .map(path -> {
                    String lessonName = path.substring(path.lastIndexOf(File.separator) + 1);
                    return new Lesson(lessonName, path);
                })
                .findFirst()
                .get();

    }

    /**
     * Save code locally
     * 1. Parse className from code (3rd word - public class NAME {...}
     * 2. Create directories or delete everything from them if there are already something
     * 3. Write code into .java file
     *
     * @return path for created .java file as String
     */
    public String saveExternalCodeLocally(String code) throws IOException {
        String className = StringUtils.parseString(code, "class", "{").trim();
        Path classPathDirectory = Paths.get(localPathForExternalCode);

        Files.createDirectories(classPathDirectory);
        FileUtils.cleanDirectory(new File(localPathForExternalCode));

        String javaClassName = className + ".java";
        File sourceFile = new File(localPathForExternalCode, javaClassName);

        Files.write(sourceFile.toPath(), code.getBytes(), StandardOpenOption.CREATE);

        return localPathForExternalCode + File.separator + javaClassName;
    }


    public Pair<List<String>, String> ensureLessonClassPathsAndRoot(List<String> classPaths, String sourceRoot) throws IOException, LessonClassPathsException {

        if (sourceRoot == null) {
            if (classPaths == null || classPaths.size() == 0) {
                throw new LessonClassPathsException("No classes in lesson or not valid class path");
            } else {
                sourceRoot = StringUtils.getClassRootFromClassPath(classPaths.get(0).replace("/",File.separator), "java" + File.separator);
            }
        } else {
            if (classPaths == null || classPaths.size() == 0) {
                try {
                    classPaths = Arrays.asList(commonIOUtils.parseFilePaths(sourceRoot.replace("/",File.separator), "java"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        classPaths = classPaths.stream().map(path -> path.replace("/",File.separator)).collect(Collectors.toList());

        return new Pair(classPaths,sourceRoot.replace("/",File.separator));
    }

    public String[] getLessonClassPaths(String path, String endsWith) throws IOException {
        return commonIOUtils.parseFilePaths(path, "java");
    }

    public String[] getLessonClassAndTestsPaths
            (List<String> lessonSourceClasses, List<String> lessonTestsClasses, List<String> lessonRequiredClasses) throws
            IOException {
        lessonSourceClasses.addAll(lessonTestsClasses);
        lessonSourceClasses.addAll(lessonRequiredClasses);
        return lessonSourceClasses.stream()
                .toArray(String[]::new);
    }

    /**
     * Saving locally all dependencies from pom.xml locally
     * Uses 3 values from application.properties:
     * 1. maven.home
     * 2. maven.dependenciesPath - where files will be saved
     * 3. maven.goals.copyToDirectory - maven goal (copy and save files locally)
     * So you need to specify these 3 values before using application.
     *
     * @param projectRoot root folder for project (not src/ or java/, just regular project folder
     * @return true if saved successfully, false otherwise
     */

    public boolean saveMavenDependenciesLocally(String projectRoot) {
        // todo declare beans for both
        InvocationRequest request = new DefaultInvocationRequest();
        Invoker invoker = new DefaultInvoker();

        projectRoot = Paths.get(projectRoot).toAbsolutePath().toString();

        String pomPath = generatePomPath(projectRoot);
        String mavenGoal = generateMavenGoal(projectRoot);

        request.setPomFile(new File(pomPath));
        request.setGoals(Collections.singletonList(mavenGoal));
//        invoker.setMavenHome(new File(mvnHome));

        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private String generatePomPath(String projectRoot) {
        projectRoot = checkEndsWithSeparator(projectRoot);
        return projectRoot + "pom.xml";

    }

    private String generateMavenGoal(String projectRoot) {
        projectRoot = checkEndsWithSeparator(projectRoot);
        return mvnCopyToDirectory
                + '"' + projectRoot
                + mvnDependencyDownloadPath + '"';
    }

    private String checkEndsWithSeparator(String path) {
        return path.endsWith(File.separator) ? path : path + File.separator;
    }

    private String generatePath(String courseName, int courceID) {
        return localPathForProjects + File.separator + courceID + courseName + File.separator;
    }
}
