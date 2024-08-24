package com.tester.part02.repository;

import com.tester.part02.adapter.out.persistence.ArticlePersistenceAdapter;
import com.tester.part02.adapter.out.persistence.entity.ArticleJpaEntity;
import com.tester.part02.adapter.out.persistence.entity.BoardJpaEntity;
import com.tester.part02.adapter.out.persistence.repository.ArticleRepository;
import com.tester.part02.domain.Article;
import com.tester.part02.domain.Board;
import com.tester.part02.persistence.BoardJpaEntityFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateArticleCaptorTest {

	private ArticlePersistenceAdapter adapter;

	@Mock
	private ArticleRepository articleRepository;

	@Captor
	ArgumentCaptor<ArticleJpaEntity> captor;

	@BeforeEach
	void setUp() {
		articleRepository = mock(ArticleRepository.class);

		adapter = new ArticlePersistenceAdapter(articleRepository);
	}

	@Nested
	@DisplayName("Aritlce 검증")
	class CreateArticle {

		private final Article article = Article.builder()
			.board(new Board(5L, "board"))
			.subject("subject")
			.content("content")
			.username("user")
			.createdAt(LocalDateTime.now())
			.build();

		@Test
		@DisplayName("argumentCapture 검증")
		void createArticle_verifySaveArg() {
			var board = BoardJpaEntityFixtures.board();
			var articleJpaEntity = new ArticleJpaEntity(board, "subject", "content", "user", LocalDateTime.parse("2024-08-24T11:23:33"));

			ReflectionTestUtils.setField(articleJpaEntity, "id", 1L);
			given(articleRepository.save(any())).willReturn(articleJpaEntity);

			// 캡터에 값저장
			adapter.createArticle(article);

			verify(articleRepository).save(captor.capture());

			// 캡터에 담긴 값을 조회하여 비교
			// 비교의 목적은 리턴값이 아닌 요청값을 개발자가 원하는대로 정상적으로 보냈는지에 대하여 체크할수있다.
			then(captor.getValue())
				.hasFieldOrPropertyWithValue("id", null)
				.hasFieldOrPropertyWithValue("board.id", 5L)
				.hasFieldOrPropertyWithValue("subject", "subject")
				.hasFieldOrPropertyWithValue("content", "content")
				.hasFieldOrPropertyWithValue("username", "user");
		}

	}



}
