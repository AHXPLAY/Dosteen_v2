package com.dahy.practice.ui;

import java.util.ArrayList;

public class Event {

    String previewUrl;
    String classIcon;
    ArrayList<String> picturesUrls;
    String name;
    String description;
    boolean isConstant;
    String beginDate;
    String endDate;
    String city;
    String site;
    String address;
    String contacts;


    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }


    public Event(String classIcon, String name, String city) {
        this.classIcon = classIcon;
        this.name = name;
        this.city = city;
    }


    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Event() {
    }

    public boolean getConstant() {
        return isConstant;
    }

    public void setConstant(boolean constant) {
        isConstant = constant;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
