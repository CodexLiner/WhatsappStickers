package com.CodingErgo.sticker.MyManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.CodingErgo.sticker.Constants.Folders;
import com.CodingErgo.sticker.R;
import com.CodingErgo.sticker.StickerManager.EntryActivity;
import com.CodingErgo.sticker.StickerManager.StickerPack;
import com.CodingErgo.sticker.StickerManager.StickerPackListActivity;
import com.CodingErgo.sticker.TestPackage.testAcitivity;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyDownloadManager extends AppCompatActivity {
    boolean IntentResult ;
    boolean RESULT ;
    boolean DownloadResult;
    long DownLoadId;
    String DownloadName , DownLoadLink;
    TextView StickerLoadidText;
    StickerPack stickerPack ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_download_manager);
        IntentResult = getIntent().getBooleanExtra("FolderStatus", false);
        DownLoadLink = getIntent().getStringExtra("FileLink");
        DownloadName = getIntent().getStringExtra("FolderName");
        StickerLoadidText = findViewById(R.id.StickerLoadidText);

        RunnTV();
       if (IntentResult){
           Intent intent = new Intent(MyDownloadManager.this , EntryActivity.class);
           intent.putExtra("EntryResult", true);
           overridePendingTransition(0,0);
           startActivity(intent);
           finish();
       }else {
           File DirName = new File(Folders.ContentFolder + DownloadName);
           if (!DirName.isDirectory()){
               boolean success = DirName.mkdirs();
           }
           RESULT = DownloadZip(DownloadName+".zip", DownLoadLink );
       }
    }

    private void RunnTV() {

            final Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(10000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            StickerLoadidText.setText("We're Getting Sticker's Ready for you");
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        Log.d("TAG", "Threadrun: "+e);
                    }
                }
            };

            thread.start();


    }

    private boolean DownloadZip(String name, String link) {
        try {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
                request.setDescription("Sticker's Wale");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,name);
                request.setTitle("Downloading Sticker's");
                DownloadManager dm = (DownloadManager)this.getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
                DownLoadId = dm.enqueue(request);
                BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long myDownloadId;
                        myDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,0);
                        if (myDownloadId == DownLoadId){
                            File Src = new File(Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_PICTURES , name);
                            File Dest = new File(Folders.ContentFolder);
                            DownloadResult = ZipManager.ZipExtractor(Src.toString() , Dest.toString());
                            Log.d("TAG", "downloadRES: "+DownloadResult);
                            if (DownloadResult){
                                Intent intent2 = new Intent(MyDownloadManager.this , PermissionRequest.class);
                                intent2.putExtra("EntryResult", DownloadResult);
                                startActivity(intent2);
                                overridePendingTransition(0,0);
                                System.exit(1);
                            }

                        }else { }

                    }
                };
                registerReceiver(broadcastReceiver , new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                return DownloadResult;

            }catch (Exception e){
            Log.d("TAG", "DownloadZip: "+e);
            return false;
        }
    }
}