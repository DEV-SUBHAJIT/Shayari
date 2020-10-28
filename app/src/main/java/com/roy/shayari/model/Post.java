package com.roy.shayari.model;

public class Post {
    private String post;
    private String name;

    public Post() {
        //Empty contractor needed
    }

    public Post(String post, String name) {
        this.post = post;
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public String getName() {
        return name;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setName(String name) {
        this.name = name;
    }
}
