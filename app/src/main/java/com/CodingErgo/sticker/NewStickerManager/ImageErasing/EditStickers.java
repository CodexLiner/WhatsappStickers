package com.CodingErgo.sticker.NewStickerManager.ImageErasing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.CodingErgo.sticker.MyStickerManager.MyStickerManager;
import com.CodingErgo.sticker.NewStickerManager.EnterText.Emojis.EmojiAdapter;
import com.CodingErgo.sticker.NewStickerManager.EnterText.Emojis.EmojiModel;
import com.CodingErgo.sticker.NewStickerManager.EnterText.TextModel;
import com.CodingErgo.sticker.NewStickerManager.EnterText.TextStyleAdapter;
import com.CodingErgo.sticker.NewStickerManager.Views.CropImageClass;
import com.CodingErgo.sticker.R;
import com.bumptech.glide.load.engine.Initializable;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class EditStickers extends AppCompatActivity {
    LinearLayout BottomSheet , Editt ,AddText ;
    Dialog dialog;
    Bitmap toSet , toCreate;
    Paint mPaint ;
    ImageView Manu;
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
        Manu = findViewById(R.id.ManualCrop);
        AddText = findViewById(R.id.AddText);
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
                try {
                    BottomSheet(getApplicationContext() );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        AddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditDialog();
            }
        });
    }

    private void ShowEditDialog() {
        RecyclerView recyclerView;
        ImageView CancleDialog ;
        ImageView DoneText;
        ArrayList<Object> textList;
        TextStyleAdapter textStyleAdapter ;
        TextModel textModel;
        EditText editText;
        String [] TextNames = {"Normal" ,  "Basic" ,"Advance" , "Cursive"};
        dialog = new Dialog(EditStickers.this);
        dialog.setContentView(R.layout.enter_text_dialog);
        dialog.setCancelable(true);
        recyclerView = dialog.findViewById(R.id.TextRecyclerView);
        CancleDialog = dialog.findViewById(R.id.CancleEditText);
        DoneText = dialog.findViewById(R.id.DoneEditText);
        editText = dialog.findViewById(R.id.setText);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ,LinearLayoutManager.HORIZONTAL,false));
        textList = new ArrayList<>();
        TextModel t1 = new TextModel("Normal");
        TextModel t2 = new TextModel("Basic");
        TextModel t3 = new TextModel("Advance");
        TextModel t4 = new TextModel("Nirmala Ui");
        TextModel t5 = new TextModel("Cursive");
        TextModel t6 = new TextModel("Monospace");
        textList.add(t1);
        textList.add(t2);
        textList.add(t3);
        textList.add(t4);
        textList.add(t5);
        textList.add(t6);
        textStyleAdapter = new TextStyleAdapter(this , textList);
        recyclerView.setAdapter(textStyleAdapter);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawableResource(R.color.trans);
        dialog.show();
        editText.requestFocus();
        CancleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        DoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    // editText.setError("errorr");
                    return;
                }
                CropedImage.AddTextToMap(text, "qh.ttf");
                dialog.dismiss();
                Toast.makeText(EditStickers.this, "Clicked", Toast.LENGTH_SHORT).show();
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
    public void BottomSheet(Context context) throws IOException {
        AssetManager as = context.getAssets();
        String [] Emoji = as.list("emoji");
        EmojiAdapter adapter ;
        EmojiModel model;
        ArrayList<Object> Emjois;
        RecyclerView EmojiRec;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditStickers.this,R.style.BottomSheet);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_dialog);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.trans);
        EmojiRec = bottomSheetDialog.findViewById(R.id.EmojiRec);
        EmojiRec.setLayoutManager(new GridLayoutManager(this ,5));
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