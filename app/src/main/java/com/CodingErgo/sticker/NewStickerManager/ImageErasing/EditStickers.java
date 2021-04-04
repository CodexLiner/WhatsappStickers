package com.CodingErgo.sticker.NewStickerManager.ImageErasing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.CodingErgo.sticker.MyStickerManager.MyStickerManager;
import com.CodingErgo.sticker.NewStickerManager.Views.CropImageClass;
import com.CodingErgo.sticker.R;
import com.bumptech.glide.load.engine.Initializable;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import static android.content.ContentValues.TAG;

public class EditStickers extends AppCompatActivity {
    LinearLayout BottomSheet , EditText ;
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
        BottomSheet = findViewById(R.id.BottomSheet);
        CropedImage = findViewById(R.id.CropedImage);
        xInt = getIntent().getIntExtra("xInt",0);
        yInt = getIntent().getIntExtra("yInt", 0);
        parceX = getIntent().getIntExtra("parceX",0);
        parceY = getIntent().getIntExtra("parceY", 0);
        crop = getIntent().getBooleanExtra("crop", false);
        ButtonManager();
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
                Log.d(TAG, "parcey "+parceY);
                Log.d(TAG, "parcex "+parceX);

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

    private void ButtonManager() {
        BottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet(getApplicationContext() );
            }
        });
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
        paint.setFilterBitmap(false);

        canvas.drawBitmap(toCreate, transformation, paint);
        CropedImage.setImageBitmap(background);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       startActivity(new Intent(getApplicationContext(), CropImageTool.class));
       MyStickerManager.MyBitmap(toSet);
        overridePendingTransition(0,R.anim.out_right);
       finish();
    }
    public void BottomSheet(Context context){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditStickers.this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_dialog);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }
    private void ToastMaker(){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_layout_file , (findViewById(R.id.Toast_shape_layout)));
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setText("Hello Toast");
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}