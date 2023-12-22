package dev.levelupschool.backend.request;

import dev.levelupschool.backend.model.Category;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleRequest {
    @Valid
    private String title;
    @Valid
    private String content;

    private String slug;

    private List<Long> categories;
}
