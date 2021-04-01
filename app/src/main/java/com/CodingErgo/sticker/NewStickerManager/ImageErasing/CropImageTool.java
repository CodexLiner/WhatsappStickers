package com.CodingErgo.sticker.NewStickerManager.ImageErasing;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.CodingErgo.sticker.R;
import com.CodingErgo.sticker.view.CropView;

public class CropImageTool extends AppCompatActivity {
    ImageView  OnlineCrop , ManualCrop, FreeHandCrop;
    CropView CropImage;
    String URI ;
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image_tool);
        URI = getIntent().getStringExtra("URI");
        CropImage = findViewById(R.id.CropImage);
        CropImage.setImageUri(Uri.parse(URI));
//        CropImage.setImageURI(Uri.parse(URI));
        //Buttons
        OnlineCrop = findViewById(R.id.OnlineCrop);
        ManualCrop = findViewById(R.id.ManualCrop);
        FreeHandCrop = findViewById(R.id.FreeHandCrop);

    }
}