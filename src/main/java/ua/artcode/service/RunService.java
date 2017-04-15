package ua.artcode.service;

import ua.artcode.model.ExternalCode;
import ua.artcode.model.RunResults;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by v21k on 15.04.17.
 */
public interface RunService {
    RunResults runClass(ExternalCode code) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
