package com.CodingErgo.sticker.MyManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.CodingErgo.sticker.Constants.Folders;
import com.CodingErgo.sticker.R;
import com.CodingErgo.sticker.StickerManager.EntryActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

public class PermissionRequest extends AppCompatActivity {
    private boolean FOLDERSTATUS;
    FirebaseFirestore firestore;
    String Fname , Flink ;
    long Size;
    ProgressBar progressBar ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oneSignal.SignalInitializable(this);
        setContentView(R.layout.activity_permission_request);
        sharedPreferences = getSharedPreferences("PERMS", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.entry_activity_progress);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseRunner();

        if (sharedPreferences.contains("packName")){
            Fname = sharedPreferences.getString("packName", "noName");
            Flink = sharedPreferences.getString("URL","noUrl");
            Size = sharedPreferences.getLong("Size",22);
            FirebaseRunner();
            PermissionValidator();


        }else {
            getFolderName();
        }


    }

    private void FirebaseRunner() {
       DocumentReference documentReference = firestore.collection("Sticker").document("C14aN02hCyHAd7dtjycW");
       documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
                       documentSnapshot.getString("packName");
                       Fname = documentSnapshot.getString("packName");
                       Flink = documentSnapshot.getString("url");
                       Size = documentSnapshot.getLong("Size");
                       Log.d("TAG", "onSuccess: "+documentSnapshot.getLong("Size"));
                       editor.putLong("Size", documentSnapshot.getLong("Size"));
                       editor.putString("packName", documentSnapshot.getString("packName"));
                       editor.putString("URL",documentSnapshot.getString("url"));
                       editor.commit();
           }
       });
    }

    private void getFolderName() {
        DocumentReference df = firestore.collection("Sticker").document("C14aN02hCyHAd7dtjycW");
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                documentSnapshot.getString("packName");
                Fname = documentSnapshot.getString("packName");
                Flink = documentSnapshot.getString("url");
                Size = documentSnapshot.getLong("Size");
                Log.d("TAG", "onSuccess: "+documentSnapshot.getLong("Size"));
                editor.putLong("Size", documentSnapshot.getLong("Size"));
                editor.putString("packName", documentSnapshot.getString("packName"));
                editor.putString("URL",documentSnapshot.getString("url"));
                editor.commit();
                PermissionValidator();

            }
        });
    }

    private void PermissionValidator() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            boolean RESULT = CheckDir("sticker");
            Intent intent = new Intent(PermissionRequest.this , MyDownloadManager.class);
            intent.putExtra("FolderStatus", RESULT);
            intent.putExtra("FileLink", Flink);
            intent.putExtra("FolderName", Fname);
            intent.putExtra("Size", Size);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }

    }

    private boolean CheckDir(String dir_name) {
        File DirName = new File(Folders.ContentFolder);
        if (!DirName.isDirectory()){
             return FOLDERSTATUS = false;
        }else {
            if (DirName.isDirectory() && DirName.listFiles().length < Size){
                DirName.delete();
                return FOLDERSTATUS = false;
            }else {
                return FOLDERSTATUS = true;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           // Toast.makeText(this, "PERMISSION_GRANTED" , Toast.LENGTH_SHORT).show();
            boolean RESULT = CheckDir("dir_name");
            Intent intent = new Intent(PermissionRequest.this , MyDownloadManager.class);
            intent.putExtra("FolderStatus", RESULT);
            intent.putExtra("FileLink", Flink);
            intent.putExtra("FolderName", Fname);
            intent.putExtra("Size", Size);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
       }else {
            Toast.makeText(this, "PERMISSION DENIED" ,Toast.LENGTH_SHORT).show();
        }
    }
}