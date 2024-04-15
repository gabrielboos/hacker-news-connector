package com.challenge.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class ArticlesSearchParams {

  String author;
  String title;
  String tag;
  String month;
  int page;

}
