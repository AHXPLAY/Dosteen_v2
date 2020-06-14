package com.dahy.practice.ui;

import java.util.ArrayList;

public class Event {

    String previewUrl;
    String classIcon;
    ArrayList<String> picturesUrls;
    String name;
    String description;
    boolean isConstant;
    String city;
    String adress;
    String site;

    public Event(String classIcon, String name, String city) {
        this.classIcon = classIcon;
        this.name = name;
        this.city = city;
    }

    public Event(String name) {
        this.name = name;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getClassIcon() {
        return classIcon;
    }

    public void setClassIcon(String classIcon) {
        this.classIcon = classIcon;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public ArrayList<String> getPicturesUrls() {
        return picturesUrls;
    }

    public void setPicturesUrls(ArrayList<String> picturesUrls) {
        this.picturesUrls = picturesUrls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Event() {
    }

    public boolean isConstant() {
        return isConstant;
    }

    public void setConstant(boolean constant) {
        isConstant = constant;
    }

    public Event(String previewUrl,
                 String classIcon,
                 ArrayList<String> picturesUrls,
                 String name, String description,
                 boolean isConstant, String city,
                 String adress, String site) {
        this.previewUrl = previewUrl;
        this.classIcon = classIcon;
        this.picturesUrls = picturesUrls;
        this.name = name;
        this.description = description;
        this.isConstant = isConstant;
        this.city = city;
        this.adress = adress;
        this.site = site;
    }
}
