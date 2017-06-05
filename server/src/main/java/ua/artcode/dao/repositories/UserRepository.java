package ua.artcode.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.artcode.model.User;

@Repository
public interface UserRepository<T extends User> extends CrudRepository<T, Integer> {
    T findByEmail(String email);

    T findByLogin(String login);

}
