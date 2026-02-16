package com.blog;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer {

    public static final PostgreSQLContainer<?> INSTANCE =
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("talkto_test")
            .withUsername("test")
            .withPassword("test");

    static {
        INSTANCE.start();
    }

    private PostgresTestContainer() {}
}