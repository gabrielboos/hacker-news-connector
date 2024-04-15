package com.challenge.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challenge.entities.UserEntity;
import com.challenge.utils.ControllerTestResources;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@AutoConfigureMockMvc(addFilters = false) // for unauthenticated requests
class AuthControllerTest extends ControllerTestResources {

  private final String username = "admin";
  private final String password = "testpass";
  private final String password2 = "notcorrect";

  @Test
  @DisplayName("Makes sure that the user can login successfully if the credentials are correct.")
  void happyAuthorizeUser() throws Exception {

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(UserEntity.builder()
        .username(username)
        .password(passwordEncoder.encode(password))
        .permissions(List.of("ADMIN"))
        .build()));

    this.mockedMvc.perform(post("/api/v1/auth/login")
            .header("username", username)
            .header("password", password))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Makes sure we return the proper response if the user has mismatched credentials")
  void unhappyAuthorizeUser() throws Exception {

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(UserEntity.builder()
        .username(username)
        .password(passwordEncoder.encode(password))
        .permissions(List.of("ADMIN"))
        .build()));

    this.mockedMvc.perform(post("/api/v1/auth/login")
            .header("username", username)
            .header("password", password2))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Makes sure we return the proper response if the user is not found")
  void unhappyUserDoesntExist() throws Exception {
    this.mockedMvc.perform(post("/api/v1/auth/login")
            .header("username", username)
            .header("password", password))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Makes sure we can create the initial user successfully.")
  void happyCreateInitialUser() throws Exception {
    var responseBody = this.mockedMvc.perform(post("/api/v1/auth/initial-user"))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    Assertions.assertEquals("Created user 'admin' with password 'admin' and role 'ADMIN'", responseBody);
  }
}
