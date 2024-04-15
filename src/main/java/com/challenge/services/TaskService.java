package com.challenge.services;

import com.challenge.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

  private final ExternalHackerNewsService externalHackerNewsService;
  private final ArticleService articleService;

  @Scheduled(fixedDelay = AppConstants.ARTICLE_SYNC_INTERVAL)
  public void synchronizeArticlesTask() {
    log.info("Schedule articles sync executing.");
    synchronizeArticles();
  }

  public void synchronizeArticles() {
    log.info("Updating hacker news articles for default query '{}'.", AppConstants.HACKER_NEWS_DEFAULT_QUERY);
    var result = externalHackerNewsService.retrieveArticles(AppConstants.HACKER_NEWS_DEFAULT_QUERY);

    if (result == null || result.getHits() == null) {
      log.error("Failed to retrieve articles for default query '{}'.", AppConstants.HACKER_NEWS_DEFAULT_QUERY);
      return;
    }

    result.getHits().forEach(articleService::saveArticle);

    log.info("Updated {} articles for query '{}'.", result.getHits().size(),
        AppConstants.HACKER_NEWS_DEFAULT_QUERY);
  }

}
