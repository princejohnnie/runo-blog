package dev.levelupschool.backend.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateArticleRequest {
    @Valid
    private String title;
    @Valid
    private String content;

    private String slug;

    private int premium;
}
