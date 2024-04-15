package com.challenge.repositories;

import com.challenge.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

  Optional<UserEntity> findByUsername(String username);

}
