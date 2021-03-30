package com.CodingErgo.sticker.TestPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.CodingErgo.sticker.MyStickerManager.MyStickerManager;
import com.CodingErgo.sticker.R;
import com.CodingErgo.sticker.StickerManager.StickerPack;

import java.util.ArrayList;

public class testAcitivity extends AppCompatActivity {
    boolean RESULT;
    ArrayList<StickerPack> stickerPacks ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_acitivity);
        stickerPacks = MyStickerManager.StickerLoader(this);
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("ArrayList",stickerPacks);
        setResult(RESULT_OK , intent);
        finish();
     }
}