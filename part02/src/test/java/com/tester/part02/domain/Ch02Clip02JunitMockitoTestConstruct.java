package com.tester.part02.domain;

import com.tester.part02.application.port.out.CommandArticlePort;
import com.tester.part02.application.port.out.LoadArticlePort;
import com.tester.part02.application.port.out.LoadBoardPort;
import com.tester.part02.application.service.ArticleService;
import com.tester.part02.domain.factory.ArticleFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;

public class Ch02Clip02JunitMockitoTestConstruct {

	// 테스트하고자하는 주 서비스
	private ArticleService sut;

	// mock 할당
	private LoadArticlePort loadArticlePort;
	private CommandArticlePort commandArticlePort;
	private LoadBoardPort loadBoardPort;

	@BeforeEach
	void setUp() {
		loadArticlePort = Mockito.mock(LoadArticlePort.class);
		commandArticlePort = Mockito.mock(CommandArticlePort.class);
		loadBoardPort = Mockito.mock(LoadBoardPort.class);

		sut = new ArticleService(loadArticlePort, commandArticlePort, loadBoardPort);
	}

	@Test
	public void ARTICLE_ID_로_조회시_ARTICLE_반환() {
		// given
		var article = ArticleFixtures.article();

		// when
		// 특정 입력값을 받으면 특정값을 리턴하도록
			// loadArticlePort.findArticleById 호출시 파라미터는 어떤값이라도 Optional.of(article)값을 리턴하도록 설정
		Mockito.when(loadArticlePort.findArticleById(any())).thenReturn(Optional.of(article));
		var result = sut.getArticleById(1L);

		// then
		then(result)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", article.getId())
			.hasFieldOrPropertyWithValue("board.id", article.getBoard().getId())
			.hasFieldOrPropertyWithValue("subject", article.getSubject())
			.hasFieldOrPropertyWithValue("content", article.getContent())
			.hasFieldOrPropertyWithValue("username", article.getUsername())
			.hasFieldOrPropertyWithValue("createdAt", article.getCreatedAt());
	}

}
