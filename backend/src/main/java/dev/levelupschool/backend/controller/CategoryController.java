package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.auth.AuthenticationUtils;
import dev.levelupschool.backend.dtos.CategoryDto;
import dev.levelupschool.backend.exception.CustomValidationException;
import dev.levelupschool.backend.model.Category;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> index() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> show(@PathVariable Long id) {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> store(@RequestBody Category category) throws CustomValidationException {
        CategoryDto storedTag = categoryService.store(category);

        return new ResponseEntity<>(storedTag, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDto> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
