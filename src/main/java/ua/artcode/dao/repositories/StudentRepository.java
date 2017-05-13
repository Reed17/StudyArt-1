package ua.artcode.dao.repositories;


import org.springframework.stereotype.Repository;
import ua.artcode.model.Student;

@Repository
public interface StudentRepository extends UserRepository<Student> {
}
