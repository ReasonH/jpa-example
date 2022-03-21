package com.example.transactionaltestintegration.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String comment;

    public Long getId() {
        return this.id;
    }

    public String getComment() {
        return this.comment;
    }

    public Post getPost() {
        return this.post;
    }

    public void setComment(String title) { this.comment = title; }

    public void setPost(Post post) { this.post = post; }

    public Comment() {
    }

    public Comment(String title, Post post) {
        this.comment = title;
        this.post = post;
    }

    @Override
    public String toString() {
        return getId() + " " + getComment();
    }
}
