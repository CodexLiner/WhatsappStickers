package com.CodingErgo.sticker.NewStickerManager.ImageErasing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.CodingErgo.sticker.MyStickerManager.MyStickerManager;
import com.CodingErgo.sticker.NewStickerManager.Views.CropImageClass;
import com.CodingErgo.sticker.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class EditStickers extends AppCompatActivity {
    Bitmap toSet , toCreate;
    Paint mPaint ;
    Path mPath;
    FileInputStream fs;
    BufferedInputStream bf;
    Canvas canvas;
    String URI;
    StickerEditorView CropedImage ;
    int xInt , yInt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stickers);
        CropedImage = findViewById(R.id.CropedImage);
        xInt = getIntent().getIntExtra("xInt",0);
        yInt = getIntent().getIntExtra("yInt", 0);
        int height , width;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        width = dm.widthPixels;
       try{
           fs = new FileInputStream(URI);
           bf= new BufferedInputStream(fs);
           toSet = BitmapFactory.decodeStream(bf);
       }catch (Exception e){ }
        toSet = MyStickerManager.getBitmap();
        toCreate = Bitmap.createBitmap(width , height, toSet.getConfig());
        canvas = new Canvas(toCreate);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
       for (int i = 0 ; i < CropImageClass.points.size() ; i++) {
           mPath.lineTo(CropImageClass.points.get(i).x ,CropImageClass.points.get(i).y);
       }
       canvas.drawPath(mPath , mPaint);
       mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
       // to Set On VIew
        //Todo: if size matter
        canvas.drawBitmap(toSet , xInt, yInt, mPaint);
//        CropedImage.setImageBitmap(toCreate);
        BitmapSizeReducer();
    }
    public void BitmapSizeReducer() {
        //Todo: if size matter
        int width = 1500;
        int height = 2100;
        Bitmap background = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);

        float originalWidth = toCreate.getWidth();
        float originalHeight = toCreate.getHeight();

        Canvas canvas = new Canvas(background);

        float scale = width / originalWidth;

        float xTranslation = 0.0f;
        float yTranslation = (height - originalHeight * scale) / 2.0f;

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(toCreate, transformation, paint);
        CropedImage.setImageBitmap(background);

        Log.d("TAG", "EdittoreBACJ: "+background.getHeight() +"  "+ background.getWidth());
    }
}