package com.gofortrainings.newsportal.core.servlets;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class AuthenticationService {

    // Database configuration (for example, MySQL)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/users";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Dundu@003";

    public AuthenticationResult authenticate(String email, String password) throws AuthenticationException {
        AuthenticationResult authResult = null;

        // Query the database to check user credentials
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            // SQL query to find user based on email
            String sql = "SELECT user_id, email, password, roles FROM user WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Get data from the result set
                        String dbPassword = rs.getString("password");
                        if (dbPassword != null && dbPassword.equals(password)) {
                            // Passwords match, create the authentication result
                            String userId = rs.getString("user_id");
                            String userEmail = rs.getString("email");
                            List<String> userRoles = Arrays.asList(rs.getString("roles").split(","));

                            authResult = new AuthenticationResult(true, userId, userEmail, userRoles, null);
                        } else {
                            throw new AuthenticationException("Invalid credentials");
                        }
                    } else {
                        throw new AuthenticationException("Invalid credentials");
                    }
                }
            }
        } catch (SQLException e) {
            throw new AuthenticationException("Database error during authentication", e);
        }

        return authResult;
    }
}
