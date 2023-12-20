package dev.levelupschool.backend.service;

import dev.levelupschool.backend.security.auth.AuthenticationProvider;
import dev.levelupschool.backend.dtos.ArticleDto;
import dev.levelupschool.backend.dtos.BookmarkDto;
import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.dtos.UserDto;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.UpdateUserRequest;
import dev.levelupschool.backend.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Qualifier("AWSStorageService")
    @Autowired
    private StorageService storageService;

    @Autowired
    private AuthenticationProvider authProvider;

    @Autowired
    private SlugService slugService;

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
        if (newUser.getName() != null && !newUser.getName().isEmpty()) {
            newUser.setSlug(slugService.makeSlug(newUser.getName()));
        }


        return userRepository.save(newUser);
    }

    public Map<String, String>  uploadAvatar(MultipartFile avatar) {
        User loggedInUser = authProvider.getAuthenticatedUser();

        Map<String, String> response = new HashMap<>();

        if (avatar != null) {
            var avatarUrl = storageService.store(avatar, "avatars/");
            loggedInUser.setAvatarUrl(avatarUrl.toString());
            response.put("avatarUrl", avatarUrl.toString());
        }

        userRepository.save(loggedInUser);

        return response;
    }

    public UserDto updateUser(UpdateUserRequest newUser, Long id) throws Exception {
        var loggedInUser = authProvider.getAuthenticatedUser();

        var checkUser = userRepository.findByEmail(newUser.getEmail());

        if (checkUser != null && !loggedInUser.getEmail().equals(checkUser.getEmail())) {
            throw new Exception("user with email already exists");
        }

        var user = userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id));

        if (!loggedInUser.getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot update another user's profile!");
        }

        user.setEmail(newUser.getEmail() == null ? user.getEmail() : newUser.getEmail());
        user.setName(newUser.getName() == null ? user.getName() : newUser.getName());

        if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
            user.setPassword(newUser.getPassword());
        }
        if (newUser.getName() != null && !newUser.getName().equals(user.getName())) {
            user.setSlug(slugService.makeSlug(newUser.getName()));
            System.out.println("My user" + user.getSlug());
        }

        return new UserDto(userRepository.save(user));
    }

    public void deleteUser(Long id) throws AccessDeniedException {
        var loggedInUser = authProvider.getAuthenticatedUser();

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

    public List<UserDto> getFollowers(Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id));

        return user.followers.stream().map(UserDto::new).toList();
    }

    public List<UserDto> getFollowing(Long id) {
        var user = userRepository
            .findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id));

        return user.following.stream().map(UserDto::new).toList();
    }

    public void followUser(Long userId) throws Exception {
        var loggedInUser = authProvider.getAuthenticatedUser();

        var user = userRepository
            .findById(userId)
            .orElseThrow(() -> new ModelNotFoundException(User.class, userId));

        if (loggedInUser.getId().equals(userId)) {
            throw new OperationNotSupportedException("You cannot follow yourself");
        }

        if (loggedInUser.following.contains(user)) {
            throw new OperationNotSupportedException("You are already following user with id " + userId);
        }

        loggedInUser.following.add(user);

        userRepository.save(loggedInUser);

    }

    public void unfollowUser(Long userId) throws Exception {
        var loggedInUser = authProvider.getAuthenticatedUser();

        var user = userRepository
            .findById(userId)
            .orElseThrow(() -> new ModelNotFoundException(User.class, userId));

        if (!loggedInUser.following.contains(user)) {
            throw new OperationNotSupportedException("You are not following the user");
        }

        loggedInUser.following.remove(user);

       userRepository.save(loggedInUser);

    }

    public List<BookmarkDto> getBookmarks() {
        var loggedInUser = authProvider.getAuthenticatedUser();

        return loggedInUser.bookmarks.stream().map(BookmarkDto::new).toList();
    }

}
