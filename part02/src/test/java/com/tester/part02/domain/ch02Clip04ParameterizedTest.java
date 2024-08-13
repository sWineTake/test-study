package com.tester.part02.domain;

import com.tester.part02.adapter.in.api.dto.ArticleDto;
import com.tester.part02.application.port.out.CommandArticlePort;
import com.tester.part02.application.port.out.LoadArticlePort;
import com.tester.part02.application.port.out.LoadBoardPort;
import com.tester.part02.application.service.ArticleService;
import com.tester.part02.domain.factory.ArticleFixtures;
import com.tester.part02.domain.factory.BoardFixtures;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Long.sum;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ch02Clip04ParameterizedTest {

    @InjectMocks
    private ArticleService sut;
    @Mock
    private LoadArticlePort loadArticlePort;
    @Mock
    private CommandArticlePort commandArticlePort;
    @Mock
    private LoadBoardPort loadBoardPort;

    @ParameterizedTest
    @ValueSource(strings = {"elglish", "한글", "!@#$"})
    public void 영어_한국어_특수문자로_제목생성(String subject) {
        // given
        var req = new ArticleDto.CreateArticleRequest(5L, subject, "content", "user");
        var board = BoardFixtures.board();
        var article = ArticleFixtures.article();

        // when
        given(loadBoardPort.findBoardById(any())).willReturn(Optional.of(board));
        given(commandArticlePort.createArticle(any())).willReturn(article);

        // then
        Article result = sut.createArticle(req);

        BDDAssertions.then(result).isEqualTo(article);
    }

    @ParameterizedTest
    @NullAndEmptySource // 아래와 동일한 프로세스
    // @ValueSource(string = {null, ""})
    public void NULL_공백_THROW_에러처리(String subject) {
        var req = new ArticleDto.CreateArticleRequest(5L, subject, "content", "user");

        BDDAssertions.thenThrownBy(() -> sut.createArticle(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{0} + {1} = {2}") // 출력 포맷팅도 변경 가능
    @CsvSource(
        value = {
            "2,3,5",
            "1,5,6",
            "10,11,21",
            // ",''" // 이렇게 null 과 빈값 ''을 보낼수도있다
        }
    )
    public void CSV_테스트_2개의_합(Integer a, Integer b, Integer sum) {
        BDDMockito.then(sum).equals(a + b);
    }

    @ParameterizedTest(name = "{0}, {1}, {2}, {3}")
    @MethodSource("invalidParameters")
    public void 메소드를_리턴하는_테스트코드_만들기(String name, String subject, String content, String username) {
        var req = new ArticleDto.CreateArticleRequest(5L, subject, content, username);

        BDDAssertions.thenThrownBy(() -> sut.createArticle(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> invalidParameters() {
        return Stream.of(
            Arguments.of("A", null , "content", "user"),
            Arguments.of("B", "" , null, "user"),
            Arguments.of("C", "subject" , "content", "user"),
            Arguments.of("D", "subject" , null, "user"),
            Arguments.of("E", "subject" , "", "user"),
            Arguments.of("F", "subject" , "content", null)
        );
    }






}
