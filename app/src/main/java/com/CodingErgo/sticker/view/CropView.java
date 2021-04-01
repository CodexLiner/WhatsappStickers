package com.CodingErgo.sticker.view;

import android.content.Context;
import android.content.Intent;
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
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.CodingErgo.sticker.NewStickerManager.ImageErasing.CroppedImage;
import com.CodingErgo.sticker.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CropView extends View implements View.OnTouchListener {
    Bitmap bitmap;
    Paint paint ;
    public static List<Point> points ;
    boolean Freehand = true;
    boolean FirstPoint = false;
    Point lastPoint = null;
    Point firstPoint = null;
    String ImgURI ;

    public CropView(Context context) {
        super(context);
        InitVIew(null);
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);

        paint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);

        this.setOnTouchListener(this);
        points = new ArrayList<Point>();

        FirstPoint = false;
    }

    public CropView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitVIew(attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLUE);

        this.setOnTouchListener(this);
        points = new ArrayList<Point>();
        FirstPoint = false;
    }

    public CropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitVIew(attrs);
    }

    public CropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        InitVIew(attrs);
    }
    public void InitVIew(@Nullable AttributeSet set){
        bitmap = BitmapFactory.decodeResource(getResources() , R.drawable.ppp);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int padding = 50;
                bitmap = GetResizedBitmap( bitmap , getWidth()  - padding , getHeight() - padding );
            }
        });
        this.setOnTouchListener(this);
        points = new ArrayList<Point>();
        postInvalidate();

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x =  (getWidth() - bitmap.getWidth()) /2f ;
        float y =  (getHeight() - bitmap.getHeight() ) /2f;

        canvas.drawBitmap(bitmap , x, y, null);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[] {10,20},0));
        paint.setStrokeMiter(0.5f);


        Path path = new Path();
        boolean status = true;

        for (int i = 0; i < points.size(); i += 2) {
            Point point = points.get(i);
            if (status) {
                status = false;
                path.moveTo(point.x, point.y);
            } else if (i < points.size() - 1) {
                Point next = points.get(i + 1);
                path.quadTo(point.x, point.y, next.x, next.y);
            } else {
                lastPoint = points.get(i);
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);


    }
    public void setImageUri(Uri uri){
        DecodeUri(uri.toString());
        ImgURI = uri.getPath() ;

    }
    private Bitmap GetResizedBitmap(Bitmap bitmap, int width ,int height) {
        Matrix m = new Matrix();
        RectF src = new RectF(0,0,bitmap.getHeight() , bitmap.getWidth());
        RectF dest = new RectF(0 ,0 , width, height );
        m.setRectToRect(src , dest , Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap , 0,0,bitmap.getWidth() ,bitmap.getHeight() , m , true);
    }

    private void DecodeUri(String toString) {
        BufferedInputStream bf = null;
        FileInputStream fileInputStream = null;
      try{
           fileInputStream = new FileInputStream(toString);
           bf = new BufferedInputStream(fileInputStream);
           bitmap = BitmapFactory.decodeStream(bf);

      }catch (Exception e){
          Log.d(TAG, "DecodeUriWriting : "+e);
      }finally {
          try {
              if (bf!=null){
                  bf.close();
              }
              if (fileInputStream!=null){
                  fileInputStream.close();
              }
          }catch (Exception e){
              Log.d(TAG, "DecodeUriClosing : "+e);
          }


      }
   }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Point point = new Point();
        point.x =(int) event.getX();
        point.y =(int) event.getY();
        if (Freehand)
        {
             if (FirstPoint) {
                 if (ComparePoint(firstPoint, point))
                  {
                     points.add(firstPoint);
                     Freehand = false;
                      Intent intent = new Intent(getContext().getApplicationContext(), CroppedImage.class);
                      intent.putExtra("URI", ImgURI);
                      intent.putExtra("CROP", true);
                      getContext().startActivity(intent);
                  }
                 else
                  {
                     points.add(point);
                  }
             }
             else
                 {
                     points.add(point);
                 }

             if (!(FirstPoint))
                 {
                    firstPoint = point;
                    FirstPoint = true;
                 }
        }
      invalidate();
        if (event.getAction() == MotionEvent.ACTION_UP){
            lastPoint = point ;
            if (Freehand){
                if (points.size() > 12){
                    if (!ComparePoint(firstPoint,lastPoint)){
                       Freehand =false ;
                       points.add(firstPoint);
                       Intent intent = new Intent(getContext().getApplicationContext(), CroppedImage.class);
                       intent.putExtra("URI", ImgURI);
                       intent.putExtra("CROP", true);
                       getContext().startActivity(intent);

                   }
                }
            }
        }
     return true;
    }
    public boolean ComparePoint (Point first , Point current){
        int left_range_x = (int) (current.x - 3);
        int left_range_y = (int) (current.y - 3);

        int right_range_x = (int) (current.x + 3);
        int right_range_y = (int) (current.y + 3);

        if ((left_range_x < first.x && first.x < right_range_x)
                && (left_range_y < first.y && first.y < right_range_y)) {
            if (points.size() < 10) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }
}
