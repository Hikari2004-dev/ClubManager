package com.clubmanager.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Utility
 * Quản lý kết nối đến MySQL database
 */
public class DBConnection {
    
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    
    static {
        try {
            Properties props = new Properties();
            InputStream input = DBConnection.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            
            if (input != null) {
                props.load(input);
                driver = props.getProperty("db.driver");
                url = props.getProperty("db.url");
                username = props.getProperty("db.username");
                password = props.getProperty("db.password");
                
                // Load driver
                Class.forName(driver);
            } else {
                throw new RuntimeException("Không tìm thấy file db.properties");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đọc cấu hình database: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lấy một kết nối mới đến database
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    
    /**
     * Đóng kết nối an toàn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Đóng tất cả resources
     */
    public static void closeAll(Connection conn, java.sql.Statement stmt, java.sql.ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        closeConnection(conn);
    }
    
    /**
     * Kiểm tra kết nối database
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
