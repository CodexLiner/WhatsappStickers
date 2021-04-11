package com.CodingErgo.sticker.HomePackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.CodingErgo.sticker.MyManager.PermissionRequest;
import com.CodingErgo.sticker.R;
import com.CodingErgo.sticker.StickerManager.EntryActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeMain extends AppCompatActivity {
    RecyclerView homeRec;
    FirebaseFirestore firestore;
    FirebaseDatabase firebaseDatabase;
    HomeAdapter adapter;
    FireAdaper fireAdaper;
    ArrayList<?> stickerDetails , items , test;
    BottomNavigationView bottomNav;
    FloatingActionButton FabBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        homeRec = findViewById(R.id.homeRec);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FabBtn = findViewById(R.id.FabBtn);
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setBackgroundColor(Color.TRANSPARENT);
        homeRec.setLayoutManager(new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL , false));
        CollectionReference df = firestore.collection("StickerDetails");
        df.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                stickerDetails = (ArrayList<?>) queryDocumentSnapshots.toObjects(itemsModel.class);
               // adapter = new HomeAdapter(stickerDetails);
               // homeRec.setAdapter(adapter);
            }
        });

        Query query = firestore.collection("StickerDetails");
        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<itemsModel>()
                .setQuery(query , itemsModel.class)
                .build();
        fireAdaper = new FireAdaper(options, this);
        homeRec.setAdapter(fireAdaper);
        FabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PermissionRequest.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        fireAdaper.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        fireAdaper.stopListening();

    }
}