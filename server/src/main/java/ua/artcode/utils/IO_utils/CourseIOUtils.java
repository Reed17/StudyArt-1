package ua.artcode.utils.IO_utils;

import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.LessonClassPathsException;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;
import ua.artcode.utils.AppPropertyHolder;
import ua.artcode.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by v21k on 20.04.17.
 */
@Component
public class CourseIOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseIOUtils.class);
    private final CommonIOUtils commonIOUtils;
    private final InvocationRequest request;
    private final Invoker invoker;
    private final AppPropertyHolder.Courses.Paths paths;
    private final AppPropertyHolder.Maven maven;

    @Autowired
    public CourseIOUtils(CommonIOUtils commonIOUtils,
                         InvocationRequest invocationRequest,
                         Invoker invoker,
                         AppPropertyHolder appPropertyHolder) {

        this.commonIOUtils = commonIOUtils;
        this.request = invocationRequest;
        this.invoker = invoker;
        this.paths = appPropertyHolder.getCourses().getPaths();
        this.maven = appPropertyHolder.getMaven();
    }

    /**
     * Downloading project from Git and save it locally
     * <p>
     * 1.Create directory for project.
     * 2.Clone git project to dir.
     * 3.Return path.
     *
     * @return path where project has been saved
     */

    // todo save in directory from app.properties
    public String saveCourseLocally(String courseURL,
                                    String courseName,
                                    int courseID,
                                    int userID) throws DirectoryCreatingException, GitAPIException {

        String projectPath = generatePath(courseName, courseID, userID);
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

    public String saveCourseLocally(String courseURL,
                                    String courseName,
                                    int courseID) throws DirectoryCreatingException, GitAPIException {

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
        Path classPathDirectory = Paths.get(paths.getExternalCode());

        Files.createDirectories(classPathDirectory);
        FileUtils.cleanDirectory(new File(paths.getExternalCode()));

        String javaClassName = className + ".java";
        File sourceFile = new File(paths.getExternalCode(), javaClassName);

        Files.write(sourceFile.toPath(), code.getBytes(), StandardOpenOption.CREATE);

        return paths.getExternalCode() + File.separator + javaClassName;
    }


    public AbstractMap.SimpleEntry<List<String>, String> ensureLessonClassPathsAndRoot(List<String> classPaths,
                                                                                       String sourceRoot,
                                                                                       String courseRoot) throws IOException, LessonClassPathsException {

        if (sourceRoot == null) {
            if (classPaths == null || classPaths.size() == 0) {
                throw new LessonClassPathsException("No classes in lesson or not valid class path");
            } else {
                // todo delimiter must be not hardcoded - we can have another project structures, no only maven. Also /java is not enough
                // todo because user can have his custom package with name 'java'. In this case everything will crash :)
                classPaths = classPaths.stream()
                        .map(path -> path.startsWith(courseRoot) ? path : StringUtils.normalizePath(courseRoot + path))
                        .collect(Collectors.toList());

                sourceRoot = StringUtils.getClassRootFromClassPath(classPaths.get(0).replace("/", File.separator),
                        "java" + File.separator);
            }
        } else {
            if (classPaths == null || classPaths.size() == 0) {
                classPaths = Arrays.asList(commonIOUtils.parseFilePaths(sourceRoot.replace("/", File.separator),
                        "java"));
            }
        }

        String finalSourceRoot = sourceRoot;

        classPaths = classPaths.stream().
                map(path -> path.replace("/", File.separator))
                .map(path -> finalSourceRoot + File.separator + path)
                .collect(Collectors.toList());

        return new AbstractMap.SimpleEntry<>(classPaths, sourceRoot.replace("/", File.separator));
    }

    public String[] getLessonClassAndTestsPaths
            (List<String> lessonSourceClasses, List<String> lessonTestsClasses) throws
            IOException {
        lessonSourceClasses.addAll(lessonTestsClasses);
        return lessonSourceClasses.stream()
                .filter(lesson -> lesson.endsWith(".java"))
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
        projectRoot = Paths.get(projectRoot).toAbsolutePath().toString();

        String pomPath = generatePomPath(projectRoot);
        String mavenGoal = generateMavenGoal(projectRoot);

        request.setPomFile(new File(pomPath));
        request.setGoals(Collections.singletonList(mavenGoal));

        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Create folder where all dependencies will be located (if doesn't exist),
     * then copy certain course dependencies to it.
     * Existing dependencies will not be copied.
     *
     * @return array with dependencies names (.jar) which will be saved in Course
     */
    public String[] copyDependencies(String projectPath) throws IOException, DirectoryCreatingException {
        File globalDep = new File(paths.getDependencies());
        if (!globalDep.exists() && !globalDep.mkdir()) {
            throw new DirectoryCreatingException("Can't create directory for dependencies");
        }

        File[] allDependencies = globalDep.listFiles();

        // get all .jar files
        String[] jarfiles = Files.walk(Paths.get(projectPath))
                .map(Path::toString)
                .filter(path -> path.endsWith(".jar"))
                .toArray(String[]::new);

        // copy .jars to global dependencies directory (filter before, if we already have them)
        Arrays.stream(jarfiles)
                .filter(path -> {
                    String jarName = StringUtils.substringAfterDelimiter(path, File.separator);
                    return allDependencies == null || Arrays.stream(allDependencies)
                            .noneMatch(dep -> dep.getAbsolutePath()
                                    .contains(jarName));
                })
                .forEach(path -> {
                            try {
                                Files.copy(
                                        Paths.get(path),
                                        Paths.get(globalDep.getAbsolutePath() +
                                                File.separator +
                                                StringUtils.substringAfterDelimiter(path, File.separator) +
                                                ".jar"),
                                        StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                LOGGER.error("Copying dependencies - FAILED.", e);
                            }
                        }
                );

        // return array (dependencyName.jar)
        return Arrays.stream(jarfiles)
                .map(path -> StringUtils.substringAfterDelimiter(path, File.separator) + ".jar")
                .toArray(String[]::new);
    }

    public String getValidPathForUsersCourse(String pathToClass, String userSource) {
        String separator = File.separator.equals("\\") ? "\\\\" : File.separator;
        String[] sourceSplitted = userSource.split(separator);
        String[] pathSplitted = pathToClass.split(separator);


        pathSplitted[sourceSplitted.length - 1] = sourceSplitted[sourceSplitted.length - 1];

        return String.join(File.separator, pathSplitted);
    }

    private String generatePomPath(String projectRoot) {
        projectRoot = checkEndsWithSeparator(projectRoot);
        return projectRoot + "pom.xml";

    }

    private String generateMavenGoal(String projectRoot) {
        projectRoot = checkEndsWithSeparator(projectRoot);
        return maven.getGoals().getCopyToDirectory()
                + '"' + projectRoot
                + maven.getDependenciesPath() + '"';
    }

    private String checkEndsWithSeparator(String path) {
        return path.endsWith(File.separator) ? path : path + File.separator;
    }

    private String generatePath(String courseName, int courseID, int userID) {
        return paths.getGit() + File.separator + userID + courseID + courseName + File.separator;
    }

    private String generatePath(String courseName, int courseID) {
        return paths.getGit() + File.separator + courseID + courseName + File.separator;
    }
}
