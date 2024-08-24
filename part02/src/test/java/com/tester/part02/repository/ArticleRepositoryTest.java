package com.tester.part02.repository;

import com.tester.part02.adapter.out.persistence.entity.ArticleJpaEntity;
import com.tester.part02.adapter.out.persistence.entity.BoardJpaEntity;
import com.tester.part02.adapter.out.persistence.repository.ArticleRepository;
import com.tester.part02.domain.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

@DataJpaTest
class ArticleRepositoryTest {

	@Autowired
	private ArticleRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	private BoardJpaEntity boardJpaEntity;

	@BeforeEach
	void setUp() {
		boardJpaEntity = entityManager.persist(new BoardJpaEntity("TEST"));

		entityManager.persist(new ArticleJpaEntity(boardJpaEntity, "subject1", "content1", "user1", LocalDateTime.now()));
		entityManager.persist(new ArticleJpaEntity(boardJpaEntity, "subject2", "content2", "user2", LocalDateTime.now()));
	}




}
