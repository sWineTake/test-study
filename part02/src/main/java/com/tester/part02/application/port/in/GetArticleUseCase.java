package com.tester.part02.application.port.in;

import com.tester.part02.domain.Article;

import java.util.List;

public interface GetArticleUseCase {
    Article getArticleById(Long articleId);

    List<Article> getArticlesByBoard(Long boardId);
}
