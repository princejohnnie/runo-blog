package dev.levelupschool.backend.service;

import dev.levelupschool.backend.auth.AuthenticationUtils;
import dev.levelupschool.backend.dtos.CategoryDto;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Category;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.CategoryRepository;
import dev.levelupschool.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.map(this::convertToDto).orElseThrow(() -> new ModelNotFoundException(Category.class, id));
    }

    public CategoryDto store(Category category) {
      Category categoryName = categoryRepository.existsDistinctByName(category.getName());
        if(categoryName == null) {
            categoryName = new Category();
            categoryName.setName(category.getName());
        }
        return convertToDto(categoryName);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
    private CategoryDto convertToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}

