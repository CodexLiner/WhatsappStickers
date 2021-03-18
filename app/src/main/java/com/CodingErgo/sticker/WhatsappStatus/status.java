package com.CodingErgo.sticker.WhatsappStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.CodingErgo.sticker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class status extends AppCompatActivity {
BottomNavigationView bottomNavigationView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.statusMenu);
        Bottomnavtest();
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