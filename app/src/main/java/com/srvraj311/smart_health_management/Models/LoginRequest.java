package com.srvraj311.smart_health_management.Models;

public class LoginRequest {
    private String email;
    private String password;
    private String device_id;

    public LoginRequest(String email, String password, String device_id) {
        this.email = email;
        this.password = password;
        this.device_id = device_id;
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.device_id = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
