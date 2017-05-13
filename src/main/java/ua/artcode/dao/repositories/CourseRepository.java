package ua.artcode.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.artcode.model.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

    Course findByNameAndAuthor(String name, String author);

}
