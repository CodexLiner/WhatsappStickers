package com.CodingErgo.sticker.HomePackage;

import java.util.ArrayList;

public class itemsModel {
    ArrayList<?> stickers;
    String identifier,name,publisher,tray_image_file;
    boolean animated_sticker_pack;

    public ArrayList<?> getStickers() {
        return stickers;
    }

    public void setStickers(ArrayList<?> stickers) {
        this.stickers = stickers;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTray_image_file() {
        return tray_image_file;
    }

    public void setTray_image_file(String tray_image_file) {
        this.tray_image_file = tray_image_file;
    }

    public boolean isAnimated_sticker_pack() {
        return animated_sticker_pack;
    }

    public void setAnimated_sticker_pack(boolean animated_sticker_pack) {
        this.animated_sticker_pack = animated_sticker_pack;
    }

    public itemsModel() {
    }

    public itemsModel(ArrayList<?> stickers, String identifier, String name, String publisher, String tray_image_file, boolean animated_sticker_pack) {
        this.stickers = stickers;
        this.identifier = identifier;
        this.name = name;
        this.publisher = publisher;
        this.tray_image_file = tray_image_file;
        this.animated_sticker_pack = animated_sticker_pack;
    }
}
