package com.dahy.practice.ui;

import java.util.ArrayList;
import java.util.List;

public class User {

    String name;
    String ahivements;
    String imageUrl = "";
    List<Event> listOfEvents;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getListOfEvents() {
        return listOfEvents;
    }

    public void setListOfEvents(List<Event> listOfEvents) {
        this.listOfEvents = listOfEvents;
    }

    public String getAhivements() {
        return ahivements;
    }

    public void setAhivements(String ahivements) {
        this.ahivements = ahivements;
    }


    public User() {
    }
}
