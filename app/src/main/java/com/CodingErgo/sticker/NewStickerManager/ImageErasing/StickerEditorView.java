package com.CodingErgo.sticker.NewStickerManager.ImageErasing;

import android.content.Context;
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
import android.text.BoringLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.CodingErgo.sticker.R;

import java.util.ArrayList;
import java.util.List;

public class StickerEditorView extends View implements View.OnTouchListener {
    Bitmap mainBitmap;
    Point onStart;
    Point onClose;
    boolean isStepOne, isStepTwo;
    List<Point> points;
    Paint mPaint;
    Path mPath;
    int Zoom =1 ;
    int mode ;
    float isBig;

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
        mainBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ppp);
        setFocusable(true);
        setFocusableInTouchMode(true);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mainBitmap = getResizedMap(mainBitmap, getWidth() - 50, getHeight() - 50);
            }
        });
        points = new ArrayList<>();
        invalidate();
    }

    private Bitmap getResizedMap(Bitmap mainBitmap, int width, int height) {
        Matrix m = new Matrix();
        RectF src = new RectF(0, 0, mainBitmap.getHeight(), mainBitmap.getWidth());
        RectF dest = new RectF(0, 0, width, height);
        m.setRectToRect(src, dest, Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(mainBitmap, 0, 0, mainBitmap.getWidth(), mainBitmap.getHeight(), m, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = (getWidth() - mainBitmap.getWidth()) / 2;
        int y = (getHeight() - mainBitmap.getHeight()) / 2;
        canvas.drawBitmap(mainBitmap, x, y, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10, 20, 30}, 20));
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        mPath = new Path();

        boolean First = true;
        for (int i = 0; i < points.size(); i += 2) {
            Point Initial = points.get(i);
            if (First) {
                First = false;
                mPath.moveTo(Initial.x, Initial.y);
            } else if (i < points.size() - 1) {
                Point next = points.get(i + 1);
                mPath.quadTo(Initial.x, Initial.y, next.x, next.y);
            } else {
                onClose = points.get(i);
                mPath.lineTo(Initial.x, Initial.y);
            }
        }
        canvas.drawPath(mPath, mPaint);

    }

    public void setImageBitmap(Bitmap bm) {
        mainBitmap = bm;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){

        }


        return true;
    }
}
