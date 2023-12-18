package dev.levelupschool.backend.dtos;

import dev.levelupschool.backend.model.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookmarkDto {
    public Long id;
    public String title;
    public String content;
    public String coverUrl;

    public BookmarkDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.coverUrl = article.getCoverUrl();
    }
}
