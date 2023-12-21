package dev.levelupschool.backend.repository;

import dev.levelupschool.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category existsDistinctByName(String name);

}
