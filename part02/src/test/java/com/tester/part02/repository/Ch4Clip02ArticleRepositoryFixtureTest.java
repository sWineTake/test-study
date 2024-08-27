package com.tester.part02.repository;

import com.tester.part02.adapter.out.persistence.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest // -> @Transactional 포함되어있음
// 매번 테스트가 끝나면 롤백이된다.
@Sql("/data/ArticleRepositoryFixtureTest.sql") // SQL문 별도 분리
class Ch4Clip02ArticleRepositoryFixtureTest {

	@Autowired
	private ArticleRepository repository;

	@Test
	void listAllArticle() {
		var result = repository.findByBoardId(5L);

		then(result).hasSize(2);
	}

	@Test
	// 메소드단위 어노테이션에서 sql을 다르게 설정도 가능
	@Sql("/data/ArticleRepositoryFixtureTest.listAllArticles2.sql")
	void listAllArticles2() {
		var result = repository.findByBoardId(5L);

		then(result).hasSize(3);
	}

}
