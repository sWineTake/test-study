package com.tester.part02.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Article {
	private Long id;
	private Board board;
	private String subject;
	private String content;
	private String username;
	private LocalDateTime createdAt;


	public void update(String subject, String content) {
		this.subject = subject;
		this.content = content;
	}
}
