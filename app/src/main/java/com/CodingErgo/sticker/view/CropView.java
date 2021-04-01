package com.CodingErgo.sticker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.CodingErgo.sticker.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import static android.content.ContentValues.TAG;

public class CropView extends View {
    Bitmap bitmap;
    ImageView bitImage;
    public CropView(Context context) {
        super(context);
        InitVIew(null);
    }

    public CropView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitVIew(attrs);
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
        postInvalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        canvas.drawBitmap(bitmap , 0, 0, null);
    }
    public void setImageUri(Uri uri){
        DecodeUri(uri.toString());

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
}
