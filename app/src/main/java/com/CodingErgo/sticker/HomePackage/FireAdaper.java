package com.CodingErgo.sticker.HomePackage;

import android.content.Context;
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
import com.CodingErgo.sticker.StickerManager.StickerPackLoader;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FireAdaper extends FirestoreRecyclerAdapter<itemsModel , FireAdaper.Holder> {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Context context;

    public FireAdaper(@NonNull FirestoreRecyclerOptions<itemsModel> options, Context context) {
        super(options);
        this.context = context;
    }

    public FireAdaper(@NonNull FirestoreRecyclerOptions<itemsModel> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull itemsModel model) {

       CollectionReference df = firestore.collection("StickerPackItems");
       df.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               ArrayList<testModel> im = (ArrayList<testModel>) queryDocumentSnapshots.toObjects(testModel.class);
               testModel tm = im.get(position);
           if (tm.getIdentifier().equals(model.getIdentifier())) {
               holder.sticker_pack_title.setText(model.getName());
               for (int i = 0; i < 5; i++) {
//                   final SimpleDraweeView rowImage = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.sticker_packs_list_image_item, holder.imageRowView, false);
//                   rowImage.setImageURI(Uri.parse(tm.getUrl()));
//                   holder.imageRowView.addView(rowImage);
               }
           }
           }
       });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_packs_list_item,parent ,false);
        return new  Holder(view);
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView sticker_pack_title, sticker_pack_publisher;
        LinearLayout sticker_packs_list_item_image_list;
        LinearLayout imageRowView;
        ImageView img ;
        public Holder(@NonNull View itemView) {
            super(itemView);
            sticker_pack_title = itemView.findViewById(R.id.sticker_pack_title);
            sticker_pack_publisher = itemView.findViewById(R.id.sticker_pack_publisher);
            sticker_packs_list_item_image_list = itemView.findViewById(R.id.sticker_packs_list_item_image_list);
            imageRowView = itemView.findViewById(R.id.sticker_packs_list_item_image_list);
        }
    }
}
