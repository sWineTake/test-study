package com.tester.part02.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tester.part02.adapter.in.api.ArticleController;
import com.tester.part02.adapter.in.api.dto.ArticleDto;
import com.tester.part02.adapter.in.api.dto.BoardDto;
import com.tester.part02.application.port.in.CreateArticleUseCase;
import com.tester.part02.application.port.in.DeleteArticleUseCase;
import com.tester.part02.application.port.in.GetArticleUseCase;
import com.tester.part02.application.port.in.ModifyArticleUseCase;
import com.tester.part02.common.exception.ResourceNotFoundException;
import com.tester.part02.domain.Article;
import com.tester.part02.domain.Board;
import com.tester.part02.domain.factory.ArticleFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class)
public class Ch03Clip03JsonPathAssertTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private GetArticleUseCase getArticleUseCase;
	@MockBean
	private CreateArticleUseCase createArticleUseCase;
	@MockBean
	private ModifyArticleUseCase modifyArticleUseCase;
	@MockBean
	private DeleteArticleUseCase deleteArticleUseCase;


	@Nested
	@DisplayName("GET /articles/{articleId}")
	public class setGetArticleUseCase {

		@Test
		@DisplayName("ARTICLE이 있으면, 200 OK return response")
		public void returnResponse() throws Exception {
			// given
			var article = ArticleFixtures.article();

			// when
			given(getArticleUseCase.getArticleById(any())).willReturn(article);

			// then
			mockMvc.perform(get("/articles/{articleId}", 1L))
				.andDo(print())
				.andExpectAll(
					status().isOk(),
					jsonPath("$.id").value(article.getId()),
					jsonPath("$.board.id").value(article.getBoard().getId()),
					jsonPath("$.subject").value(article.getSubject()),
					jsonPath("$.content").value(article.getContent()),
					jsonPath("$.username").value(article.getUsername()),
					jsonPath("$.createdAt").value(article.getCreatedAt().toString())
				);
		}

		@Test
		@DisplayName("articleId에 해당하는 Article이 없으면 400 Not Found")
		public void notFound() throws Exception{

			given(getArticleUseCase.getArticleById(any())).willThrow(new ResourceNotFoundException(""));

			mockMvc.perform(get("/articles/{articleId}", 1L))
				.andDo(print())
				.andExpectAll(status().isNotFound());
		}
	}

	@Test
	@DisplayName("GET /articles?boardId={boardId}")
	public void listArticlesByBoard() throws Exception {

		given(getArticleUseCase.getArticlesByBoard(any()))
			.willReturn(
				List.of(ArticleFixtures.article(1L), ArticleFixtures.article(2L))
			);

		mockMvc.perform(get("/articles?boardId={boardId}", 1L))
			.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.size()").value(2),
				jsonPath("$.[0].id").value(1L),
				jsonPath("$.[1].id").value(2L)
			);
	}

	@Nested
	@DisplayName("POST Article")
	public class PostArticle {
		@Test
		@DisplayName("생성된 Article ID 반환")
		public void returnArticleId() throws Exception {
			var article = ArticleFixtures.article(1L);

			var body = objectMapper.writeValueAsString(
				new ArticleDto.CreateArticleRequest(1L, "subject", "content", "username")
			);

			given(createArticleUseCase.createArticle(any())).willReturn(article);

			mockMvc.perform(
				post("/articles")
					.contentType(MediaType.APPLICATION_JSON)
					.content(body)
				)
				.andExpectAll(
					status().isOk(),
					jsonPath("$.id").value(1L),
					jsonPath("$.id").exists() // 해당값이 존재한다면 true
				);
		}

		@Test
		@DisplayName("수정 Article ID 반환")
		public void modifyArticleId() throws Exception {
			var article = ArticleFixtures.article(1L);
			var board = new BoardDto(10L, "boardName");

			var body = objectMapper.writeValueAsString(
				new ArticleDto.UpdateArticleRequest(1L, board, "subject","content", "username")
			);

			given(modifyArticleUseCase.modifyArticle(any())).willReturn(article);

			mockMvc.perform(
					put("/articles")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body)
				)
				.andExpectAll(
					status().isOk(),
					jsonPath("$.id").value(1L),
					jsonPath("$.id").exists() // 해당값이 존재한다면 true
				);
		}
	}





}






































