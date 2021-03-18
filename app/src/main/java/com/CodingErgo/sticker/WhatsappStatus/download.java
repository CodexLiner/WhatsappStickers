package com.CodingErgo.sticker.WhatsappStatus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.CodingErgo.sticker.R;

import java.net.URI;

public class download extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        String uri = getIntent().getStringExtra("URI");
        String mime;
        mime =   MimeTypeMap .getFileExtensionFromUrl(uri);
       try {
           DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
           request.setTitle("Downloading...");
           request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM , "Stciker Wale"+System.currentTimeMillis()+"."+mime);
           DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
           downloadManager.enqueue(request);
       }catch (Exception e){
           Toast.makeText(this, "Failed To Save", Toast.LENGTH_SHORT).show();
           Log.d("TAG", "onCreateEx: "+e);
       }
      finish();
    }
}