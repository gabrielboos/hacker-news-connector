package com.challenge.utils;

public class AppConstants {

  private AppConstants() {
  }

  public static final String ADMIN_PERMISSION = "ADMIN";

  public static final String VIEW_ARTICLES_PERMISSION = "VIEW_ARTICLES";

  public static final String JWT_SCOPE_CLAIM = "scope";

  public static final String JWT_USER_CLAIM = "user";

  public static final String HACKER_NEWS_DEFAULT_QUERY = "java";

  public static final int HACKER_NEWS_PAGEABLE_SIZE = 5;

  public static final long ARTICLE_SYNC_INTERVAL = 1000 * 60 * 60; //Millis * seconds * minutes
}
