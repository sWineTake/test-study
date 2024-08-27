package com.tester.part02.repository;

import com.tester.part02.adapter.out.persistence.entity.ArticleJpaEntity;
import com.tester.part02.adapter.out.persistence.entity.BoardJpaEntity;
import com.tester.part02.adapter.out.persistence.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class ArticleRepositoryTest {

	@Autowired
	private ArticleRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	private BoardJpaEntity boardJpaEntity;

	@BeforeEach
	void setUp() {
		// 엔티티 객체를 만듬
		boardJpaEntity = entityManager.persist(new BoardJpaEntity("TEST"));

		// 임의에 데이터를 미리 저장
		entityManager.persist(new ArticleJpaEntity(boardJpaEntity, "subject1", "content1", "user1", LocalDateTime.now()));
		entityManager.persist(new ArticleJpaEntity(boardJpaEntity, "subject2", "content2", "user2", LocalDateTime.now()));
	}

	@Test
	void listAllArticles() {
		var result = repository.findByBoardId(boardJpaEntity.getId());
		then(result).hasSize(2);
	}




}
