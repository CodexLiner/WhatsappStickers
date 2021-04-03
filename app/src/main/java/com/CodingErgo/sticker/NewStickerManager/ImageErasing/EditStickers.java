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
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.CodingErgo.sticker.MyStickerManager.MyStickerManager;
import com.CodingErgo.sticker.NewStickerManager.Views.CropImageClass;
import com.CodingErgo.sticker.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import static android.content.ContentValues.TAG;

public class EditStickers extends AppCompatActivity {
    Bitmap toSet , toCreate;
    Paint mPaint ;
    Path mPath;
    RectF mRect;
    FileInputStream fs;
    BufferedInputStream bf;
    Canvas canvas;
    String URI;
    boolean crop;
    StickerEditorView CropedImage ;
    int xInt , yInt , parceX , parceY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stickers);
        CropedImage = findViewById(R.id.CropedImage);
        xInt = getIntent().getIntExtra("xInt",0);
        yInt = getIntent().getIntExtra("yInt", 0);
        parceX = getIntent().getIntExtra("parceX",0);
        parceY = getIntent().getIntExtra("parceY", 0);
        crop = getIntent().getBooleanExtra("crop", false);
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
        mRect = new RectF();
        mPaint.setAntiAlias(true);
        if (crop){
            for (int i = 0 ; i < CropImageClass.points.size() ; i++) {
                mPath.lineTo(CropImageClass.points.get(i).x ,CropImageClass.points.get(i).y);

                mRect.top = CropImageClass.points.get(i).y;
                mRect.left = CropImageClass.points.get(i).x;

                mRect.bottom = parceY;
                mRect.right = parceX;

            }
            canvas.drawRect(mRect , mPaint);
        }else {
            for (int i = 0 ; i < CropImageClass.points.size() ; i++) {
                mPath.lineTo(CropImageClass.points.get(i).x ,CropImageClass.points.get(i).y);
            }
            canvas.drawPath(mPath , mPaint);
        }
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(toSet , xInt, yInt, mPaint);
        BitmapSizeReducer();
    }
    public void BitmapSizeReducer() {
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}