package com.tester.part02.application.port.out;

import com.tester.part02.domain.Article;

import java.util.List;
import java.util.Optional;

public interface LoadArticlePort {
    Optional<Article> findArticleById(Long articleId);
    List<Article> findArticlesByBoardId(Long boardId);
}
