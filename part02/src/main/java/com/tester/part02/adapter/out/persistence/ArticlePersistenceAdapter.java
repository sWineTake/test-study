package com.tester.part02.adapter.out.persistence;

import com.tester.part02.adapter.out.persistence.entity.ArticleJpaEntity;
import com.tester.part02.adapter.out.persistence.repository.ArticleRepository;
import com.tester.part02.application.port.out.CommandArticlePort;
import com.tester.part02.application.port.out.LoadArticlePort;
import com.tester.part02.domain.Article;
import com.tester.part02.domain.Board;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ArticlePersistenceAdapter implements LoadArticlePort, CommandArticlePort {
    private final ArticleRepository articleRepository;

    public ArticlePersistenceAdapter(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Optional<Article> findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
            .map(article ->
                Article.builder()
                    .id(article.getId())
                    .board(new Board(article.getBoard().getId(), article.getBoard().getName()))
                    .subject(article.getSubject())
                    .content(article.getContent())
                    .username(article.getUsername())
                    .createdAt(article.getCreatedAt())
                    .build()
            );
    }

    @Override
    public List<Article> findArticlesByBoardId(Long boardId) {
        return articleRepository.findByBoardId(boardId).stream()
            .map(article ->
                Article.builder()
                    .id(article.getId())
                    .board(new Board(article.getBoard().getId(), article.getBoard().getName()))
                    .subject(article.getSubject())
                    .content(article.getContent())
                    .username(article.getUsername())
                    .createdAt(article.getCreatedAt())
                    .build()
            )
            .toList();
    }

    @Override
    public Article createArticle(Article article) {
        var articleJpaEntity = articleRepository.save(ArticleJpaEntity.fromDomain(article));

        return articleJpaEntity.toDomain();
    }

    @Override
    public Article modifyArticle(Article article) {
        var articleJpaEntity = articleRepository.save(ArticleJpaEntity.fromDomain(article));

        return articleJpaEntity.toDomain();
    }

    @Override
    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
