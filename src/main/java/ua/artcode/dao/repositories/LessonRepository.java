package ua.artcode.dao.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.artcode.model.Lesson;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Integer> {

}
