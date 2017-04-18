package ua.artcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.core.RunCore;
import ua.artcode.core.method_checkers.Checkers;
import ua.artcode.core.method_runner.Runners;
import ua.artcode.core.post_processor.ResultsProcessors;
import ua.artcode.core.pre_processor.PreProcessors;
import ua.artcode.dao.StudyDB;
import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonNotFoundException;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.RunResults;
import ua.artcode.utils.IOUtils;
import ua.artcode.utils.StringUtils;

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
    private StudyDB<Course> courseDB;

    @Autowired
    private IOUtils ioUtils;

    @Autowired
    private RunCore runCore;

    @Override
    public RunResults runMain(ExternalCode code)
            throws ClassNotFoundException,
            IOException,
            InvocationTargetException,
            IllegalAccessException,
            NoSuchMethodException {
        String path = ioUtils.saveExternalCodeLocally(code.getSourceCode());
        String[] classes = {path};
        return runCore.runMethod(classes, PreProcessors.singleClass, Checkers.main, Runners.main, ResultsProcessors.main);
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
        String[] classPaths = ioUtils.getLessonClassPaths(courseId, lessonNumber, courseDB);
        return runCore.runMethod(classPaths, PreProcessors.lessons, Checkers.main, Runners.main, ResultsProcessors.main);
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
        String[] classPaths = ioUtils.getLessonClassPaths(courseId, lessonNumber, courseDB);

        //
        //  Should be discussed!!!! use Annotation instead of className @Solution on className
        // find class which contains "solution" in class name
        String solutionClassPath = StringUtils.getClassPathByClassName(classPaths, "solution");

        // saving original content for this class
        String originalContent = Files.readAllLines(Paths.get(solutionClassPath))
                .stream()
                .collect(Collectors.joining());

        // append solution
        String originalWithSolution = StringUtils.appendSolution(code, originalContent);

        // delete old content and write new (with solution)
        ioUtils.deleteAndWrite(solutionClassPath, originalWithSolution);

        // run main (tests in psvm)
        RunResults results = runCore.runMethod(classPaths, PreProcessors.lessons, Checkers.main, Runners.main,
                ResultsProcessors.main);

        // rewrite original content again (reset to original state)
        ioUtils.deleteAndWrite(solutionClassPath, originalContent);

        return results;
    }


}
