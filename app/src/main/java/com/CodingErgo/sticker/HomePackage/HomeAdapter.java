package com.CodingErgo.sticker.HomePackage;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CodingErgo.sticker.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder>{
    ArrayList<?> stickerDetails, StickerItems;
    testModel testModel;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public HomeAdapter(ArrayList<?> stickerDetails) {
        this.stickerDetails = stickerDetails;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_packs_list_item,parent ,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        itemsModel model = (itemsModel) stickerDetails.get(position);
        holder.sticker_pack_title.setText(model.getName());
        DocumentReference df = firestore.collection("StickerPackItems").document();
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               Log.d(TAG, "onSuccess: "+model.getIdentifier());
                if (model.getIdentifier().equals(documentSnapshot.getString("identifier"))){
//                    Log.d(TAG, "onSuccess: "+documentSnapshot.getString("identifier"));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return stickerDetails.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView sticker_pack_title, sticker_pack_publisher;
        LinearLayout sticker_packs_list_item_image_list;
        ImageView img ;
        public Holder(@NonNull View itemView) {
            super(itemView);
            sticker_pack_title = itemView.findViewById(R.id.sticker_pack_title);
            sticker_pack_publisher = itemView.findViewById(R.id.sticker_pack_publisher);
            sticker_packs_list_item_image_list = itemView.findViewById(R.id.sticker_packs_list_item_image_list);
        }
    }
}
