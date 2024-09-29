package com.example.baitap10;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    File[] filesAndFolders;

    public Adapter(Context context, File[] filesAndFolders) {
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_files, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        File selected = filesAndFolders[position];
        holder.file_name.setText(selected.getName());
        if (selected.isDirectory()){
            holder.icon.setImageResource(R.drawable.baseline_folder_24);
        }else {
            String filePath = selected.getAbsolutePath();
            String compare = filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
            if (compare.equals(".mp3") || compare.equals(".wav") || compare.equals(".ogg")){
                holder.icon.setImageResource(R.drawable.baseline_music_note_24);
            }
            else {
                holder.icon.setImageResource(R.drawable.baseline_play);
            }
        }

        holder.file_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected.isDirectory()){
                    Intent intent = new Intent(context,SecondActivity.class);
                    String path = selected.getAbsolutePath();
                    intent.putExtra("pathAbsolute", path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });

        holder.file_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().add("Play music");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Play music")){
                            String path = selected.getAbsolutePath();
                            if (checkFileMusic(path)==false){
                                Toast.makeText(context.getApplicationContext(),"No found file music",Toast.LENGTH_SHORT).show();
                            }else {
                                Intent intent = new Intent(context, MusicActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("pathFile",path);
                                intent.putExtra("Bundle",bundle);
                                context.startActivity(intent);
                            }
                        }
                        return true;
                    }
                });

                popupMenu.show();
                return true;
            }
        });

    }

    private boolean checkFileMusic(String path){
        Boolean flag = false;
        File root = new File(path);
        File[] fileMusic = root.listFiles();
        String filePath = new String();
        if (fileMusic==null || fileMusic.length==0){
            return false;
        } else {
            for (int i =0; i<fileMusic.length ; i++){
                if (fileMusic[i].isDirectory()==false){
                    filePath = fileMusic[i].getAbsolutePath();
                    String compare = filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
                    if (compare.equals(".mp3") || compare.equals(".wav") || compare.equals(".ogg")){
                        flag = true;
                        break;
                    }
                }

            }
        }
        return flag;
    }


    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon ;
        TextView file_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iconView);
            file_name = (TextView) itemView.findViewById(R.id.fileName);
        }
    }
}
