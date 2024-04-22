package com.cybermall.backend.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user in the system.
 */
public class User {

    private Long id;
    private String username;

    private Set<ViewHistory> viewHistories = new HashSet<>();

    public User() {}

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
        this.viewHistories = new HashSet<>();
    }

    public Long getUserId() {
        return this.id;
    }

    public void setUserId(Long id) {
        this.id = id;
    }
    
    public String getUserName() {
        return this.username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public Set<ViewHistory> getViewHistories() {
        return viewHistories;
    }

    public void setViewHistories(Set<ViewHistory> viewHistories) {
        this.viewHistories = viewHistories;
    }
}
