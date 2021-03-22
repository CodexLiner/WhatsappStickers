package com.CodingErgo.sticker.MyStickerManager;

import android.content.Context;
import android.util.Log;

import com.CodingErgo.sticker.Constants.Folders;
import com.CodingErgo.sticker.StickerManager.ContentFileParser;
import com.CodingErgo.sticker.StickerManager.StickerPack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyStickerManager {

    public static List<StickerPack> getStickerPacks(Context context) {
        List<StickerPack> stickerPackList = new ArrayList<>();
        File content = new File(Folders.ContentFolder + "content.json");
        try(InputStream parser = new FileInputStream(content)){
            stickerPackList = ContentFileParser.parseStickerPacks(parser);

        }catch (IOException e){
            Log.d("TAG", "getStickerPacks: "+e);

        }
        return stickerPackList;


    }
}
