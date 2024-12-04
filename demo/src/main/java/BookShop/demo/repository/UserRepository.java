package BookShop.demo.repository;

import BookShop.demo.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findById(int id);
    boolean existsByEmail(String email);
    Void deleteById(int id);
    User findByEmail(String email);
    List<User> findAll();

}
