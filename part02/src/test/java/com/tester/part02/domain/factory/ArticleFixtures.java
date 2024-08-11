package com.tester.part02.domain.factory;

import com.tester.part02.domain.Article;
import com.tester.part02.domain.Board;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleFixtures {

	public static Article article(Long id) {
		var board = new Board(1L, "board");
		return new Article(id, board, "subject" + id, "content" + id, "user" + id, LocalDateTime.parse("2024-08-11T00:00:01").plusDays(id));
	}

	public static Article article() {
		var board = new Board(1L, "Board");

		return new Article(1L, board, "subject", "content", "user", LocalDateTime.parse("2024-08-11T00:00:01"));
	}

}
