package com.CodingErgo.sticker.NewStickerManager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CodingErgo.sticker.NewStickerManager.ImageErasing.CropImageTool;
import com.CodingErgo.sticker.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Initializable;

import java.util.List;

import static android.content.ContentValues.TAG;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.Holder> {
    Context context ;
    List<String> photos ;

    public GalleryAdapter(Context context, List<String> photos) {
        this.context = context;
        this.photos = photos;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_row_design, parent ,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String uri = photos.get(position);
        Glide.with(context).load(uri).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.imageView.getContext() , CropImageTool.class);
                intent.putExtra("URI", uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.GalleryImageRow);
        }
    }
}
