package com.cybermall.backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ViewHistory> viewHistories = new HashSet<>();

    private boolean isNewUser;

    // default constructor for JPA
    public User() {}

    public User(String username) {
        this.username = username;
        this.isNewUser = true;
        this.viewHistories = new HashSet<>();
    }

    public Long getUserId() {
        return this.id;
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

    public boolean getIsNewUser() {
        return this.isNewUser;
    }

    public void setIsNewUser(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }
}
