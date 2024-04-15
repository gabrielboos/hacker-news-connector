package com.challenge.utils;

import static com.challenge.utils.AppConstants.ADMIN_PERMISSION;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challenge.Application;
import com.challenge.entities.UserEntity;
import com.challenge.repositories.UserRepository;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The default test resources for controller / mvc tests. This will not have an embedded MongoDB.
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = Application.class
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ControllerTestResources {

  @Autowired
  public MockMvc mockedMvc;

  @MockBean
  public UserRepository userRepository;

  @Autowired
  public PasswordEncoder passwordEncoder;

  public final String defaultAuthUsername = "admin";
  public final String defaultAuthPassword = "testpass";

  @BeforeEach
  void defaultBeforeEach() {
  }

  public String createDefaultAdminAuthToken() throws Exception {
    return createDefaultAuthToken(ADMIN_PERMISSION);
  }

  public String createDefaultAuthToken(String... permissions) throws Exception {
    when(userRepository.findByUsername(defaultAuthUsername)).thenReturn(
        Optional.of(UserEntity.builder()
            .username(defaultAuthUsername)
            .password(passwordEncoder.encode(defaultAuthPassword))
            .permissions(Arrays.asList(permissions))
            .build()));

    return this.mockedMvc.perform(post("/api/v1/auth/login")
            .header("username", defaultAuthUsername)
            .header("password", defaultAuthPassword))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
  }

}
