package com.clubmanager.util;

/**
 * Utility class cho password (không mã hóa - plain text)
 */
public class PasswordUtil {
    
    /**
     * Trả về password nguyên bản (không mã hóa)
     */
    public static String hashPassword(String plainPassword) {
        return plainPassword;
    }
    
    /**
     * So sánh password trực tiếp (plain text)
     */
    public static boolean checkPassword(String plainPassword, String storedPassword) {
        if (plainPassword == null || storedPassword == null) {
            return false;
        }
        return plainPassword.equals(storedPassword);
    }
}
