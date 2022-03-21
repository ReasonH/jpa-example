package com.example.transactionaltestintegration.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public User getUser() { return this.user; }

    public void setTitle(String title) { this.title = title; }

    public void setUser(User user) { this.user = user; }

    public Post() {
    }

    public Post(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return getId() + " " + getTitle();
    }
}
