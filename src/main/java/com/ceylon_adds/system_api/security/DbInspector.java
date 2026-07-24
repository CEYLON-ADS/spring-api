package com.ceylon_adds.system_api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public class DbInspector implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DbInspector.class);
    private final JdbcTemplate jdbcTemplate;

    public DbInspector(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== INSPECTING DATABASE SCHEMA ===");
        inspectTable("application_user");
        inspectTable("application_user_role");
        inspectTable("otp");

        logger.info("=== CLEANING UP DATABASE COLUMNS ===");
        try {
            // Drop redundant columns created by incorrect Hibernate mappings
            jdbcTemplate.execute("ALTER TABLE application_user DROP COLUMN user_id");
            logger.info("Dropped application_user.user_id successfully.");
        } catch (Exception e) {
            logger.warn("Could not drop application_user.user_id: {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("ALTER TABLE application_user_role DROP COLUMN role_id");
            logger.info("Dropped application_user_role.role_id successfully.");
        } catch (Exception e) {
            logger.warn("Could not drop application_user_role.role_id: {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("ALTER TABLE otp DROP COLUMN otp_id");
            logger.info("Dropped otp.otp_id successfully.");
        } catch (Exception e) {
            logger.warn("Could not drop otp.otp_id: {}", e.getMessage());
        }
        
        logger.info("=== END OF DATABASE CLEANUP ===");
    }

    private void inspectTable(String tableName) {
        try {
            logger.info("--- Table: {} ---", tableName);
            List<Map<String, Object>> columns = jdbcTemplate.queryForList("DESCRIBE " + tableName);
            for (Map<String, Object> col : columns) {
                logger.info("Column: {} | Type: {} | Null: {} | Key: {} | Default: {} | Extra: {}",
                        col.get("Field"), col.get("Type"), col.get("Null"), col.get("Key"), col.get("Default"), col.get("Extra"));
            }
        } catch (Exception e) {
            logger.error("Failed to inspect table " + tableName, e);
        }
    }
}
