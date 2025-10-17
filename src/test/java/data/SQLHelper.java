package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.password");

    @SneakyThrows
    public static String getPaymentStatus() {
        var conn = getConnection();
        var sql = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        return runner.query(conn, sql, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getCreditPaymentStatus() {
        var conn = getConnection();
        var sql = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return runner.query(conn, sql, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getDebitPaymentId() {
        var conn = getConnection();
        var sql = "SELECT transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1";
        return runner.query(conn, sql, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getCreditPaymentId() {
        var conn = getConnection();
        var sql = "SELECT credit_id FROM order_entity ORDER BY created DESC LIMIT 1";
        return runner.query(conn, sql, new ScalarHandler<>());
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var conn = getConnection();
        runner.execute(conn, "DELETE FROM payment_entity");
        runner.execute(conn, "DELETE FROM order_entity");
    }

    @SneakyThrows
    private static Connection getConnection() {
        return DriverManager.getConnection(url, user, password);
    }
}