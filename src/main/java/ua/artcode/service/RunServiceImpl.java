package ua.artcode.service;

import org.springframework.stereotype.Service;
import ua.artcode.core.method_checkers.Checkers;
import ua.artcode.core.results_processor.ResultsProcessors;
import ua.artcode.core.method_runner.Runners;
import ua.artcode.core.RunCore;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.RunResults;
import ua.artcode.utils.IOUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class RunServiceImpl implements RunService {

    @Override
    public RunResults runClass(ExternalCode code) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String path = IOUtils.saveExternalCodeLocally(code.getSourceCode());
        return RunCore.runMethod(path, Checkers.mainChecker, Runners.main, ResultsProcessors.mainProcessor);
    }
}
