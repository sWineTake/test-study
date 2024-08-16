package com.tester.part02.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.tester.part02.adapter.in.api.ArticleController;
import com.tester.part02.application.port.in.CreateArticleUseCase;
import com.tester.part02.application.port.in.DeleteArticleUseCase;
import com.tester.part02.application.port.in.GetArticleUseCase;
import com.tester.part02.application.port.in.ModifyArticleUseCase;
import com.tester.part02.common.api.GlobalControllerAdvice;
import com.tester.part02.common.exception.ResourceNotFoundException;
import com.tester.part02.domain.Article;
import com.tester.part02.domain.factory.ArticleFixtures;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class Ch03Clip01ArticleControllerUnitTest {

	private MockMvc mockMvc;
	@InjectMocks
	private ArticleController articleController;
	@Mock
	private GetArticleUseCase getArticleUseCase;
	@Mock
	private CreateArticleUseCase createArticleUseCase;
	@Mock
	private ModifyArticleUseCase modifyArticleUseCase;
	@Mock
	private DeleteArticleUseCase deleteArticleUseCase;

	private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
		.serializers(LocalTimeSerializer.INSTANCE)
		.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
		.build();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders
			.standaloneSetup(articleController)
			.alwaysDo(print())
			.setControllerAdvice(new GlobalControllerAdvice()) // 스프링 부트 테스트를 사용하지않기에 직접 커스텀 에러 핸들러
			.setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
			.build();
	}

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
				.andExpect(status().isOk());
		}

		@Test
		public void ARTICLE_에_해당하는_ARTICLE_이_없으면_400_NOT_FOUND() throws Exception {
			given(getArticleUseCase.getArticleById(any()))
				.willThrow(new ResourceNotFoundException("article is not"));

			Long articleId = 1L;
			mockMvc.perform(get("/articles/{articleId}", articleId))
				.andExpect(status().isNotFound());
		}

	}



}
