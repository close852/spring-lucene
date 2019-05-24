package com.cjhm;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cjhm.entity.Article;
import com.cjhm.search.service.SearchService;

@SpringBootApplication
public class SpringJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaApplication.class, args);
	}

	@Bean
	public static CommandLineRunner getContext(SearchService service) {
		return (args) -> {
			Article article = Article.builder().articleId(4L).title("ti2tle.. t ti tit titl title").content("content...").build();
			Article article2 = Article.builder().title("tit5le2..").content("content2...").build();
			Article article3 = Article.builder().title("ti 776tt86asdw24w..").content("content2...").build();
			List<Article> lists = new ArrayList<>();
			lists.add(article);
			lists.add(article2);
			lists.add(article3);
//			service.indexing(lists);
			List<Document> dList =  service.multiSearch("cont");
			dList.forEach(System.out::println);
		};
	}

}
