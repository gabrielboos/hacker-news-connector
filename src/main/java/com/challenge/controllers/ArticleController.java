package com.challenge.controllers;

import com.challenge.model.Article;
import com.challenge.services.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Articles")
@RequestMapping("api/v1/articles")
public class ArticleController {

  private final ArticleService articleService;

  @GetMapping
  @PreAuthorize("hasAnyRole('VIEW_ARTICLES', 'ADMIN')")
  @Operation(
      summary = "Searches for articles based on the filters provided.",
      description = "Searches on all available articles based on the filters provided in a paginated response with maximum of 5 articles per request.",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Article[].class))),
          @ApiResponse(responseCode = "400", description = "Bad Request"),
          @ApiResponse(responseCode = "401", description = "Failed to validate token"),
          @ApiResponse(responseCode = "403", description = "User not permission to view articles"),
      }
  )
  public List<Article> get(
      @RequestParam(required = false, defaultValue = "") String author,
      @RequestParam(required = false, defaultValue = "") String tag,
      @RequestParam(required = false, defaultValue = "") String title,
      @RequestParam(required = false, defaultValue = "") String month,
      @RequestParam(required = false, defaultValue = "0") int page) {
    return articleService.searchArticles(author, tag, title, month, page);
  }

  @DeleteMapping
  @PreAuthorize("hasAnyRole('ADMIN')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
      summary = "Hides an article based on its ID.",
      description = "Sets the field 'hiddenAt' for the article found by the articleId param.",
      responses = {
          @ApiResponse(responseCode = "204", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad Request"),
          @ApiResponse(responseCode = "401", description = "Failed to validate token"),
          @ApiResponse(responseCode = "403", description = "User not permission to hide articles"),
          @ApiResponse(responseCode = "404", description = "Article with matching ID not found"),
      }
  )
  public void hideArticle(@RequestParam String articleId) {
    articleService.hideArticle(articleId);
  }

}
