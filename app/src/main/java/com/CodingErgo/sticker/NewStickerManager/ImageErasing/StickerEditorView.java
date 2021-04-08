package com.CodingErgo.sticker.NewStickerManager.ImageErasing;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.BoringLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.CodingErgo.sticker.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class StickerEditorView extends View implements View.OnTouchListener {
    Bitmap mainBitmap, tmbp;
    Paint mPaint;
    int x , y;
    int textHeight , textWidth;
    String canvasText = "";
    String textFace = "qh.ttf";

    public StickerEditorView(Context context) {
        super(context);
        OnCreate(null);
    }

    public StickerEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        OnCreate(attrs);
        this.setOnTouchListener(this);
    }

    public void OnCreate(@Nullable AttributeSet set) {
        mainBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.window);
        setFocusable(true);
        setFocusableInTouchMode(true);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mainBitmap = getResizedMap(mainBitmap, getWidth() - 50, getHeight() - 50);
            }
        });
        invalidate();
    }

    private Bitmap getResizedMap(Bitmap mainBitmap, int width, int height) {
        Matrix m = new Matrix();
        RectF src = new RectF(0, 0, mainBitmap.getHeight(), mainBitmap.getWidth());
        RectF dest = new RectF(0, 0, width, height);
        m.setRectToRect(src, dest, Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(mainBitmap, 0, 0, mainBitmap.getWidth(), mainBitmap.getHeight(), m, false);
    }
    public void AddTextToMap(String text , String face ){
        canvasText = text;
        textFace = face;
        invalidate();
    }
    public void setImageBitmap(Bitmap bm) {
        mainBitmap = bm;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
// Image Bitmap
        x = (getWidth() - mainBitmap.getWidth()) / 2;
        y = (getHeight() - mainBitmap.getHeight()) / 2;
        canvas.drawBitmap(mainBitmap, x, y, null);
// Text Bitmap
        mPaint = new Paint();
        textWidth = canvas.getWidth() / 2 ;
        textHeight = canvas.getHeight() / 2;
        AssetManager as = getContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(as ,textFace);
        mPaint.setTypeface(typeface);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      //  mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10, 20, 30}, 20));
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.parseColor("#FF42A5F5"));
        mPaint.setTextSize(80);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(canvasText, textWidth , textHeight , mPaint);
        canvas.save();


    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_POINTER_DOWN :{
                Log.d(TAG, "onTouchDragDOWN: true ");
                return true;

            }
            case MotionEvent.ACTION_MOVE:{
                float ny = event.getY();
                float nx = event.getX();
               if (event.getAction() == MotionEvent.ACTION_MOVE){
                   int newHeight = getHeight() + (int) nx;
                     int newWidth = getWidth() +(int) ny;
                       mainBitmap = getResizedMap(mainBitmap , newWidth , newHeight);
                        postInvalidate();
                         return true;

               }
                // Log.d(TAG, "onTouchDragMove: true ");

            }
            case MotionEvent.ACTION_POINTER_UP:{
                postInvalidate();
                return true;
            }

        }


        return true;
    }
}
