package com.CodingErgo.sticker.WhatsappStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.CodingErgo.sticker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class status extends AppCompatActivity {
BottomNavigationView bottomNavigationView ;
ArrayList<Object> objectArrayList = new ArrayList<>();
File[] files;
StatusAdapter adapter ;
RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        recyclerView = findViewById(R.id.statusRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.statusMenu);
        Bottomnavtest();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2 ));
        adapter  = new StatusAdapter(status.this , getstory());
        recyclerView .setAdapter(adapter);
        adapter.notifyDataSetChanged();
        permission();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void permission() {
        if (ActivityCompat.checkSelfPermission(status.this , Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Log.d("TAG", "PermissionManager: Granteed ");

        }
        else {
            ActivityCompat.requestPermissions(status.this ,new  String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && (grantResults.length >0 ) && grantResults[0] == PackageManager.PERMISSION_GRANTED){
           Toast.makeText(this, "Permission Granteed", Toast.LENGTH_SHORT).show();
            adapter  = new StatusAdapter(status.this , getstory());
            recyclerView .setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Object> getstory() {
            StatusModel model;
            String fecthpath = Environment.getExternalStorageDirectory().getAbsolutePath() + folderName.FOLDER_NAME + "Media/.Statuses";
            File tarfile = new File(fecthpath);
            Log.d("TAG", "getstory: "+fecthpath);
            files = tarfile.listFiles();
            if (files == null) {
               // Toast.makeText(getApplicationContext()," null ", Toast.LENGTH_LONG).show();
            }
            else {
                Log.i("filepath", "getstory status list: " + Arrays.toString(tarfile.listFiles()));
                Log.i("filepath", "getstory status path: " + fecthpath);

                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    model = new StatusModel();
                    model.setUri(Uri.fromFile(file));
                    model.setFilename(file.getName());
                    model.setPath(file.getAbsolutePath());
                    if (!model.getUri().toString().endsWith(".nomedia")) {
                        objectArrayList.add(model);
                        Log.d("TAG", "getstoryURI: "+file);

                    }
                }
            }

            return objectArrayList;

        }


    private void Bottomnavtest() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.statusMenu :{
                        return true;
                    }
                    case R.id.homeMenu :{
                        finish();
                       // startActivity(new Intent(getApplicationContext(),StickerPackListActivity.class));
                        overridePendingTransition(0,0);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,0);
        super.onBackPressed();
    }
}