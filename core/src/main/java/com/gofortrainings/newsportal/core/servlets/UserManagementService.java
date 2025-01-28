package com.gofortrainings.newsportal.core.servlets;

import org.osgi.service.component.annotations.Component;

@Component(service = UserManagementService.class)
public class UserManagementService {

    public String getUserDetails(String userId) {
        // Logic to retrieve user details, for example, querying a database
        return "User details for " + userId;
    }
}
