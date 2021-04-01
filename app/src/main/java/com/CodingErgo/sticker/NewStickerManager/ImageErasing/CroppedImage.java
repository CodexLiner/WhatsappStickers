package com.CodingErgo.sticker.NewStickerManager.ImageErasing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.CodingErgo.sticker.R;
import com.CodingErgo.sticker.view.CropView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class CroppedImage extends AppCompatActivity {
    ImageView CroppedImage ;
    String URI ;
    boolean Crop;
    Bitmap bitmap , CreateBitmap;
    Paint paint;
    Path path ;
    Canvas canvas;
    FileInputStream fileInputStream =null;
    BufferedInputStream bf = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crop = getIntent().getBooleanExtra("CROP",false);
        URI = getIntent().getStringExtra("URI");
        setContentView(R.layout.activity_cropped_image);
        CroppedImage = findViewById(R.id.CroppedImage);
      //  CroppedImage.setImageURI(Uri.parse(URI));
        //Create Bitmaop
        try{

            fileInputStream = new FileInputStream(URI);
            bf = new BufferedInputStream(fileInputStream);
            CreateBitmap = BitmapFactory.decodeStream(bf);
        }catch (Exception e){ Log.d("TAG", "onCreate: "+e); }
        finally {
            if (bf!=null){
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }// end Bitmap
        int height;
        int width ;
        DisplayMetrics fm = new DisplayMetrics();
        try{
            getWindowManager().getDefaultDisplay().getMetrics(fm);
        }catch (Exception e){}
        height = fm.heightPixels;
        width = fm.widthPixels;
        bitmap = Bitmap.createBitmap(width ,height ,CreateBitmap.getConfig());
      // Croping
        canvas = new Canvas(bitmap);
        paint = new Paint();
        path = new Path();
        paint.setAntiAlias(true);
        for (int i = 0; i < CropView.points.size() ; i++){
            path.lineTo(CropView.points.get(i).x , CropView.points.get(i).y);
        }
        canvas.drawPath(path , paint);
        if (Crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }
        canvas.drawBitmap(CreateBitmap ,0,0,paint);
        CroppedImage.setImageBitmap(bitmap);
    }
}