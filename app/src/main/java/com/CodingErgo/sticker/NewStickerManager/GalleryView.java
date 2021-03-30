package com.CodingErgo.sticker.NewStickerManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.CodingErgo.sticker.R;

import java.util.List;

import static android.content.ContentValues.TAG;

public class GalleryView extends AppCompatActivity {
    GalleryAdapter galleryAdapter;
    List<String> stringList ;
    RecyclerView GalleryRecView ;
    ImageView GalleryCanc ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);
        GalleryRecView = findViewById(R.id.GalleryRecView);
        GalleryCanc = findViewById(R.id.GalleryCanc);
        GalleryRecView.setHasFixedSize(true);
        GalleryRecView.setLayoutManager(new GridLayoutManager(this, 4));
        stringList = GalleryContentProvider.getImages(this);
        galleryAdapter = new GalleryAdapter(this, stringList);
        GalleryRecView.setAdapter(galleryAdapter);
        GalleryCanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);
    }
}