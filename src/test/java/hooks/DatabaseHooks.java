package hooks;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseHooks {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHooks.class);
    private static final String SEED_SQL_FILE = "src/test/resources/sql/seed.sql";
    private static final String CLEAN_SQL_FILE = "src/test/resources/sql/clean.sql";

    @BeforeAll
    public static void seedDatabase() {
        LOGGER.info("Starting database seeding from {}", SEED_SQL_FILE);
        executeSqlFromFile(SEED_SQL_FILE);
    }

    @AfterAll
    public static void cleanDatabase() {
        LOGGER.info("Cleaning up database after tests");
        executeSqlFromFile(CLEAN_SQL_FILE);
    }

    private static void executeSqlFromFile(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
             // fallback for running from different dir
             path = Paths.get("..", filePath);
             if (!Files.exists(path)) {
                 // Try to load from classpath if possible, but file system is easier for now given structure
                 // or try absolute path based on project root if available.
                 // let's try a direct approach relative to user workspace if failing.
                 LOGGER.error("SQL file not found: {}", filePath);
                 return;
             }
        }

        String sqlContent;
        try {
            sqlContent = new String(Files.readAllBytes(path));
        } catch (IOException e) {
             LOGGER.error("Failed to read SQL file: {}", path, e);
             return;
        }

        List<String> statements = splitSqlStatements(sqlContent);
        
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        String dbUrl = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("credentials.database.url");
        String dbUser = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("credentials.database.username");
        String dbPassword = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("credentials.database.password");

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            LOGGER.warn("Database credentials not found in configuration. Skipping database operation.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            for (String sql : statements) {
                if (sql.trim().isEmpty()) continue;
                
                try {
                    LOGGER.info("Executing SQL: {}", sql.trim());
                    stmt.execute(sql);
                } catch (SQLException e) {
                    LOGGER.error("Failed to execute SQL statement: {}", sql, e);
                    throw new RuntimeException("Database operation failed", e);
                }
            }
            LOGGER.info("Database operation completed successfully for {}", filePath);

        } catch (SQLException e) {
            LOGGER.error("Database connection failed", e);
            throw new RuntimeException("Could not connect to database", e);
        }
    }
    
    private static List<String> splitSqlStatements(String sqlScript) {
        // Remove comment lines (starting with --)
        String cleanScript = sqlScript.replaceAll("(?m)^\\s*--.*$", "");
        
        List<String> statements = new ArrayList<>();
        String[] bulkyStatements = cleanScript.split(";");
        for (String st : bulkyStatements) {
            String trimmed = st.trim();
            if (!trimmed.isEmpty()) {
                statements.add(trimmed);
            }
        }
        return statements;
    }
}
