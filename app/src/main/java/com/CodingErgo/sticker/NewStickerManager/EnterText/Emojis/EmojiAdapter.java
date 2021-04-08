package com.CodingErgo.sticker.NewStickerManager.EnterText.Emojis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CodingErgo.sticker.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.Holder> {
    Context context;
    ArrayList<Object> Emojis;

    public EmojiAdapter(Context context, ArrayList<Object> emojis) {
        this.context = context;
        Emojis = emojis;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_rec_item ,parent , false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        EmojiModel model = (EmojiModel) Emojis.get(position);
        Glide.with(holder.itemView).load(model.getUri()).placeholder(R.drawable.ic_smile).into(holder.emoji);

    }

    @Override
    public int getItemCount() {
        return Emojis.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView emoji;
        public Holder(@NonNull View itemView) {
            super(itemView);
            emoji= itemView.findViewById(R.id.emojis);
        }
    }
}
