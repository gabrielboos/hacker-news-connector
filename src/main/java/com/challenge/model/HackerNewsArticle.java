package com.challenge.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class HackerNewsArticle {

  private String author;
  @JsonAlias("created_at")
  private String createdAt;
  @JsonAlias("comment_text")
  private String commentText;
  @JsonAlias("story_title")
  private String storyTitle;
  @JsonAlias("num_comments")
  private int numComments;
  @JsonAlias("object_id")
  private String objectID;
  private int points;
  @JsonAlias("story_id")
  private int storyId;
  private String title;
  @JsonAlias("created_at_i")
  private int createdAtI;
  @JsonAlias("updated_at")
  private String updatedAt;
  @JsonAlias("url")
  private String url;

  @JsonAlias("_tags")
  private String[] tags;
}
