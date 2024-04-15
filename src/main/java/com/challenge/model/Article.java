package com.challenge.model;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Article implements Serializable {

  private String articleId;

  private String author;
  private String title;
  private String url;
  private String commentText;
  private int createdAtI;
  private int numComments;
  private int points;
  private int storyId;
  private String updatedAt;
  private String createdAt;

  private List<String> tags;

}
