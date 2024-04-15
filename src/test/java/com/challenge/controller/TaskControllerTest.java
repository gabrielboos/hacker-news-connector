package com.challenge.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challenge.model.HackerNewsSearchResult;
import com.challenge.services.ExternalHackerNewsService;
import com.challenge.utils.ControllerTestResources;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class TaskControllerTest extends ControllerTestResources {

  @MockBean
  ExternalHackerNewsService externalHackerNewsService;

  @BeforeEach
  void beforeEach() {
    when(externalHackerNewsService.retrieveArticles(anyString())).thenReturn(
        HackerNewsSearchResult.builder().hits(
            List.of()).build());
  }

  @Test
  @DisplayName("Makes sure that an admin can retrieve articles.")
  void happySyncTaskExecution() throws Exception {
    var token = createDefaultAdminAuthToken();

    this.mockedMvc.perform(post("/api/v1/task/sync-articles")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Makes sure that an user without proper permissions is denied.")
  void unhappySyncTaskExecution() throws Exception {
    var token = createDefaultAuthToken();

    this.mockedMvc.perform(post("/api/v1/task/sync-articles")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }
}

