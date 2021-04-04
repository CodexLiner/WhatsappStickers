package com.CodingErgo.sticker.NewStickerManager.ImageErasing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.CodingErgo.sticker.NewStickerManager.Gallary.GalleryView;
import com.CodingErgo.sticker.NewStickerManager.Views.CropImageClass;
import com.CodingErgo.sticker.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class CropImageTool extends AppCompatActivity {
    LinearLayout OnlineCrop , ManualCrop, FreeHandCrop;
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
                    FreeHandCrop.setBackgroundResource(R.drawable.state_enabled);
                    ManualCrop.setBackgroundResource(R.drawable.buttonshape2);
                    OnlineCrop.setBackgroundResource(R.drawable.buttonshape2);
                    ToastMaker("Manual Crop", R.drawable.ic_manual);
                }
            });
            ManualCrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cropImageClass.ButtonClick(true);
                    ManualCrop.setBackgroundResource(R.drawable.state_enabled);
                    FreeHandCrop.setBackgroundResource(R.drawable.buttonshape2);
                    OnlineCrop.setBackgroundResource(R.drawable.buttonshape2);
                    ToastMaker("Square Crop", R.drawable.ic_crop);
                }
            });
            OnlineCrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cropImageClass.ButtonClick(true);
                    OnlineCrop.setBackgroundResource(R.drawable.state_enabled);
                    FreeHandCrop.setBackgroundResource(R.drawable.buttonshape2);
                    ManualCrop.setBackgroundResource(R.drawable.buttonshape2);
                    ToastMaker("OnLine Crop", R.drawable.ic_add_fab);
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
    private void ToastMaker(String text , int uri){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_layout_file , (findViewById(R.id.Toast_shape_layout)));
        Toast toast = new Toast(cropImageClass.getContext());
        TextView t = view.findViewById(R.id.toast_text);
        ImageView imageView = view.findViewById(R.id.toast_icon);
        imageView.setImageDrawable(getResources().getDrawable(uri));
        t.setText(text);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,-200);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), GalleryView.class));
        overridePendingTransition(0,R.anim.out_right);
        finish();
    }
}