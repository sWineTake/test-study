package com.tester.part02.domain;

import com.tester.part02.application.port.out.CommandArticlePort;
import com.tester.part02.application.port.out.LoadArticlePort;
import com.tester.part02.application.port.out.LoadBoardPort;
import com.tester.part02.application.service.ArticleService;
import com.tester.part02.domain.factory.ArticleFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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

		// 아래처럼 loadArticlePort의 findBoardById메소드가 실행되었는지에 대하여 여부 체크 테스트 코드를 추가할수있다.
		// verify(loadArticlePort).findBoardById(1L);

		// 하지만, 좋은 테스트 코드가 아니다!
		// main코드가 수정됨에 따라 테스트 코드가 수정되어야하기에 테스트 코드의 목적에 어긋난다.
		// 테스트코드는 내부가 어떻든 원하는 결과를 얻을수있는지에 대하여 검증하기위해 테스트 코드를 작성한다.
		// 단 게시물 삭제 (아래 코드)처럼 삭제가 주된 원인이 되는 테스트의 경우에는 실행 여부를 확인한다.
	}

	@Test
	public void 게시물_삭제() {
		BDDMockito.willDoNothing()
			.given(commandArticlePort).deleteArticle(any());

		// 게시물의 삭제메소드의 경우에는 리턴값이 없기에 메소드 실행여부로 테스트 코드를 작성하게된다.
		sut.deleteArticle(1L);

		verify(commandArticlePort).deleteArticle(1L);
	}

	@Test
	public void BDDSTYLE_ARTICLE_ID_로_조회시_ARTICLE_반환() {
		// given
		Article article1 = ArticleFixtures.article(1L);
		Article article2 = ArticleFixtures.article(2L);

		// when
		BDDMockito.given(loadArticlePort.findArticlesByBoardId(any())).willReturn(List.of(article1, article2));

		// then
		List<Article> result = loadArticlePort.findArticlesByBoardId(1L);
		then(result)
			.hasSize(2)
			.extracting("board.id").containsOnly(1L);
	}




}
