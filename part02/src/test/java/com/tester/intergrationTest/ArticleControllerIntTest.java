package com.tester.intergrationTest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/com.example.demo.article.in.api/ArticleControllerIntTest.sql")
public class ArticleControllerIntTest {


	@Test
	@DisplayName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
	public void test() {
		System.out.println("AAAAAA");
	}

}
