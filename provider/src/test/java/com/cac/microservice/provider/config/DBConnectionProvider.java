package com.cac.microservice.provider.config;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectionProvider {
    @Value("${spring.datasource.url}")
    private final String url;
    @Value("${spring.datasource.username}")
    private final String username;
    @Value("${spring.datasource.password}")
    private final String password;

    public DBConnectionProvider(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
