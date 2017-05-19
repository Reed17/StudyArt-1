package ua.artcode.dao.repositories;


import org.springframework.stereotype.Repository;
import ua.artcode.model.Teacher;

@Repository
public interface TeacherRepository extends UserRepository<Teacher> {
}
