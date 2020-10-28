package com.roy.shayari.model;

public class Text {
    private String story;
    private String image;

    public Text() {
        //Empty contractor needed
    }

    public Text(String story, String image) {
        this.story = story;
        this.image = image;
    }

    public String getStory() {
        return story;
    }

    public String getImage() {
        return image;
    }
}
