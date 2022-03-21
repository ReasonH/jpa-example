package com.example.transactionaltestintegration.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "user")
    private List<Post> postList;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) { this.name = name; }

    public List<Post> getPostList() {
        return this.postList;
    }

    public User() {
    }

    public User(String name) {
        this.name = name;
    }
}
