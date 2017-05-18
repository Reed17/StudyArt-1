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
    public RunResults runLessonWithSolutionTests(int courseID, int lessonID, CourseFromUser userCource) throws Exception {

        String projectLocalPath = courseIOUtils.saveCourseLocally(userCource.getUrl(), userCource.getName(), userCource.getId());

        Lesson lesson = lessonDB.findOne(lessonID);
        Course course = courseDB.findOne(courseID);

        String[] classPaths =
                courseIOUtils.getLessonClassAndTestsPaths(
                        lesson.getBaseClasses(),
                        lesson.getTestsClasses(),
                        lesson.getRequiredClasses());

        String sourcesRoot = course.getSourcesRoot();
        String testsRoot = course.getTestsRoot();

        RunResults results = runCore.run(projectLocalPath,
                new String[]{sourcesRoot,
                        testsRoot},
                classPaths,
                PreProcessors.lessonsTests,
                MethodCheckers.testChecker,
                Runners.test,
                ResultsProcessors.main);

        return results;
    }


}
