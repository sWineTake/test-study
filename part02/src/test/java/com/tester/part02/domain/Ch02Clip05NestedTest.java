package com.tester.part02.domain;

import com.tester.part02.application.port.out.CommandArticlePort;
import com.tester.part02.application.port.out.LoadArticlePort;
import com.tester.part02.application.port.out.LoadBoardPort;
import com.tester.part02.application.service.ArticleService;
import com.tester.part02.common.exception.ResourceNotFoundException;
import com.tester.part02.domain.factory.ArticleFixtures;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class Ch02Clip05NestedTest {

	@InjectMocks
	private ArticleService sut;
	@Mock
	private LoadArticlePort loadArticlePort;
	@Mock
	private CommandArticlePort commandArticlePort;
	@Mock
	private LoadBoardPort loadBoardPort;

	@Test
	public void ARTICLE_ID_로_조회시_ARTICLE_반환() {
		//given
		Article article = ArticleFixtures.article();

		// when
		given(loadArticlePort.findArticleById(any()))
			.willReturn(Optional.of(article));

		// then
		Article result = sut.getArticleById(article.getId());
		then(result)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", article.getId())
			.hasFieldOrPropertyWithValue("board.id", article.getBoard().getId())
			.hasFieldOrPropertyWithValue("subject", article.getSubject())
			.hasFieldOrPropertyWithValue("content", article.getContent())
			.hasFieldOrPropertyWithValue("username", article.getUsername())
			.hasFieldOrProperty("createdAt")
			;
	}

	@Test
	public void ARTICLE_ID_로_조회시_없을경우() {
		// given
		given(loadArticlePort.findArticleById(any())).willReturn(Optional.empty());

		// then
		thenThrownBy(() -> sut.getArticleById(1L))
			.isInstanceOf(ResourceNotFoundException.class);
	}


	@Description("위에 2개의 테스트를 @Nested어노테이션을 활용하여 한개의 이너클래스 생성후 하위로 집어넣음")
	@Nested
	@DisplayName("ARTICLE 조회")
	class GetArticle {
		@Test
		public void ARTICLE_ID_로_조회시_ARTICLE_반환() {
			//given
			Article article = ArticleFixtures.article();

			// when
			given(loadArticlePort.findArticleById(any()))
				.willReturn(Optional.of(article));

			// then
			Article result = sut.getArticleById(article.getId());
			then(result)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", article.getId())
				.hasFieldOrPropertyWithValue("board.id", article.getBoard().getId())
				.hasFieldOrPropertyWithValue("subject", article.getSubject())
				.hasFieldOrPropertyWithValue("content", article.getContent())
				.hasFieldOrPropertyWithValue("username", article.getUsername())
				.hasFieldOrProperty("createdAt")
			;
		}

		@Test
		public void ARTICLE_ID_로_조회시_없을경우() {
			// given
			given(loadArticlePort.findArticleById(any())).willReturn(Optional.empty());

			// then
			thenThrownBy(() -> sut.getArticleById(1L))
				.isInstanceOf(ResourceNotFoundException.class);
		}
	}



}
