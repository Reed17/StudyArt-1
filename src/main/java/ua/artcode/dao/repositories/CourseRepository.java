package ua.artcode.dao.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.model.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

    Course findByNameAndAuthor(String name, String author);


    @Transactional
    @Modifying
    @Query("update Course c set c.sourcesRoot  = ?1, c.testsRoot = ?2 where c.id = ?3")
    int updateSourcesAndTestsRoot(String sourcesRoot, String testsRoot, int courseId);

    @Transactional
    @Modifying
    @Query("update Course c set c.dependencies = ?1 where c.id = ?2")
    int updateDependencies(String[] dependencies, int courseId);


}
