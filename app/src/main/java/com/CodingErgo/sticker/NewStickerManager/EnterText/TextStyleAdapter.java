package com.CodingErgo.sticker.NewStickerManager.EnterText;

import android.content.Context;
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

public class TextStyleAdapter extends RecyclerView.Adapter<TextStyleAdapter.Holder>{
    Context context ;
    ArrayList<Object> textList;

    public TextStyleAdapter(Context context, ArrayList<Object> textList) {
        this.context = context;
        this.textList = textList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_rec_item , parent , false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TextModel textModel = ( TextModel) textList.get(position);
        holder.textName.setText(textModel.getTextName());
        Log.d(TAG, "onBindViewHolder: "+textList.toString());

    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView textName;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
        }
    }
}
