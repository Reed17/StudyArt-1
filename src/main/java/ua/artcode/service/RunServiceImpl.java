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
import ua.artcode.model.Lesson;
import ua.artcode.model.RunResults;
import ua.artcode.utils.IOUtils;
import ua.artcode.utils.RunUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class RunServiceImpl implements RunService {

    @Autowired
    private StudyDB<Course> courseDB;

    @Override
    public RunResults runMain(ExternalCode code) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String path = IOUtils.saveExternalCodeLocally(code.getSourceCode());
        String[] classes = {path};
        return RunCore.runMethod(classes, PreProcessors.singleClass, Checkers.main, Runners.main, ResultsProcessors.main);
    }

    @Override
    public RunResults runLesson(int courseId, int lessonNumber) throws InvalidIDException, CourseNotFoundException, LessonNotFoundException, ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Course course = courseDB.getByID(courseId);
        Lesson lesson = RunUtils.getLesson(lessonNumber, course);
        String[] classPaths = IOUtils.parseJavaFiles(lesson.getLocalPath());

        return RunCore.runMethod(classPaths, PreProcessors.lessons, Checkers.main, Runners.main, ResultsProcessors.main);
    }


}
