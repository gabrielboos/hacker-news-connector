package com.challenge.services;

import static com.challenge.utils.AppConstants.HACKER_NEWS_PAGEABLE_SIZE;

import com.challenge.entities.ArticleEntity;
import com.challenge.model.Article;
import com.challenge.model.HackerNewsArticle;
import io.micrometer.common.util.StringUtils;
import java.time.Instant;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

  private final MongoTemplate mongoTemplate;

  public void saveArticle(HackerNewsArticle article) {
    mongoTemplate.save(toArticleEntity(article).toBuilder()
        .lastSyncAt(Instant.now()).build());
    log.info("Saved article with articleId: {}", article.getObjectID());
  }

  public void hideArticle(String articleId) {
    Query query = new Query(Criteria.where("_id").is(articleId));
    Update update = new Update();
    update.set("hiddenAt", Instant.now());

    var result = mongoTemplate.updateFirst(query, update, ArticleEntity.class);
    log.info("{} articles were hidden for articleId: {}", result.getModifiedCount(), articleId);

    if (result.getModifiedCount() < 1) {
      throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,
          "Article not found for articleId: " + articleId);
    }
  }

  public List<Article> searchArticles(String author, String title, String tag, String month,
      int page) {

    Query query = buildQuery(author, tag, title, month);
    query.with(PageRequest.of(page, HACKER_NEWS_PAGEABLE_SIZE));
    query.with(Sort.by(Order.desc("createdAtI")));

    return mongoTemplate.find(query, ArticleEntity.class).stream().map(this::toModel)
        .toList();
  }

  private Query buildQuery(String author, String tag, String title, String month) {

    var searchMap = Map.of("author", author, "tags", tag, "title", title);
    var criteria = Criteria.where("hiddenAt").exists(false);

    for (Entry<String, String> keyValueEntry : searchMap.entrySet()) {
      if (StringUtils.isNotBlank(keyValueEntry.getValue())) {
        criteria = criteria.andOperator(
            buildStringSearchCriteria(keyValueEntry.getKey(), keyValueEntry.getValue()));
      }
    }

    if (StringUtils.isNotBlank(month)) {
      criteria = criteria.andOperator(buildMonthCriteria(month));
    }

    return new Query(criteria);
  }

  private Criteria buildStringSearchCriteria(String columnName, String value) {
    return Criteria.where(columnName).regex(value, "i");
  }

  private Criteria buildMonthCriteria(String monthName) {
    Month month = Month.valueOf(monthName.toUpperCase(Locale.ROOT));
    String searchMonthNumber = String.format("%02d", month.getValue());
    String regexPattern = "^\\d{4}-" + searchMonthNumber + "-.+$";

    return Criteria.where("createdAt").regex(regexPattern);
  }

  protected ArticleEntity toArticleEntity(HackerNewsArticle article) {
    return ArticleEntity.builder()
        .articleId(article.getObjectID())
        .title(article.getTitle())
        .url(article.getUrl())
        .author(article.getAuthor())
        .storyId(article.getStoryId())
        .title(Optional.ofNullable(article.getStoryTitle()).orElse(article.getTitle()))
        .commentText(article.getCommentText())
        .numComments(article.getNumComments())
        .points(article.getPoints())
        .createdAt(article.getCreatedAt())
        .createdAtI(article.getCreatedAtI())
        .updatedAt(article.getUpdatedAt())
        .tags(Arrays.asList(article.getTags()))
        .build();
  }

  protected Article toModel(ArticleEntity article) {
    return Article.builder()
        .articleId(article.getArticleId())
        .title(article.getTitle())
        .url(article.getUrl())
        .author(article.getAuthor())
        .storyId(article.getStoryId())
        .title(article.getTitle())
        .commentText(article.getCommentText())
        .numComments(article.getNumComments())
        .points(article.getPoints())
        .createdAt(article.getCreatedAt())
        .createdAtI(article.getCreatedAtI())
        .updatedAt(article.getUpdatedAt())
        .tags(article.getTags())
        .build();
  }
}
