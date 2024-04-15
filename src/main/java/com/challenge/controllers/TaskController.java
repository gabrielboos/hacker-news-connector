package com.challenge.controllers;

import com.challenge.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Task")
@RequestMapping("api/v1/task")
public class TaskController {

  private final TaskService taskService;

  @PostMapping("sync-articles")
  @PreAuthorize("hasAnyRole('ADMIN')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
      summary = "Executes a manual articles synchronization.",
      description = "Runs the Hacker News API articles synchronization manually.",
      responses = {
          @ApiResponse(responseCode = "204", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad Request"),
          @ApiResponse(responseCode = "401", description = "Failed to validate token"),
          @ApiResponse(responseCode = "403", description = "User not permission to execute this task"),
      }
  )
  public void executeArticlesSync() {
    taskService.synchronizeArticles();
  }

}
