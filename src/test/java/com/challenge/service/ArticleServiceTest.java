package com.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.challenge.entities.ArticleEntity;
import com.challenge.model.Article;
import com.challenge.repositories.ArticleRepository;
import com.challenge.services.ArticleService;
import com.challenge.utils.ArticlesSearchParams;
import com.challenge.utils.ServiceTestResources;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

class ArticleServiceTest extends ServiceTestResources {

  private String author1 = "author1";
  private String author2 = "author2";
  private String author3 = "author3";
  private String title1 = "title1";
  private String title2 = "title2";
  private String title3 = "title3";
  private String title4 = "title4";
  private String title5 = "title5";
  private String title6 = "title6";
  private String title7 = "title7";
  private String tag1 = "tag1";
  private String tag2 = "tag2";
  private String tag3 = "tag3";

  @Autowired
  ArticleRepository articleRepository;

  @Autowired
  MongoTemplate mongoTemplate;

  ArticleService articleService;

  ArticlesSearchParams searchParams;

  @BeforeEach
  void beforeEach() {
    articleRepository.deleteAll();
    articleService = new ArticleService(mongoTemplate);
    fillSearchData();
    searchParams = ArticlesSearchParams.builder().author("").tag("").title("")
        .month("").page(0).build();
  }

  @Test
  void searchByPages() {
    searchParams = searchParams.toBuilder().page(0).build();
    assertSearch(searchParams, "1", "2", "3", "4", "5");

    searchParams = searchParams.toBuilder().page(1).build();
    assertSearch(searchParams, "6");
  }

  @Test
  void searchByAuthor() {
    searchParams = searchParams.toBuilder().author(author1).build();
    assertSearch(searchParams, "1", "2");

    searchParams = searchParams.toBuilder().author(author2).build();
    assertSearch(searchParams, "3", "4");

    searchParams = searchParams.toBuilder().author(author3).build();
    assertSearch(searchParams, "5", "6");
  }

  @Test
  void searchByTag() {
    searchParams = searchParams.toBuilder().tag(tag1).build();
    assertSearch(searchParams, "1", "3");

    searchParams = searchParams.toBuilder().tag(tag2).build();
    assertSearch(searchParams, "2", "5", "6");

    searchParams = searchParams.toBuilder().tag(tag3).build();
    assertSearch(searchParams, "4");
  }

  @Test
  void searchByTitle() {
    searchParams = searchParams.toBuilder().title(title1).build();
    assertSearch(searchParams, "1");
    searchParams = searchParams.toBuilder().title(title2).build();
    assertSearch(searchParams, "2");
    searchParams = searchParams.toBuilder().title(title3).build();
    assertSearch(searchParams, "3");
    searchParams = searchParams.toBuilder().title(title4).build();
    assertSearch(searchParams, "4");
    searchParams = searchParams.toBuilder().title(title5).build();
    assertSearch(searchParams, "5");
    searchParams = searchParams.toBuilder().title(title6).build();
    assertSearch(searchParams, "6");
    searchParams = searchParams.toBuilder().title(title7).build();
    assertSearch(searchParams);
  }

  @Test
  void searchByMonth() {
    searchParams = searchParams.toBuilder().month("january").build();
    assertSearch(searchParams, "1");
    searchParams = searchParams.toBuilder().month("february").build();
    assertSearch(searchParams, "2");
    searchParams = searchParams.toBuilder().month("march").build();
    assertSearch(searchParams, "3");
    searchParams = searchParams.toBuilder().month("april").build();
    assertSearch(searchParams, "4");
    searchParams = searchParams.toBuilder().month("may").build();
    assertSearch(searchParams, "5");
    searchParams = searchParams.toBuilder().month("june").build();
    assertSearch(searchParams, "6");
    searchParams = searchParams.toBuilder().month("july").build();
    assertSearch(searchParams);
  }

  @Test
  void hideArticle() {
    articleService.hideArticle("2");

    searchParams = searchParams.toBuilder().month("february").build();
    assertSearch(searchParams);
  }

  void assertSearch(ArticlesSearchParams searchParams, String... expectedIds) {
    List<Article> articles = articleService.searchArticles(searchParams.getAuthor(),
        searchParams.getTitle(), searchParams.getTag(), searchParams.getMonth(),
        searchParams.getPage());
    assertThat(articles).hasSize(expectedIds.length)
        .extracting(Article::getArticleId).containsExactlyInAnyOrder(expectedIds);
  }

  void fillSearchData() {
    Stream.of(
            Tuple.tuple("1", author1, title1, tag1, "2023-01-01T00:00:00Z", null),
            Tuple.tuple("2", author1, title2, tag2, "2023-02-01T00:00:00Z", null),
            Tuple.tuple("3", author2, title3, tag1, "2023-03-01T00:00:00Z", null),
            Tuple.tuple("4", author2, title4, tag3, "2023-04-01T00:00:00Z", null),
            Tuple.tuple("5", author3, title5, tag2, "2023-05-01T00:00:00Z", null),
            Tuple.tuple("6", author3, title6, tag2, "2023-06-01T00:00:00Z", null),
            Tuple.tuple("7", author3, title7, tag1, "2023-07-01T00:00:00Z", Instant.now())
        )
        .map(tuple -> ArticleEntity.builder()
            .articleId((String) tuple.toList().getFirst())
            .author((String) tuple.toList().get(1))
            .title((String) tuple.toList().get(2))
            .url("url")
            .commentText("commentText")
            .createdAtI(1)
            .numComments(1)
            .points(1)
            .storyId(1)
            .updatedAt("updatedAt")
            .createdAt((String) tuple.toList().get(4))
            .lastSyncAt(Instant.now())
            .hiddenAt((Instant) tuple.toList().get(5))
            .tags(List.of((String) tuple.toList().get(3)))
            .build()).forEach(articleRepository::save);
  }

}
