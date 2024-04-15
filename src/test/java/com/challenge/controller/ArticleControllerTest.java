package com.challenge.controller;

import static com.challenge.utils.AppConstants.VIEW_ARTICLES_PERMISSION;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challenge.services.ArticleService;
import com.challenge.utils.ControllerTestResources;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;

class ArticleControllerTest extends ControllerTestResources {

  @MockBean
  public ArticleService articleService;

  @Test
  @DisplayName("Makes sure that an admin can retrieve articles.")
  void happyAdminRetrieveArticles() throws Exception {
    var token = createDefaultAdminAuthToken();

    this.mockedMvc.perform(get("/api/v1/articles")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Makes sure that an user with the proper permission can retrieve articles.")
  void happyArticlesRetrieveArticles() throws Exception {
    var token = createDefaultAuthToken(VIEW_ARTICLES_PERMISSION);

    this.mockedMvc.perform(get("/api/v1/articles")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Makes sure that an user without proper permissions is denied.")
  void unhappyArticlesRetrieveArticles() throws Exception {
    var token = createDefaultAuthToken();

    this.mockedMvc.perform(get("/api/v1/articles")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Makes sure that an admin can hide the article.")
  void happyHideArticle() throws Exception {
    var token = createDefaultAdminAuthToken();
    var articleId = "articleId";

    doAnswer((Answer<Void>) invocationOnMock -> null).when(articleService).hideArticle(articleId);

    this.mockedMvc.perform(delete("/api/v1/articles")
            .header("Authorization", "Bearer " + token)
            .param("articleId", articleId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Makes sure that users without permissions cannot hide articles.")
  void unhappyHideArticle() throws Exception {
    var token = createDefaultAuthToken();
    var articleId = "articleId";

    this.mockedMvc.perform(delete("/api/v1/articles")
            .header("Authorization", "Bearer " + token)
            .param("articleId", articleId))
        .andExpect(status().isForbidden());
  }
}
