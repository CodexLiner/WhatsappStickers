package com.CodingErgo.sticker.TestPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.CodingErgo.sticker.MyStickerManager.MyStickerManager;
import com.CodingErgo.sticker.R;
import com.CodingErgo.sticker.StickerManager.StickerPack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class testAcitivity extends AppCompatActivity {
    boolean RESULT;
    ArrayList<StickerPack> stickerPacks ;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_acitivity);
        firestore = FirebaseFirestore.getInstance();
        stickerPacks = MyStickerManager.StickerLoader(this);
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("ArrayList",stickerPacks);
        setResult(RESULT_OK , intent);
        DocumentReference df = firestore.collection("ssss").document("sticker_packs");
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              if (task.isComplete()){
                  DocumentSnapshot arrayList = task.getResult();
                 if (arrayList.exists()){
                     Log.d("TAG", "onComplete: "+arrayList.toString());

                 }
              }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(testAcitivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
     }
}