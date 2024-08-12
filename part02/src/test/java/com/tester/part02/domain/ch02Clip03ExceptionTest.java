package com.tester.part02.domain;

import com.tester.part02.adapter.in.api.dto.ArticleDto;
import com.tester.part02.application.port.out.CommandArticlePort;
import com.tester.part02.application.port.out.LoadArticlePort;
import com.tester.part02.application.port.out.LoadBoardPort;
import com.tester.part02.application.service.ArticleService;
import com.tester.part02.domain.factory.BoardFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@ExtendWith(MockitoExtension.class)
public class ch02Clip03ExceptionTest {

	private ArticleService sut;

	@Mock
	private LoadArticlePort loadArticlePort;
	@Mock
	private CommandArticlePort commandArticlePort;
	@Mock//(strictness = Mock.Strictness.LENIENT)
	private LoadBoardPort loadBoardPort;

	private final Board board = BoardFixtures.board();

	@BeforeEach
	void setUp() {
		sut = new ArticleService(loadArticlePort, commandArticlePort, loadBoardPort);
	}

	@Test
	public void SUBJECT가_정상적이지않으면_에러() {
		// given
		var request = new ArticleDto.CreateArticleRequest(5L, null, "content", "user");

		// 여기서! given으로 loadBoardPort.findBoardById 응답 값을 Optional.of(board) 로 설정했는데,
		// createArticle 메소드에서 Assert.hasLength - 에러가 발생하고 설정한 응답값을 받지 못하여 에러가 발생했다.
		// given(loadBoardPort.findBoardById(any())).willReturn(Optional.of(board));
		// 이를 해결하기위해 (strictness = Mock.Strictness.LENIENT) 설정으로 Mock객체에서 끝까지 코드가 안가도 된다라는 의미에 설정값
		// 또는 given코드를 아예 주석처리하는것도 방법이다.

		// then
		thenThrownBy(() -> sut.createArticle(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("subject should not empty")
			.hasMessageContaining("not empty");

	}

}
