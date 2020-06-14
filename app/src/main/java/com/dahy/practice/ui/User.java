package com.dahy.practice.ui;

import java.util.ArrayList;
import java.util.List;

public class User {

    String name;
    String contacts;
    String ahivements;
    String imageUrl = "";
    List<Event> listOfEvents;

    public User(String name, String contacts, String ahivements, String imageUrl, List<Event> listOfEvents) {
        this.name = name;
        this.contacts = contacts;
        this.ahivements = ahivements;
        this.imageUrl = imageUrl;
        this.listOfEvents = listOfEvents;
    }

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

    public User(String name, String contacts, String ahivements, List<Event> listOfEvents) {
        this.name = name;
        this.contacts = contacts;
        this.ahivements = ahivements;
        this.listOfEvents = listOfEvents;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
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
