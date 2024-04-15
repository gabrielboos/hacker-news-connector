package com.challenge.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.commands.ServerAddress;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.ImmutableMongod;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.embed.mongo.types.DatabaseDir;
import de.flapdoodle.reverse.TransitionWalker.ReachedState;
import de.flapdoodle.reverse.transitions.Start;
import java.io.Closeable;
import java.io.File;
import java.nio.file.Files;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

public class EmbeddedMongoDb implements Closeable {

  private static final String CONNECTION_STRING = "mongodb://%s:%d";
  private static ReachedState<RunningMongodProcess> mongod;
  private static MongoClient client;
  private static MongoTemplate mongoTemplate;

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {

    if (mongoTemplate == null) {

      int port = 17027;

      String tmpdir = System.getProperty("java.io.tmpdir");
      File databaseDir = new File(tmpdir, "database");
      if (!databaseDir.exists()) {
        Files.createDirectory(databaseDir.toPath());
      }

      ImmutableMongod mongodWithoutAuth = Mongod.builder()
          .net(Start.to(Net.class).initializedWith(Net.defaults().withPort(port)))
          .databaseDir(
              Start.to(DatabaseDir.class).initializedWith(DatabaseDir.of(databaseDir.toPath())))
          .build();

      mongod = mongodWithoutAuth.start(Version.Main.V6_0);
      ServerAddress address = mongod.current().getServerAddress();
      client = MongoClients.create(
          String.format(CONNECTION_STRING, address.getHost(), address.getPort()));

      mongoTemplate = new MongoTemplate(client, "test");
    }

    return mongoTemplate;
  }

  @Override
  public void close() {
    if (mongod.current().isAlive()) {
      client.close();
      mongod.current().stop();
      mongoTemplate = null;
    }
  }
}