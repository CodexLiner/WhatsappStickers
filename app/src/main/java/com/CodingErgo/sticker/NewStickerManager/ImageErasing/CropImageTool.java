package com.CodingErgo.sticker.NewStickerManager.ImageErasing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.CodingErgo.sticker.NewStickerManager.Views.CropImageClass;
import com.CodingErgo.sticker.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class CropImageTool extends AppCompatActivity {
    ImageView  OnlineCrop , ManualCrop, FreeHandCrop;
    CropImageClass cropImageClass ;
    BufferedInputStream bf ;
    FileInputStream inputStream;
   static Bitmap bitmap , ret;

    String URI ;
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image_tool);
        URI = getIntent().getStringExtra("URI");
        OnlineCrop = findViewById(R.id.OnlineCrop);
        ManualCrop = findViewById(R.id.ManualCrop);
        FreeHandCrop = findViewById(R.id.FreeHandCrop);
        cropImageClass = findViewById(R.id.CropImage);
        ButtonManager();

        try{

            inputStream = new FileInputStream(URI);
            bf = new BufferedInputStream(inputStream);
            bitmap = BitmapFactory.decodeStream(bf);

        }catch (Exception ignored){ }

        BitmapSizeReducer();
    }

    private void ButtonManager() {
            FreeHandCrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cropImageClass.ButtonClick(false);
                }
            });
            ManualCrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cropImageClass.ButtonClick(true);
                }
            });
    }

    public void BitmapSizeReducer() {

            int width = 1024;
            int height = 1920;

        Bitmap background = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);

        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();

        Canvas canvas = new Canvas(background);

        float scale = width / originalWidth;

        float xTranslation = 0.0f;
        float yTranslation = (height - originalHeight * scale) / 2.0f;

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(bitmap, transformation, paint);
        cropImageClass.setMainBitmap(background);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cropImageClass.invalidate();
    }
}