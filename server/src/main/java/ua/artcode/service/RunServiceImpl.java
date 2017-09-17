package ua.artcode.service;

import com.sun.xml.internal.bind.v2.TODO;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.core.RunCore;
import ua.artcode.core.method_checker.MethodCheckers;
import ua.artcode.core.method_runner.Runners;
import ua.artcode.core.post_processor.ResultsProcessors;
import ua.artcode.core.pre_processor.PreProcessors;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.dao.repositories.LessonRepository;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.Lesson;
import ua.artcode.model.Student;
import ua.artcode.model.response.RunResults;
import ua.artcode.utils.AppPropertyHolder;
import ua.artcode.utils.IO_utils.CourseIOUtils;
import ua.artcode.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class RunServiceImpl implements RunService {

    private final CourseRepository courseDB;
    private final LessonRepository lessonDB;
    private final StudentRepository studentDB;
    private final CourseIOUtils courseIOUtils;
    private final RunCore runCore;
    private final AppPropertyHolder.Courses.Paths paths;

    @Autowired
    public RunServiceImpl(CourseRepository courseDB, LessonRepository lessonDB,
                          CourseIOUtils courseIOUtils, RunCore runCore,
                          AppPropertyHolder appPropertyHolder, StudentRepository studentDB) {

        this.courseDB = courseDB;
        this.lessonDB = lessonDB;
        this.courseIOUtils = courseIOUtils;
        this.runCore = runCore;
        this.paths = appPropertyHolder.getCourses().getPaths();
        this.studentDB = studentDB;
    }

    @Override
    public RunResults runMain(ExternalCode code) throws Exception {
        String path = courseIOUtils.saveExternalCodeLocally(code.getSourceCode());

        String sourcesRoot = StringUtils.getClassRootFromClassPath(path, File.separator);

        RunResults runResults = runCore.run(
                new String[]{sourcesRoot},
                new String[]{path},
                new String[]{},
                PreProcessors.singleClass,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        FileUtils.deleteDirectory(new File(paths.getExternalCode()));

        return runResults;
    }


    @Override
    public RunResults runLessonWithSolutionTests(int lessonID, String url) throws Exception {

        Lesson lesson = Objects.requireNonNull(lessonDB.findOne(lessonID));
        Course course = Objects.requireNonNull(courseDB.findOne(lesson.getCourseID()));

        String projectLocalPath = courseIOUtils.saveCourseLocally(url, course.getName(), course.getId());

        String[] classPaths =
                courseIOUtils.getLessonClassAndTestsPaths(
                        lesson.getBaseClasses(),
                        lesson.getTestsClasses());

        String sourcesRoot = course.getSourcesRoot();
        String testsRoot = course.getTestsRoot();

        RunResults results = runCore.run(
                new String[]{sourcesRoot,
                        testsRoot},
                classPaths,
                course.getDependencies(),
                PreProcessors.lessonsTests,
                MethodCheckers.testChecker,
                Runners.test,
                ResultsProcessors.main);

        FileUtils.deleteDirectory(new File(projectLocalPath));

        return results;
    }

    @Override
    public RunResults runBaseLessonWithSolutionTests(int lessonId, String classText, Integer userId)
            throws IOException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException {

        Lesson lesson = Objects.requireNonNull(lessonDB.findOne(lessonId));
        Course course = Objects.requireNonNull(courseDB.findOne(lesson.getCourseID()));
        Student student = Objects.requireNonNull(studentDB.findOne(userId));

        Files.write(
                Paths.get(
                        courseIOUtils.getValidPathForUsersCourse(lesson.getBaseClasses().get(0),
                                student.getUserCourseCopies().get(lesson.getCourseID()).getPath())),
                Arrays.asList(classText),
                Charset.forName("UTF-8"));

        List<String> baseClasses = new ArrayList<>();
        List<String> testClasses = new ArrayList<>();

        // TODO: 16.09.17 check this

        String studentSource = student.getUserCourseCopies().get(lesson.getCourseID()).getPath();

        lesson.getBaseClasses().forEach(c -> baseClasses.add(courseIOUtils.getValidPathForUsersCourse(c, studentSource)));

        lesson.getTestsClasses().forEach(c -> testClasses.add(courseIOUtils.getValidPathForUsersCourse(c, studentSource)));

        String[] classPaths = courseIOUtils.getLessonClassAndTestsPaths(baseClasses, testClasses);

        String sourcesRoot = courseIOUtils.getValidPathForUsersCourse(course.getSourcesRoot(), studentSource);
        String testsRoot = courseIOUtils.getValidPathForUsersCourse(course.getTestsRoot(), studentSource);

        return runCore.run(
                new String[]{sourcesRoot,
                        testsRoot},
                classPaths,
                course.getDependencies(),
                PreProcessors.lessonsTests,
                MethodCheckers.testChecker,
                Runners.test,
                ResultsProcessors.main);
    }


}
