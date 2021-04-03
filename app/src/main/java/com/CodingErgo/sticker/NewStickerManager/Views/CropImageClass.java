package com.CodingErgo.sticker.NewStickerManager.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.CodingErgo.sticker.MyStickerManager.MyStickerManager;
import com.CodingErgo.sticker.NewStickerManager.ImageErasing.CropImageTool;
import com.CodingErgo.sticker.NewStickerManager.ImageErasing.EditStickers;
import com.CodingErgo.sticker.R;

import java.util.ArrayList;
import java.util.List;

public class CropImageClass extends View implements View.OnTouchListener {
    private Paint mPaint;
    public static List<Point> points;
    boolean isStepOne = true;
    private int mGravity = Gravity.START | Gravity.TOP;
    Point onStart = null;
    Point onClose = null;
    boolean isStepTwo = false;
    Bitmap mainBitmap;
    Context mContext;
    int x , y ;


    public CropImageClass(Context c) {
        super(c);

        mContext = c;
        OnCreate(null);
        setFocusable(true);
        setFocusableInTouchMode(true);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);

        this.setOnTouchListener(this);
        points = new ArrayList<Point>();

        isStepTwo = false;
        invalidate();
    }

    public CropImageClass(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10,10,10}, 10));
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.parseColor("#00bcd4"));
        OnCreate(attrs);
        this.setOnTouchListener(this);
        points = new ArrayList<Point>();
        isStepTwo = false;

    }
    public int getGravity() {
        return mGravity;
    }
    public void OnCreate(@Nullable AttributeSet set){
        mainBitmap = BitmapFactory.decodeResource(getResources() , R.drawable.ppp);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mainBitmap = getResizeMap(mainBitmap , getWidth() -50 , getHeight() -50);
            }
        });
        setFocusable(true);
        setFocusableInTouchMode(true);
        points = new ArrayList<>();

    }

    private Bitmap getResizeMap(Bitmap mainBitmap, int width, int height) {
            Matrix m = new Matrix();
            RectF src = new RectF(0,0,mainBitmap.getHeight() , mainBitmap.getWidth());
            RectF dest = new RectF(0 ,0 ,width, height );
            m.setRectToRect(dest , src , Matrix.ScaleToFit.CENTER);

        return Bitmap.createBitmap(mainBitmap , 0,0, mainBitmap.getWidth() , mainBitmap.getHeight() , m , true);
    }

    public void setMainBitmap(Bitmap bm){
        mainBitmap = bm;
    }
    public void onDraw(Canvas canvas) {
         y = (getHeight() - mainBitmap.getHeight()) /2;
         x = (getWidth() - mainBitmap.getWidth()) / 2;
        canvas.drawBitmap(mainBitmap,x , y, null);

        Path path = new Path();
        boolean First = true;

        for (int i = 0; i < points.size(); i += 2) {
            Point point = points.get(i);
            if (First) {
                First = false;
                path.moveTo(point.x, point.y);
            } else if (i < points.size() - 1) {
                Point next = points.get(i + 1);
                path.quadTo(point.x, point.y, next.x, next.y);
            } else {
                onClose = points.get(i);
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, mPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean onTouch(View view, MotionEvent event) {

        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();

        if (isStepOne) {

            if (isStepTwo) {

                if (ComparePoint(onStart, point)) {
                    points.add(onStart);
                    isStepOne = false;
                    Intent intent = new Intent(mContext, EditStickers.class);
                    MyStickerManager.MyBitmap(mainBitmap);
                    intent.putExtra("xInt", x);
                    intent.putExtra("yIny",y);
                    mContext.startActivity(intent);
                } else {
                    points.add(point);
                }
            } else {
                points.add(point);
            }

            if (!(isStepTwo)) {

                onStart = point;
                isStepTwo = true;
            }
        }

        invalidate();

        if (event.getAction() == MotionEvent.ACTION_UP) {

            onClose = point;
            if (isStepOne) {
                if (points.size() > 12) {
                    if (!ComparePoint(onStart, onClose)) {
                        isStepOne = false;
                        points.add(onStart);
                        Intent intent = new Intent(mContext, EditStickers.class);
                        MyStickerManager.MyBitmap(mainBitmap);
                        intent.putExtra("xInt", x);
                        intent.putExtra("yIny",y);
                        mContext.startActivity(intent);
                    }
                }
            }
        }

        return true;
    }

    private boolean ComparePoint(Point onStart, Point Current) {
        int left_range_x = (int) (Current.x - 3);
        int left_range_y = (int) (Current.y - 3);

        int right_range_x = (int) (Current.x + 3);
        int right_range_y = (int) (Current.y + 3);

        if ((left_range_x < onStart.x && onStart.x < right_range_x)
                && (left_range_y < onStart.y && onStart.y < right_range_y)) {
            if (points.size() < 10) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    private void showcropdialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        intent = new Intent(mContext, EditStickers.class);
                        intent.putExtra("crop", mainBitmap);
                        mContext.startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        intent = new Intent(mContext, EditStickers.class);
                        intent.putExtra("crop", mainBitmap);
                        mContext.startActivity(intent);

                        isStepTwo = false;

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Do you Want to save Crop or Non-crop image?")
                .setPositiveButton("Crop", dialogClickListener)
                .setNegativeButton("Non-crop", dialogClickListener).show()
                .setCancelable(false);
    }
}