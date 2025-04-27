package org.example;

import org.flywaydb.core.Flyway;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class DatabaseConfig {
    public static DataSource setupDatabase() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/weather_db");
        dataSource.setUser("postgres");
        dataSource.setPassword("sasha2004");

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migrations")
                .load();
        flyway.migrate();

        return dataSource;
    }
}
