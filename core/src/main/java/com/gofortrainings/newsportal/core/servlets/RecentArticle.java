package com.gofortrainings.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/newsportal/service/users")
public class RecentArticle extends SlingAllMethodsServlet {

    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        ResourceResolver resolver = request.getResourceResolver();
        Resource resourcePath = resolver.getResource("/content/users");
        Resource resourceId = resolver.getResource("/content/users/" + userId);

        if (resourcePath != null) {
            if (resourceId == null) {
                Map<String, Object> mapProp = new HashMap<>();
                mapProp.put("userId", userId);
                mapProp.put("firstName", firstName);
                mapProp.put("lastName", lastName);
                mapProp.put("email", email);
                mapProp.put("password", hashPassword(password)); // Hash the password
                mapProp.put("phone", phone);

                resolver.create(resourcePath, userId, mapProp);
                resolver.commit();

                response.getWriter().write("User created successfully for ID: " + userId);
            } else {
                response.getWriter().write("This user ID already exists. Please log in or create an account with a different user ID.");
            }
        } else {
            response.getWriter().write("Error: Unable to access user storage location.");
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ... other methods remain the same ...
}
