package au.com.auspost.repository;

import au.com.auspost.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query(value="select * from USERS where USERNAME = ?1", nativeQuery = true)
    public User findByUsername(String username);
}
