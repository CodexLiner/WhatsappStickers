package com.CodingErgo.sticker.MyStickerManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.CodingErgo.sticker.Constants.Folders;
import com.CodingErgo.sticker.MyManager.PermissionRequest;
import com.CodingErgo.sticker.StickerManager.ContentFileParser;
import com.CodingErgo.sticker.StickerManager.StickerPack;
import com.CodingErgo.sticker.StickerManager.StickerPackLoader;
import com.CodingErgo.sticker.StickerManager.StickerPackValidator;
import com.bumptech.glide.load.engine.Initializable;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyStickerManager {
   static Bitmap bmp;

    public static List<StickerPack> getStickerPacks(Context context) {
        List<StickerPack> stickerPackList = new ArrayList<>();
        File content = new File(Folders.ContentFolder + "contents.json");
        try(InputStream parser = new FileInputStream(content)){
            stickerPackList = ContentFileParser.parseStickerPacks(parser);
        }catch (IOException e){
            Log.d("TAG", "getStickerPacks: "+e);
        }
        return stickerPackList;
     }
     public static boolean RequestSaveStatus(String path , String identifier){
        boolean RESULT;
        String PathForSaving = Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_DCIM;
        String [] SplitedtUri = path.split("/");
        String mainUri = Folders.ContentFolder + identifier +"/"+ SplitedtUri[3];
        RESULT = StartCopying(PathForSaving , mainUri);
        return RESULT ;
    }
     private static boolean StartCopying(String pathForSaving, String mainUri) {
        File DEST =new File(pathForSaving);
        File SRC = new File(mainUri);
        try{
            FileUtils.copyFileToDirectory(SRC ,DEST);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public static long GetItemCount(String id){
        long number;
        File num = new File(Folders.ContentFolder+id);
        number = num.listFiles().length -1;
        Log.d(TAG, "GetItemCount: "+number);
        if (number>30){
            number = 30 ;
        }
        return number ;
    }
    public static ArrayList<StickerPack> StickerLoader(Context context){
        ArrayList<StickerPack> stickerPacks ;
       try{
           stickerPacks = StickerPackLoader.fetchStickerPacks(context);
           for (StickerPack stickerPack : stickerPacks){
               StickerPackValidator.verifyStickerPackValidity(context , stickerPack);
           }
           return stickerPacks;
       }catch (Exception e){
           Log.d(TAG, "StickerLoaderEce: "+e);
           return null;
       }

    }
    public static DraweeController DraweeImageView(Context context , Uri uri){
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        return controller;
    }
    public static void deleteStickerZip(){
        File file = new File(Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_PICTURES+"/"+"sticker.zip");
        if (file.exists()){
            boolean success = file.delete();
            Log.d(TAG, "deleteStickerZip: "+success);
        }
    }
    public static void MyBitmap(Bitmap bm){
        bmp = bm;
    }
    public static Bitmap getBitmap(){
        return bmp;
    }

}
