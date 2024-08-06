package com.tester.part02.domain;


import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.time.LocalDateTime;

public class Ch02Clip01JunitAssertJTest {

	private Board board;

	@BeforeEach
	void setUp() {
		board = new Board(5L, "board");
	}

	@Test
	@DisplayName("AAA 패턴")
	void updateArticle() {
		// given
		Article article = Article.builder()
			.id(1L)
			.board(board)
			.subject("subject")
			.content("content")
			.username("user")
			.createAt(LocalDateTime.now())
			.build();

		// when
		article.update("update-subject", "update-content");

		// 값 1개로 여러개의 조건 테스트를 한번에 진행할 수 있다.
		Assertions.assertThat(article.getId())
			.isNotNull()
			.isEqualTo(1L)
			.isGreaterThan(0L);

		// 클래스 한번에 검증도 가능
		Assertions.assertThat(article)
			.hasNoNullFieldsOrProperties() // 모든 필드가 null이 아닐때
			.hasFieldOrPropertyWithValue("id", 1L)
			.hasFieldOrPropertyWithValue("board.id", 5L) // 객체 안에 접근도 가능 article.getBoard().getId()를 대체
			.hasFieldOrPropertyWithValue("subject", "update-subject")
			.hasFieldOrPropertyWithValue("content", "update-content")
			.hasFieldOrProperty("createAt") // 값이 존재하냐 정도로 확인
		;
	}

	@Test
	@DisplayName("BDD - 패턴")
	void updateArticle_BDDStyle() {
		// given
		Article article = Article.builder()
			.id(1L)
			.board(board)
			.subject("subject")
			.content("content")
			.username("user")
			.createAt(LocalDateTime.now())
			.build();

		// when
		article.update("update-subject", "update-content");

		// then
		BDDAssertions.then(article)
			.hasNoNullFieldsOrProperties() // 모든 값이 존재할때
			.hasFieldOrPropertyWithValue("id", article.getId())
			.hasFieldOrPropertyWithValue("board.id", article.getBoard().getId())
			.hasFieldOrPropertyWithValue("subject", "update-subject")
			.hasFieldOrPropertyWithValue("user", "update-content")
		;

	}

}
