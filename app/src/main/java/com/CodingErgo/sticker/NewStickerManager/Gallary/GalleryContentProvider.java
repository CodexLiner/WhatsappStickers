package com.CodingErgo.sticker.NewStickerManager.Gallary;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.net.URI;
import java.util.ArrayList;

public class GalleryContentProvider {
    public static ArrayList<String> getImages(Context context){
        Uri uri ;
        Cursor cursor;
        int index;
        ArrayList<String> arrayList = new ArrayList<>();
        String path;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] Projection = {MediaStore.MediaColumns.DATA ,MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        String orderBy = MediaStore.Video.Media.DATE_TAKEN ;
        cursor = context.getContentResolver().query(uri , Projection , null , null , orderBy+" DESC");
        index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()){
            path = cursor.getString(index);
            arrayList.add(path);
        }
        return arrayList;
    }
}
