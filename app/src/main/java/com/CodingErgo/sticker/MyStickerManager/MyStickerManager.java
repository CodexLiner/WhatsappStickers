package com.CodingErgo.sticker.MyStickerManager;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.CodingErgo.sticker.Constants.Folders;
import com.CodingErgo.sticker.StickerManager.ContentFileParser;
import com.CodingErgo.sticker.StickerManager.StickerPack;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyStickerManager {

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
        String PathForSaving = Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_DOWNLOADS;
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

}
