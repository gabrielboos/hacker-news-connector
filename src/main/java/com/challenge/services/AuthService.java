package com.challenge.services;

import com.challenge.entities.UserEntity;
import com.challenge.repositories.UserRepository;
import com.challenge.utils.AppConstants;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public String authenticateUser(String username, String password) {

    Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

    if (optionalUser.isEmpty() || !passwordEncoder.matches(password,
        optionalUser.get().getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
          "Username or password is incorrect.");
    }

    return jwtService.generateJWT(username,
        Map.of(
            AppConstants.JWT_SCOPE_CLAIM, optionalUser.get().getPermissions(),
            AppConstants.JWT_USER_CLAIM, optionalUser.get().getUsername())
    );
  }

  public void createInitialUser() {
    userRepository.save(UserEntity.builder()
        .username("admin")
        .password(passwordEncoder.encode("admin"))
        .permissions(List.of("ADMIN"))
        .build());
  }

}
