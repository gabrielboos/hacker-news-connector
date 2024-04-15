package com.challenge.utils;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The default test resources for service / logic / business validation. This will have a in-memory
 * MongoDB.
 */
@DataMongoTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ServiceTestResources {

}
