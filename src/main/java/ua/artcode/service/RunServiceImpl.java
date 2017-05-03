package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.core.RunCore;
import ua.artcode.core.method_checker.MethodCheckers;
import ua.artcode.core.method_runner.Runners;
import ua.artcode.core.post_processor.ResultsProcessors;
import ua.artcode.core.pre_processor.PreProcessors;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonNotFoundException;
import ua.artcode.model.Course;
import ua.artcode.model.CourseFromUser;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.Lesson;
import ua.artcode.model.response.RunResults;
import ua.artcode.utils.IO_utils.CommonIOUtils;
import ua.artcode.utils.IO_utils.CourseIOUtils;
import ua.artcode.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class RunServiceImpl implements RunService {

    @Autowired
    CommonIOUtils commonIOUtils;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseIOUtils courseIOUtils;
    @Autowired
    private RunCore runCore;

    @Override
    public RunResults runMain(ExternalCode code)
            throws ClassNotFoundException,
            IOException,
            InvocationTargetException,
            IllegalAccessException,
            NoSuchMethodException {
        String path = courseIOUtils.saveExternalCodeLocally(code.getSourceCode());
        String[] classes = {path};
        // todo 1st and 2nd args - project root and sources root have to be added as fields to Course model
        return runCore.runMethod(StringUtils.getClassRootFromClassPath(classes[0], File.separator),
                StringUtils.getClassRootFromClassPath(classes[0], File.separator),
                classes,
                PreProcessors.singleClass,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);
    }

    @Override
    public RunResults runLesson(int courseId, int lessonNumber)
            throws InvalidIDException,
            CourseNotFoundException,
            LessonNotFoundException,
            ClassNotFoundException,
            IOException,
            InvocationTargetException,
            IllegalAccessException,
            NoSuchMethodException {
        String[] classPaths = courseIOUtils.getLessonClassPaths(courseId, lessonNumber);
        Course course = courseRepository.findOne(courseId);
        // todo 1st and 2nd args - project root and sources root have to be added as fields to Course model
        return runCore.runMethod(course.getLocalPath(),
                StringUtils.getClassRootFromClassPath(classPaths[0], "java" + File.separator),
                classPaths,
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);
    }


    @Override
    public RunResults runLessonWithSolution(int courseId, int lessonNumber, ExternalCode code)
            throws InvalidIDException,
            CourseNotFoundException,
            LessonNotFoundException,
            ClassNotFoundException,
            IOException,
            InvocationTargetException,
            IllegalAccessException,
            NoSuchMethodException {
        String[] classPaths = courseIOUtils.getLessonClassPaths(courseId, lessonNumber);

        //
        //  Should be discussed!!!! use Annotation instead of className @Solution on className
        // find class which containsCourse "solution" in class name
        String solutionClassPath = StringUtils.getClassPathByClassName(classPaths, "solution");

        // saving original content for this class
        String originalContent = Files.readAllLines(Paths.get(solutionClassPath))
                .stream()
                .collect(Collectors.joining());

        // append solution
        String originalWithSolution = StringUtils.appendSolution(code, originalContent);

        // delete old content and write new (with solution)
        commonIOUtils.deleteAndWrite(solutionClassPath, originalWithSolution);

        Course course = courseRepository.findOne(courseId);

        // run main (tests in psvm)
        // todo 1st and 2nd args - project root and sources root have to be added as fields to Course model
        RunResults results = runCore.runMethod(course.getLocalPath(),
                StringUtils.getClassRootFromClassPath(classPaths[0], "java" + File.separator),
                classPaths,
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        // rewrite original content again (reset to original state)
        commonIOUtils.deleteAndWrite(solutionClassPath, originalContent);

        return results;
    }


    @Override
    public RunResults runLessonWithSolutionTests(int courseId, int lessonNumber, CourseFromUser userCource)
            throws InvalidIDException,
            CourseNotFoundException,
            LessonNotFoundException,
            ClassNotFoundException,
            IOException,
            InvocationTargetException,
            IllegalAccessException,
            NoSuchMethodException,
            GitAPIException,
            DirectoryCreatingException {

        String projectLocalPath = courseIOUtils.saveLocally(userCource.getUrl(), userCource.getName(), userCource.getId());

        Course course = courseRepository.findOne(courseId);

        Lesson lesson = courseIOUtils.getLessonByID(projectLocalPath, lessonNumber);

        String[] classPaths = courseIOUtils.getLessonClassAndTestsPaths(lesson.getLocalPath());

        // run main (tests classes)
        // todo 1st and 2nd args - project root and sources root have to be added as fields to Course model
        RunResults results = runCore.runMethodWithTests(projectLocalPath,
                StringUtils.getClassRootFromClassPath(classPaths[0], "java" + File.separator),
                StringUtils.getClassRootFromClassPath(classPaths[classPaths.length], "java" + File.separator),
                classPaths,
                PreProcessors.lessonsTests,
                MethodCheckers.testChecker,
                Runners.test,
                ResultsProcessors.main);

        return results;
    }


}
