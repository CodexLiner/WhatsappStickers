package com.CodingErgo.sticker.WhatsappStatus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

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
        if (!model.getUri().toString().endsWith(".mp4")) { holder.play.setVisibility(View.GONE); }
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
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Environment.DIRECTORY_DOWNLOADS+"/Sticker's Wale");
                try {
                    if (!dir.isDirectory()){ dir.mkdir(); dir.mkdir(); }
                    FileUtils.copyFileToDirectory(src , dir);
                    Toast.makeText(context, "Saved to Downloads", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Downloaded Faild"+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(holder.status.getContext());
                dialog.setContentView(R.layout.status_tab_dialog);
                Button share , save ;
                share = dialog.findViewById(R.id.dialogShare);
                save = dialog.findViewById(R.id.dialogSave);
                VideoView video = dialog.findViewById(R.id.statusviewvideo);
                ImageView image = dialog.findViewById(R.id.statusviewimage);
                if (model.getUri().toString().endsWith(".mp4")){
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                   dialog.getWindow().setAttributes(layoutParams);

                    video.setVideoPath(model.getPath());
                    video.setVisibility(View.VISIBLE);
                    video.start();
                    dialog.show();
                }
               if (!model.getUri().toString().endsWith(".mp4")){
                   Glide.with(image).load(model.getUri()).into(image);
                   image.setVisibility(View.VISIBLE);
                   dialog.show();
               }

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM , model.getUri());
                        intent.setType("images/*");
                        holder.status.getContext().startActivity(intent);

                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Environment.DIRECTORY_DOWNLOADS+"/Sticker's Wale");
                        File src = new File(model.getPath());
                        try{
                            if (!dir.isDirectory()){ dir.mkdir(); dir.mkdir(); }
                            FileUtils.copyFileToDirectory(src , dir);
                            Toast.makeText(holder.status.getContext(), "Saved to Downloads", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(holder.status.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClickSaveFile: "+e);
                        }
                    }
                });
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
