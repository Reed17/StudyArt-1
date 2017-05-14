package ua.artcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.core.RunCore;
import ua.artcode.core.method_checker.MethodCheckers;
import ua.artcode.core.method_runner.Runners;
import ua.artcode.core.post_processor.ResultsProcessors;
import ua.artcode.core.pre_processor.PreProcessors;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.dao.repositories.LessonRepository;
import ua.artcode.model.Course;
import ua.artcode.model.CourseFromUser;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.Lesson;
import ua.artcode.model.response.RunResults;
import ua.artcode.utils.IO_utils.CommonIOUtils;
import ua.artcode.utils.IO_utils.CourseIOUtils;
import ua.artcode.utils.StringUtils;

import java.io.File;
import java.util.Arrays;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class RunServiceImpl implements RunService {

    private final CommonIOUtils commonIOUtils;
    private final CourseRepository courseDB;
    private final LessonRepository lessonDB;
    private final CourseIOUtils courseIOUtils;
    private final RunCore runCore;

    @Autowired
    public RunServiceImpl(CommonIOUtils commonIOUtils, CourseRepository courseDB, LessonRepository lessonDB, CourseIOUtils courseIOUtils, RunCore runCore) {
        this.commonIOUtils = commonIOUtils;
        this.courseDB = courseDB;
        this.lessonDB = lessonDB;
        this.courseIOUtils = courseIOUtils;
        this.runCore = runCore;
    }

    @Override
    public RunResults runMain(ExternalCode code) throws Exception {
        String path = courseIOUtils.saveExternalCodeLocally(code.getSourceCode());
        String[] classes = {path};

        String sourcesRoot = StringUtils.getClassRootFromClassPath(classes[0], File.separator);

        return runCore.run(sourcesRoot,
                new String[]{sourcesRoot},
                classes,
                PreProcessors.singleClass,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);
    }

    @Override
    public RunResults runLesson(int courseId, int lessonNumber) throws Exception {

        String[] classPaths = courseIOUtils.getLessonClassPaths(courseId, lessonNumber);
        Course course = courseDB.findOne(courseId);

        // todo 2nd arg - project sources root have to be added as fields to Course model
        String sourcesRoot = StringUtils.getClassRootFromClassPath(classPaths[0], "java" + File.separator);
        return runCore.run(course.getLocalPath(),
                new String[]{sourcesRoot},
                classPaths,
                PreProcessors.lessonsMain,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);
    }

    @Override
    public RunResults runLessonWithSolutionTests(int courseId, int lessonID, CourseFromUser userCource) throws Exception {

        String projectLocalPath = courseIOUtils.saveCourseLocally(userCource.getUrl(), userCource.getName(), userCource.getId());

        // todo get lesson by date (corresponding course)

        Lesson lesson = lessonDB.findOne(lessonID);

//        Lesson lesson = courseIOUtils.getLessonByID(projectLocalPath, lessonNumber);

        String[] classPaths = courseIOUtils.getLessonClassAndTestsPaths(lesson.getBaseClasses(), lesson.getTestsClasses(), lesson.getRequiredClasses());

//        String srcClassRoot = StringUtils.getClassRootFromClassPath(classPaths[0], "java" + File.separator);
//        String testClassRoot = StringUtils.getClassRootFromClassPath(classPaths[classPaths.length - 1], "java" + File.separator);

        String[] classPaths =



        // run main (tests classes)
        // todo 1st and 2nd args - project root and sources root have to be added as fields to Course model
        RunResults results = runCore.run(projectLocalPath,
                new String[]{srcClassRoot,
                        testClassRoot},
                classPaths,
                PreProcessors.lessonsTests,
                MethodCheckers.testChecker,
                Runners.test,
                ResultsProcessors.main);

        return results;
    }


}
