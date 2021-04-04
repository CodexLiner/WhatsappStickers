package com.CodingErgo.sticker.NewStickerManager.EnterText;

public class TextModel {
    String textName;

    public String getTextName() {
        return textName;
    }

    public TextModel() {
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public TextModel(String textName) {
        this.textName = textName;
    }

    @Override
    public String toString() {
        return "TextModel{" +
                "textName='" + textName + '\'' +
                '}';
    }
}
