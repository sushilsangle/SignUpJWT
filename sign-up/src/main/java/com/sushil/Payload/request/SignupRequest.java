package com.sushil.Payload.request;

import com.sushil.model.Role;

import java.util.HashSet;
import java.util.Set;

public class SignupRequest {


    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();

    public SignupRequest() {
    }

    public SignupRequest(String username, String email, String password, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
