package ua.artcode.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.artcode.model.Session;
import ua.artcode.model.User;

/**
 * Created by zhenia on 19.05.17.
 */
public interface SessionRepository extends CrudRepository<Session, Integer> {
    Session findByKey(String key);
    Session findByUser(User user);
}
