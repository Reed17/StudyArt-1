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
    int updateCourse(String sourcesRoot, String testsRoot, int courseId);

}
