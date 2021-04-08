package com.CodingErgo.sticker.HomePackage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CodingErgo.sticker.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder>{
    ArrayList<?> stickerDetails , packItems;

    public HomeAdapter(ArrayList<?> stickerDetails, ArrayList<?> packItems) {
        this.stickerDetails = stickerDetails;
        this.packItems = packItems;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_packs_list_item,parent ,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        itemsModel im = (itemsModel) packItems.get(position);
        HomeModel model = (HomeModel) stickerDetails.get(position);

        holder.sticker_pack_title.setText(model.getId());
        holder.sticker_pack_publisher.setText(im.getSize());

    }

    @Override
    public int getItemCount() {
//        return packItems.size();
        return stickerDetails.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView sticker_pack_title, sticker_pack_publisher;
        public Holder(@NonNull View itemView) {
            super(itemView);
            sticker_pack_title = itemView.findViewById(R.id.sticker_pack_title);
            sticker_pack_publisher = itemView.findViewById(R.id.sticker_pack_publisher);
        }
    }
}
