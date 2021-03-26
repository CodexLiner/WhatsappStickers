package com.CodingErgo.sticker.MyManager;

import android.util.Log;

import com.CodingErgo.sticker.Constants.Folders;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static android.content.ContentValues.TAG;

public class ZipManager {
    public static boolean ZipExtractor(String FilePath , String SrcPath){
        try {

            FileInputStream fileInputStream = new FileInputStream(FilePath);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            ZipEntry zipEntry = null;
            while (( zipEntry = zipInputStream.getNextEntry() ) != null) {
                if (zipEntry.isDirectory()){
                    ZipDirMaker(zipEntry.getName());
                }else {
                    FileOutputStream fileOutputStream = new FileOutputStream(SrcPath+"/"+ zipEntry.getName());
                    BufferedOutputStream bf = new BufferedOutputStream(fileOutputStream);
                    byte[] bytes = new byte [4096];
                    int len;
                    while ( (len = zipInputStream.read(bytes) ) > 0){
                        bf.write(bytes , 0 , len);

                    }
                    zipInputStream.closeEntry();
                    bf.close();
                }

            }
            return true;
          } catch (IOException e) {

            Log.d(TAG, "ZipExtractor: "+e);
            e.printStackTrace();
            return false;
        }

    }

    private static void ZipDirMaker(String name) {
       try{
           File zipDir = new File(Folders.ContentFolder + name);
           if (!zipDir.isDirectory()){
               boolean success = zipDir.mkdirs();
               Log.d(TAG, "ZipDirMaker: "+success);
           }
       }catch (Exception e){
           Log.d(TAG, "ZipDirMaker: "+e);
       }
    }
}
