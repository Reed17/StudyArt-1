package ua.artcode.dao;

import org.eclipse.jgit.api.errors.GitAPIException;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonsParsingException;

import java.util.Collection;

/**
 * Created by v21k on 15.04.17.
 */
public interface StudyDB<T> {
    boolean add(T T) throws GitAPIException, DirectoryCreatingException, LessonsParsingException;
    boolean remove(int id);
    Collection<T> getAll();
    T getByID(int id) throws InvalidIDException;
}
