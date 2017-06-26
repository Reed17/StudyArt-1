package ua.artcode.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.artcode.core.RunCore;
import ua.artcode.core.method_checker.MethodCheckers;
import ua.artcode.core.method_runner.Runners;
import ua.artcode.core.post_processor.ResultsProcessors;
import ua.artcode.core.pre_processor.PreProcessors;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.dao.repositories.LessonRepository;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.Lesson;
import ua.artcode.model.response.RunResults;
import ua.artcode.utils.IO_utils.CourseIOUtils;
import ua.artcode.utils.ResultChecker;
import ua.artcode.utils.StringUtils;

import java.io.File;
import java.util.Objects;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class RunServiceImpl implements RunService {

    private final CourseRepository courseDB;
    private final LessonRepository lessonDB;
    private final CourseIOUtils courseIOUtils;
    private final RunCore runCore;
    private final ResultChecker resultChecker;

    @Value("${application.courses.paths.externalCode}")
    private String pathForExternalCode;

    @Autowired
    public RunServiceImpl(CourseRepository courseDB, LessonRepository lessonDB,
                          CourseIOUtils courseIOUtils, RunCore runCore, ResultChecker resultChecker) {
        this.courseDB = courseDB;
        this.lessonDB = lessonDB;
        this.courseIOUtils = courseIOUtils;
        this.runCore = runCore;
        this.resultChecker = resultChecker;
    }

    @Override
    public RunResults runMain(ExternalCode code) throws Exception {
        String path = courseIOUtils.saveExternalCodeLocally(code.getSourceCode());

        String sourcesRoot = StringUtils.getClassRootFromClassPath(path, File.separator);

        RunResults runResults = runCore.run(sourcesRoot,
                new String[]{sourcesRoot},
                new String[]{path},
                new String[]{},
                PreProcessors.singleClass,
                MethodCheckers.main,
                Runners.main,
                ResultsProcessors.main);

        FileUtils.deleteDirectory(new File(pathForExternalCode));

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
                        lesson.getTestsClasses(),
                        lesson.getRequiredClasses());

        String sourcesRoot = course.getSourcesRoot();
        String testsRoot = course.getTestsRoot();

        RunResults results = runCore.run(projectLocalPath,
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


}
