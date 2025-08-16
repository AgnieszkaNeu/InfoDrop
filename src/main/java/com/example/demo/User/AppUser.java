package com.example.demo.User;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @ElementCollection
    @CollectionTable(name = "keywords", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "keyword")
    Set<String> keywords = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "categories", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "category")
    Set<String> categories = new HashSet<>();

    public AppUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AppUser() {}

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

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> preferences) {
        this.keywords = preferences;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }
}
