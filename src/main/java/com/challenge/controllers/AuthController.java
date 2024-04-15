package com.challenge.controllers;

import com.challenge.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication")
@RequestMapping("api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("login")
  @Operation(
      summary = "Authenticates the user's credentials and returns a JWT Token.",
      description = "Checks the login credentials and if they match with the one on our database, it will create a JWT Token for that user.",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
          @ApiResponse(responseCode = "400", description = "Bad Request"),
          @ApiResponse(responseCode = "401", description = "Failed to authenticate user credentials")
      }
  )
  public ResponseEntity<String> authenticateUser(@RequestHeader String username,
      @RequestHeader String password) {
    return ResponseEntity.ok(authService.authenticateUser(username, password));
  }

  @PostMapping("initial-user")
  @Operation(
      summary = "Creates the initial user for testing the application.",
      description = "Creates a user with the username 'admin' and password 'admin' with the role 'ADMIN'.",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
      }
  )
  public ResponseEntity<String> createInitialUser() {
    authService.createInitialUser();
    return ResponseEntity.ok("Created user 'admin' with password 'admin' and role 'ADMIN'");
  }

}
