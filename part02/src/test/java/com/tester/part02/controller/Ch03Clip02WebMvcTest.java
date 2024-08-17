package com.tester.part02.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tester.part02.adapter.in.api.ArticleController;
import com.tester.part02.adapter.in.api.dto.ArticleDto;
import com.tester.part02.application.port.in.CreateArticleUseCase;
import com.tester.part02.application.port.in.DeleteArticleUseCase;
import com.tester.part02.application.port.in.GetArticleUseCase;
import com.tester.part02.application.port.in.ModifyArticleUseCase;
import com.tester.part02.common.exception.ResourceNotFoundException;
import com.tester.part02.domain.Article;
import com.tester.part02.domain.factory.ArticleFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class)
public class Ch03Clip02WebMvcTest {

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
	class GetArticle {
		@Test
		public void ARTICLE_이_있으면_200_OK_RETURN_RESPONSE() throws Exception {
			// given
			var article = ArticleFixtures.article();

			// when
			given(getArticleUseCase.getArticleById((any()))).willReturn(article);

			// then
			Long articleId = 1L;
			mockMvc.perform(get("/articles/{articleId}", articleId))
				.andDo(print())
				.andExpect(status().isOk());
		}

		@Test
		public void ARTICLE_에_해당하는_ARTICLE_이_없으면_400_NOT_FOUND() throws Exception {
			given(getArticleUseCase.getArticleById(any()))
				.willThrow(new ResourceNotFoundException("article is not"));

			Long articleId = 1L;
			mockMvc.perform(get("/articles/{articleId}", articleId))
				.andDo(print())
				.andExpect(status().isNotFound());
		}
	}

	@Test
	@DisplayName("GET /articles?boardId={boardId}")
	public void listArticleByBoard() throws Exception {
		given(getArticleUseCase.getArticlesByBoard(any()))
			.willReturn(List.of(ArticleFixtures.article(1L), ArticleFixtures.article(2L)));

		mockMvc.perform(get("/articles?boardId={boardId}", 5L))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Nested
	@DisplayName("POST /articles")
	class PostArticle {
		@Test
		public void 생성된_ARTICLE_반환() throws Exception{
			// given
			var createArticle = ArticleFixtures.article();
			// post 객체 생성
			String body = objectMapper.writeValueAsString(
				Map.of(
					"boardId", 5L,
					"subject", "subject",
					"content", "content",
					"username", "towcowsong"
				)
			);

			given(createArticleUseCase.createArticle(any()))
				.willReturn(createArticle);

			mockMvc.perform(
				// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
				post("/articles")
					.contentType(MediaType.APPLICATION_JSON)
					.content(body)
				)
				.andDo(print())
				.andExpect(
					status().isOk()
				);
		}

		@ParameterizedTest(name = "{0}")
		@DisplayName("비정상 패턴이면 BadRequest")
		@CsvSource(
			value = {
				"subject is null,,content,user",
				"content is null,subject,,user",
				"username is null,subject,content,",
				"username is empty,subject,content,''"
			}
		)
		public void 비정상_패턴_테스트(String desc, String subject, String content, String username) throws Exception{
			var body = objectMapper.writeValueAsString(
				new ArticleDto.CreateArticleRequest(5L, subject, content, username));

			mockMvc.perform(
					post("/articles")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body)
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
		}


	}



}
