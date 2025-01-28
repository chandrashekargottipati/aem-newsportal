package com.gofortrainings.newsportal.core.servlets;

import java.util.List;

public class AuthenticationResult {

    private final boolean authenticated;
    private final String userId;
    private final String email;
    private final List<String> userRoles;
    private final FailureReason failureReason;

    public AuthenticationResult(boolean authenticated, String userId, String email, List<String> userRoles, FailureReason failureReason) {
        this.authenticated = authenticated;
        this.userId = userId;
        this.email = email;
        this.userRoles = userRoles;
        this.failureReason = failureReason;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    public enum FailureReason {
        INVALID_CREDENTIALS,
        ACCOUNT_LOCKED,
        ACCOUNT_INACTIVE
    }
}
