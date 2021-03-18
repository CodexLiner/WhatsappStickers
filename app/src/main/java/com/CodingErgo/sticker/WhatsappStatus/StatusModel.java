package com.CodingErgo.sticker.WhatsappStatus;

import android.net.Uri;

public class StatusModel {
    String path , filename , name ;
    Uri uri ;

    public StatusModel(String path, String filename, String name, Uri uri) {
        this.path = path;
        this.filename = filename;
        this.name = name;
        this.uri = uri;
    }

    public StatusModel() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
