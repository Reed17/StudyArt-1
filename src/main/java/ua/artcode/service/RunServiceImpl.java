package ua.artcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.core.RunCore;
import ua.artcode.core.method_checker.MethodCheckers;
import ua.artcode.core.method_runner.Runners;
import ua.artcode.core.post_processor.ResultsProcessors;
import ua.artcode.core.pre_processor.PreProcessors;
import ua.artcode.dao.StudyArtDB;
import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonNotFoundException;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;
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
    private StudyArtDB courseDB;
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
        String[] classPaths = courseIOUtils.getLessonClassPaths(courseId, lessonNumber, courseDB);
        Course course = courseDB.getCourseByID(courseId);
        // todo 1st and 2nd args - project root and sources root have to be added as fields to Course model
        return runCore.runMethod(course.getLocalPath(),
                StringUtils.getClassRootFromClassPath(classPaths[0], "java/"),
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
        String[] classPaths = courseIOUtils.getLessonClassPaths(courseId, lessonNumber, courseDB);

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

        Course course = courseDB.getCourseByID(courseId);

        // run main (tests in psvm)
        // todo 1st and 2nd args - project root and sources root have to be added as fields to Course model
        RunResults results = runCore.runMethod(course.getLocalPath(),
                StringUtils.getClassRootFromClassPath(classPaths[0], "java/"),
                classPaths,
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        // rewrite original content again (reset to original state)
        commonIOUtils.deleteAndWrite(solutionClassPath, originalContent);

        return results;
    }


}
