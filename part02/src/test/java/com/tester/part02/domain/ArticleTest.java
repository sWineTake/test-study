package com.tester.part02.domain;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
	@BeforeAll
	static void initAll() {
		System.out.println("BeforeAll \n");
	}

	@AfterAll
	static void tearDownAll() {
		System.out.println("AfterAll\n");
	}

	@BeforeEach
	void setUp() {
		System.out.println("BeforeEach\n");
	}

	@AfterEach
	void tearDown() {
		System.out.println("AfterEach\n");
	}

	@Test
	@DisplayName("성공 테스트 - Article 생성")
	void constructArticle() {
		// given
		Board board = new Board();
		Article article = Article.builder()
			.id(1L)
			.board(board)
			.subject("subject")
			.content("content")
			.username("user")
			.createAt(LocalDateTime.now())
			.build();

		// when
		Assertions.assertEquals(1L, article.getId());
		Assertions.assertTrue(article.getBoard().equals(board));
		Assertions.assertEquals("subject", article.getSubject());
		Assertions.assertEquals("content", article.getContent());
		Assertions.assertEquals("user", article.getUsername());
		Assertions.assertNotNull(article.getCreateAt());
	}

	@Test
	@DisplayName("실패 테스트")
	void failingTest() {
		// 실패했을떄 3번째 인자를 통해 상세 메시지를 출력할 수 있음
		// Assertions.assertEquals(4, 1 + 2, "테스트 실패 시 출력되는 fail message");
		Assertions.assertEquals(4, 2 + 2, "테스트 실패 시 출력되는 fail message");
	}

	@Test
	// Disabled 어노테이션을 통해 해당 테스트를 생략할 수 있다.
	@Disabled("이 테스트를 Disable 이유")
	void skippingTest() {

	}
}
