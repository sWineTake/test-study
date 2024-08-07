package com.tester.part02.application.port.in;


import com.tester.part02.adapter.in.api.dto.ArticleDto;
import com.tester.part02.domain.Article;

public interface ModifyArticleUseCase {
    Article modifyArticle(ArticleDto.UpdateArticleRequest request);
}
