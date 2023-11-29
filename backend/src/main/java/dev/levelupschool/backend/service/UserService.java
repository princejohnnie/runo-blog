package dev.levelupschool.backend.service;

import dev.levelupschool.backend.auth.AuthenticationUtils;
import dev.levelupschool.backend.dtos.ArticleDto;
import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.dtos.UserDto;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PagedResourcesAssembler<UserDto> pagedResourcesAssembler;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PagedModel<EntityModel<UserDto>> getAllUsers(Pageable paging) {
        var result = userRepository.findAll(paging).map(UserDto::new);
        return pagedResourcesAssembler.toModel(result);
    }

    public UserDto getUser(Long id) {
        return new UserDto(userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id)));
    }

    public User createUser(User newUser) throws Exception {
        var user = userRepository.findByEmail(newUser.getEmail());

        if (user != null) {
            throw new Exception("user with email already exists");
        }

        return userRepository.save(newUser);
    }

    public UserDto updateUser(UpdateUserRequest newUser, Long id) throws Exception {
        var loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        var checkUser = userRepository.findByEmail(newUser.getEmail());
        if (checkUser != null) {
            throw new Exception("user with email already exists");
        }

        var user = userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id));

        if (!loggedInUser.getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot update another user's profile!");
        }

        user.setEmail(newUser.getEmail() == null ? user.getEmail() : newUser.getEmail());
        user.setName(newUser.getName() == null ? user.getName() : newUser.getName());
        user.setPassword(newUser.getPassword() == null ? user.getPassword() : newUser.getPassword());

        return new UserDto(userRepository.save(user));
    }

    public void deleteUser(Long id) throws AccessDeniedException {
        var loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        var user = userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id));

        if (!loggedInUser.getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot delete another user!");
        }

        userRepository.deleteById(id);
    }

    public Map<String, List<ArticleDto>> getUserArticles(Long userId) {
        var user = userRepository
            .findById(userId)
            .orElseThrow(() -> new ModelNotFoundException(User.class, userId));

        if (user.getArticles() == null) {
            return null;
        }

        Map<String, List<ArticleDto>> response = new HashMap<>();
        response.put("items", user.getArticles().stream().map(ArticleDto::new).toList());

        return response;
    }

    public Map<String, List<CommentDto>> getUserComments(Long userId) {
        var user = userRepository
            .findById(userId)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, userId));

        if (user.getComments() == null) {
            return null;
        }

        Map<String, List<CommentDto>> response = new HashMap<>();
        response.put("items",user.getComments().stream().map(CommentDto::new).toList());

        return response;
    }
}
