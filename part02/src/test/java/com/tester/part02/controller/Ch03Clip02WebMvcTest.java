package com.tester.part02.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tester.part02.adapter.in.api.ArticleController;
import com.tester.part02.adapter.in.api.dto.ArticleDto;
import com.tester.part02.adapter.in.api.dto.BoardDto;
import com.tester.part02.application.port.in.CreateArticleUseCase;
import com.tester.part02.application.port.in.DeleteArticleUseCase;
import com.tester.part02.application.port.in.GetArticleUseCase;
import com.tester.part02.application.port.in.ModifyArticleUseCase;
import com.tester.part02.common.exception.AccessDeniedException;
import com.tester.part02.common.exception.ResourceNotFoundException;
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
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
			.andExpect(status().is2xxSuccessful());
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

	@Nested
	@DisplayName("PUT /articles")
	class PutArticle {
		@Test
		public void 변경된_ARTICLE의_ARTICLE_ID_반환() throws Exception {
			// given
			var modifiedArticle = ArticleFixtures.article();
			given(modifyArticleUseCase.modifyArticle(any())).willReturn(modifiedArticle);

			// when
			var body = objectMapper.writeValueAsString(
				new ArticleDto.UpdateArticleRequest(5L, new BoardDto(1L, "name"), "subject", "content", "username")
			);

			// then
			mockMvc.perform(
					put("/articles")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body)
				).andDo(print())
				.andExpect(status().isOk());
		}

		@ParameterizedTest(name = "{0}")
		@CsvSource(
			value = {
				"subject is null,,content,user",
				"content is null,subject,,user",
			}
		)
		public void 파라미터_에러케이스(String desc, String subject, String content, String username) throws Exception{
			var body = objectMapper.writeValueAsString(
				new ArticleDto.UpdateArticleRequest(1L, new BoardDto(1L, "name"), subject, content, username)
			);

			mockMvc.perform(
				put("/articles")
					.contentType(MediaType.APPLICATION_JSON)
					.content(body)
				).andDo(print())
				.andExpect(status().isBadRequest());
		}

		@Test
		public void 권한미달_케이스() throws Exception{
			given(modifyArticleUseCase.modifyArticle(any()))
				.willThrow(new AccessDeniedException("다른 작성자는 수정 불가능"));

			var body = objectMapper.writeValueAsString(
				new ArticleDto.UpdateArticleRequest(5L, new BoardDto(1L, "name"), "subject", "content", "otherUsername")
			);

			mockMvc.perform(put("/articles").contentType(MediaType.APPLICATION_JSON).content(body))
				.andDo(print())
				// AdviceController에서 403에러를 출력하게 해두었기에 테스트코드 추가함
				.andExpect(status().isForbidden());
		}
	}

	@Test
	public void 삭제테스트() throws Exception {
		willDoNothing().given(deleteArticleUseCase).deleteArticle(any());

		mockMvc.perform(
			delete("/articles/{articleId}", 1L)
		).andDo(print()).andExpect(status().isOk());

		verify(deleteArticleUseCase).deleteArticle(1L);
	}

}
