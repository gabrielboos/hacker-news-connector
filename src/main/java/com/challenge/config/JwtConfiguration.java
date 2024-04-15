package com.challenge.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import java.time.Duration;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "server.jwt")
public class JwtConfiguration {

  private SecretKey jwtSecretKey;

  private String issuer;

  private JWSAlgorithm algorithm;

  @DurationMin(seconds = 1)
  private Duration expiresIn;

  public void setAlgorithm(String algorithm) {
    this.algorithm = JWSAlgorithm.parse(algorithm);
  }

  public void setJwtSecretKey(String key) {
    var jwk = new OctetSequenceKey.Builder(key.getBytes())
        .algorithm(algorithm)
        .build();

    this.jwtSecretKey = jwk.toSecretKey();
  }
}
