package com.CodingErgo.sticker.HomePackage;

public class testModel {
    String identifier , name , url;

    public testModel() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public testModel(String identifier, String name, String url) {
        this.identifier = identifier;
        this.name = name;
        this.url = url;
    }
}
