package com.challenge.entities;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@Document("users")
public class UserEntity implements Serializable {

  @Id
  String username;

  String password;

  List<String> permissions;
}
