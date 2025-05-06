package com.example.xxxmes.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// 数据库工具类，连接数据库并执行查询操作
public class JDBCUtils {

    private static String driver = "com.mysql.jdbc.Driver"; // 5.1.2d版本驱动类名
    private static String dbName = "mydatabase"; // 数据库名称
    private static String user = "root"; // 这里需要替换为实际的用户名
        private static String password = "123456"; // 这里需要替换为实际的密码

    public static Connection getConn() {
        Connection connection = null;
        int maxRetries = 3; // 设置最大重试次数
        int retryCount = 0;
        long connectionTimeout = 5000; // 设置连接超时时间为5秒

        while (retryCount < maxRetries && connection == null) {
            try {
                Class.forName(driver);
                String ip = "192.168.211.173";

                System.out.println("正在尝试第 " + (retryCount + 1) + " 次建立数据库连接...");
                long startTime = System.currentTimeMillis();
                connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + dbName , user, password);
                long endTime = System.currentTimeMillis();
                System.out.println("数据库连接已成功建立，耗时 " + (endTime - startTime) + " 毫秒。");

                // 验证连接的自动提交模式是否正确
                boolean autoCommit = connection.getAutoCommit();
                boolean expectedAutoCommit = true; // 假设应用要求自动提交模式为开启
                if (autoCommit!= expectedAutoCommit) {
                    System.out.println("数据库连接的自动提交模式为 " + autoCommit + ", 与预期的 " + expectedAutoCommit + " 不一致。");
                }

                // 获取并验证数据库连接的字符集编码
                String actualCharset = getCharacterEncoding(connection);
                String expectedCharset = "utf8"; // 假设应用要求UTF-8字符集
                if (!actualCharset.equals(expectedCharset)) {
                    System.out.println("数据库连接的字符集设置为 " + actualCharset + ", 与预期的 " + expectedCharset + " 不一致。");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("第 " + (retryCount + 1) + " 次数据库连接建立失败: " + e.getMessage());
                if (retryCount < maxRetries - 1) {
                    System.out.println("将在 " + (retryCount + 1) * 1000 + " 毫秒后进行下一次重试。");
                    try {
                        Thread.sleep((retryCount + 1) * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            retryCount++;
        }

        if (connection == null) {
            System.out.println("经过 " + maxRetries + " 次重试后，仍无法建立数据库连接，程序可能无法正常运行。");
        }

        return connection;
    }

    private static String getCharacterEncoding(Connection connection) throws SQLException {
        String charset = null;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW VARIABLES LIKE 'character_set_connection'")) {
            if (rs.next()) {
                charset = rs.getString(2);
            }
        }
        return charset;
    }

    public static ResultSet executeQuery(String sql) {
        Connection conn = getConn();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt!= null) stmt.close();
                if (conn!= null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rs;
    }
}