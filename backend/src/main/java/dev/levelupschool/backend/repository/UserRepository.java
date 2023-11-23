package dev.levelupschool.backend.repository;

import dev.levelupschool.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
