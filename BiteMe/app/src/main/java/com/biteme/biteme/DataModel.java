package com.biteme.biteme;


public class DataModel {


    String name;
    String version;
    String id_;
    String image;

    public DataModel(String name, String version, String id_, String image) {
        this.name = name;
        this.version = version;
        this.id_ = id_;
        this.image=image;
    }


    public String getName() {
        return name;
    }


    public String getVersion() {
        return version;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id_;
    }
}