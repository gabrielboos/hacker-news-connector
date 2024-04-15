package com.challenge.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@Document("articles")
public class ArticleEntity implements Serializable {

  @Id
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
  private Instant lastSyncAt;

  private Instant hiddenAt;

  private List<String> tags;

}
