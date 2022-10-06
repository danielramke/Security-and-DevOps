package de.lyth.repositories;

import de.lyth.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);

    boolean existsByUsername(String name);

}
