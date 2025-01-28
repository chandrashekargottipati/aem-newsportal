package com.gofortrainings.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

@Component(service = Servlet.class, immediate = true, property = {
        "sling.servlet.methods=POST",
        "sling.servlet.paths=/bin/authentication/login"
})
public class AuthenticationServiceServlet extends SlingAllMethodsServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/users";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Dundu@003";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        AuthenticationResult authResult = null;
        try {
            authResult = authenticate(email, password);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

        if (authResult.isAuthenticated()) {
            response.getWriter().write("Login successful");
            // You could return additional user info or a redirect URL
        } else {
            response.getWriter().write("Invalid username or password");
        }
    }

    public AuthenticationResult authenticate(String email, String password) throws AuthenticationException {
        AuthenticationResult authResult = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT user_id, email, password, roles FROM user WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String dbPassword = rs.getString("password");
                        if (dbPassword != null && dbPassword.equals(password)) {
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
