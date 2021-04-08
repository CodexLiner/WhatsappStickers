package com.CodingErgo.sticker.HomePackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.CodingErgo.sticker.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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
    ArrayList<?> stickerDetails , items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        homeRec = findViewById(R.id.homeRec);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        homeRec.setLayoutManager(new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL , false));
        CollectionReference df = firestore.collection("StickerDetails");
        df.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                stickerDetails = (ArrayList<?>) queryDocumentSnapshots.toObjects(HomeModel.class);

                CollectionReference cf = firestore.collection("s2");
                cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        items = (ArrayList<?>) queryDocumentSnapshots.toObjects(itemsModel.class);
                        adapter = new HomeAdapter(stickerDetails , items);
                        Log.d("TAG", "onSuccess: ");
                        homeRec.setAdapter(adapter);
                    }
                });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}