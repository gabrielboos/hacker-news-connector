package com.challenge.services;

import com.challenge.model.HackerNewsSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalHackerNewsService {

  private final RestTemplate restTemplate;

  @Value(value = "${external_api.hacker_news.search_by_date}")
  private String searchByDateUrl;

  public HackerNewsSearchResult retrieveArticles(String textToSearch) {

    String url = UriComponentsBuilder.fromHttpUrl(searchByDateUrl).encode()
        .queryParam("query", textToSearch)
        .toUriString();

    try {
      return restTemplate.getForObject(url, HackerNewsSearchResult.class);
    } catch (Exception e) {
      return null;
    }
  }

}
