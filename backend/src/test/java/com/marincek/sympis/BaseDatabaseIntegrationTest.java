package com.marincek.sympis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;

public class BaseDatabaseIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("postgres_demo")
            .withUsername("postgres")
            .withPassword("secrect");

    @BeforeClass
    public static void init() throws IOException, InterruptedException {
        postgreSQLContainer.start();
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
    }

    @AfterClass
    public static void shutdown() {
        postgreSQLContainer.stop();
    }
}
