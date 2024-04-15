# Hacker News Connector

This project is a Java application that connects to the Hacker News API and retrieves the top
stories.

## Requirements

### Local application setup

- Java 21
- Maven 3.9.2+

If you are using Intellij, you will find a run configuration called `run-local`, which you can use
to run the application without the database attached to it.

### Docker application setup

- Docker
- Docker-compose

If you are using docker, all you have to do is run `mvn clean package` and
then `docker-compose up -d --build`.

### Initial user creation

For the first admin user, you have to execute the POST endpoint located at `/api/v1/user/initial-user`. It has no authentication, and it will create the first user with the role `ADMIN`.

## Hacker News Synchronization

The application will synchronize the top stories from Hacker News when the application is first
deployed and then every 60 minutes.
You can change this value by modifying the `ARTICLE_SYNC_INTERVAL` constant, which can be found
in `AppConstants.java`.

You can also manually synchronize the articles by calling the `/api/v1/task/sync-articles` endpoint.