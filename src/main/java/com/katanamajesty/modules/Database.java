package com.katanamajesty.modules;

import com.katanamajesty.Main;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_TYPE = Config.getString("database_type");
    private static final String DB_HOST = Config.getString("database_host");
    private static final String DB_PORT = Config.getString("database_port");
    private static final String DB_NAME = Config.getString("database_name");
    private static final String DB_USER = Config.getString("database_user");
    private static final String DB_PASS = Config.getString("database_pass");
    private static final String URL = String.format("jdbc:%s://%s:%s/%s", DB_TYPE, DB_HOST, DB_PORT, DB_NAME);
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;

    static {
        config.setJdbcUrl(URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASS);
        config.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        config.addDataSourceProperty("connectionTimeout", "15000");
        config.addDataSourceProperty("useSSL", false);
        dataSource = new HikariDataSource(config);
    }

    /**
     * Метод для получения статического объекта соединения с бд
     * @return возвращает соединения с базой данных
     */
    @SneakyThrows
    public static Connection getConnection() {
        return dataSource.getConnection();
    }

    @SneakyThrows
    public static void closeConnection() {
        dataSource.getConnection().close();
        dataSource.close();
    }

    /**
     * Данный метод инициализирует таблицу для хранения привязанных игроков.
     * Следуя из этого он спокойно может быть статическим.
     * Крайне важно вызывать его перед инициализацией Дискорд бота,
     * поскольку бот не сможет вносить данные в бд, пока таблица для хранения не была инициализирована
     */
    public static void initTables() {
        try (Connection connection = Database.getConnection()) {
            final String QUERY = String.format(
                    "CREATE TABLE IF NOT EXISTS %s (gw2token VARCHAR(72), discord_id DECIMAL(18,0));",
                    Main.TABLE_NAME);
            Statement statement = connection.createStatement();
            statement.execute(QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
