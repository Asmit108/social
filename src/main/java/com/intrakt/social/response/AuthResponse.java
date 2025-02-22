package com.intrakt.social.response;

public class AuthResponse {

    private String token;
    private String message;
    private String role;

    public AuthResponse(String token, String message, String role) {
        super();
        this.token = token;
        this.message = message;
        this.role = role;
    }

    public String getToken() {
        return token;
    }
    public String getMessage() {
        return message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
