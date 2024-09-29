package com.example.musicapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private ArrayList<MusicFiles> musicFiles;

    public Adapter(Context context, ArrayList<MusicFiles> musicFiles){
        this.musicFiles = musicFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
      if (musicFiles != null){
          int id = position;
          holder.fileName.setText(musicFiles.get(position).getTitle());
          try {
              byte[] image = getAlbumnArt(musicFiles.get(position).getPath());
              if (image != null){
                  Glide.with(context).asBitmap().load(image).into(holder.art);
              }else{
                  Glide.with(context).load(R.drawable.music).into(holder.art);
              }
              holder.itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent = new Intent(context,PlayMusicActivity.class);
                      intent.putExtra("position", id);
                      context.startActivity(intent);

                  }
              });
          } catch (IOException e) {
              throw new RuntimeException(e);
          }
      }

    }

    private void deleteFile(int index, View v){
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(musicFiles.get(index).getId()));
        File file = new File(musicFiles.get(index).getPath());
        boolean deleted = file.delete();
        if (deleted){
            context.getContentResolver().delete(contentUri,null,null);
            musicFiles.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index,musicFiles.size());
        }
    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        ImageView art;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.musicFileName);
            art = (ImageView) itemView.findViewById(R.id.music_img);
        }
    }

    private byte[] getAlbumnArt(String uri) throws IOException {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(uri);
        byte[] arr = metadataRetriever.getEmbeddedPicture();
        metadataRetriever.release();
        return arr;
    }
}
