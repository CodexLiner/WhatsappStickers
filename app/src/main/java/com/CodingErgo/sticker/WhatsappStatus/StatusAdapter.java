package com.CodingErgo.sticker.WhatsappStatus;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.CodingErgo.sticker.R;
import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class StatusAdapter extends  RecyclerView.Adapter<StatusAdapter.Holder>{
    Context context ;
    ArrayList<Object> arrayList ;

    public StatusAdapter(Context context, ArrayList<Object> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_row_line, parent ,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        StatusModel model = (StatusModel) arrayList.get(position);
        if (model.getUri().toString().endsWith(".mp4")){
            holder.play.setVisibility(View.VISIBLE);
        }
        Glide.with(holder.status).load(model.getUri()).into(holder.status);
        Log.d("TAG", "onBindViewHolder: "+model.getUri());
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = model.getUri().toString();
                String sub= uri.substring(8 , uri.length());
                String uriContent = "file://";
                String MainURI = uriContent+sub;
               try{
                   Intent intent = new Intent(Intent.ACTION_SEND);
                   intent.putExtra(Intent.EXTRA_STREAM , model.getUri());
                   intent.setType("image/*");
                   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   holder.share.getContext().startActivity(intent);

               }catch (Exception e){
                   Log.d(TAG, "onClickURI: "+e);
               }

            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File src = new File(model.getPath());
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Environment.DIRECTORY_DOWNLOADS);
                try {
                 FileUtils.copyFileToDirectory(src , dir);
                    Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Downloaded Faild"+e, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView status , play ,download , share ;

        public Holder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.statusImage);
            play = itemView.findViewById(R.id.playButton);
            download = itemView.findViewById(R.id.DownloadAdapter);
            share = itemView.findViewById(R.id.shareAdapter);
        }
    }

}
