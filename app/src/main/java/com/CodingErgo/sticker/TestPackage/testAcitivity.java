package com.CodingErgo.sticker.TestPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
     }
    public void ToastMaker(Context context , String text , int duration ){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_layout_file , (findViewById(R.id.Toast_shape_layout)));
        Toast toast = new Toast(context);
        TextView t = view.findViewById(R.id.toast_text);
        t.setText(text);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,-200);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}