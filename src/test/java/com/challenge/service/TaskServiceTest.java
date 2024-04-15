package com.challenge.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.challenge.model.HackerNewsArticle;
import com.challenge.model.HackerNewsSearchResult;
import com.challenge.services.ArticleService;
import com.challenge.services.ExternalHackerNewsService;
import com.challenge.services.TaskService;
import com.challenge.utils.ServiceTestResources;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

class TaskServiceTest extends ServiceTestResources {

  @InjectMocks
  TaskService taskService;

  @Mock
  ArticleService articleService;

  @Mock
  ExternalHackerNewsService externalHackerNewsService;

  @Test
  void emptyArticleSyncTest() {

    when(externalHackerNewsService.retrieveArticles(anyString())).thenReturn(
        HackerNewsSearchResult.builder().hits(
            List.of()).build());

    taskService.synchronizeArticles();

    verify(externalHackerNewsService, atLeast(1)).retrieveArticles(anyString());
    verify(articleService, never()).saveArticle(any(HackerNewsArticle.class));
  }

  @Test
  void notEmptyArticleSyncTest() {

    when(externalHackerNewsService.retrieveArticles(anyString())).thenReturn(
        HackerNewsSearchResult.builder().hits(
            List.of(HackerNewsArticle.builder().build())).build());

    doAnswer((Answer<Void>) invocationOnMock -> null).when(articleService).saveArticle(any());

    taskService.synchronizeArticles();

    verify(externalHackerNewsService, atLeastOnce()).retrieveArticles(anyString());
    verify(articleService, atLeastOnce()).saveArticle(any(HackerNewsArticle.class));
  }

  @Test
  void unhappyArticleSyncTest() {

    when(externalHackerNewsService.retrieveArticles(anyString())).thenReturn(null);

    taskService.synchronizeArticles();

    when(externalHackerNewsService.retrieveArticles(anyString())).thenReturn(
        HackerNewsSearchResult.builder().hits(null).build());

    taskService.synchronizeArticles();

    verify(externalHackerNewsService, times(2)).retrieveArticles(anyString());
    verify(articleService, never()).saveArticle(any(HackerNewsArticle.class));
  }
}
