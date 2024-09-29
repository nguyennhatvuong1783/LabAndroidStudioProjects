package com.example.baitap10;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AdapterMusic extends RecyclerView.Adapter<AdapterMusic.ViewHolder2> {

    private Context context;
    static ArrayList<DTOMusic> musicFiles;

    public AdapterMusic(Context context, ArrayList<DTOMusic> musicFiles){
        this.musicFiles = musicFiles;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMusic.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_items, parent, false);
        return new ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMusic.ViewHolder2 holder, int position) {
      if (musicFiles != null){
          int id = position;
          holder.fileName.setText(musicFiles.get(position).getTitle());
          try {
              byte[] image = getAlbumnArt(musicFiles.get(position).getPath());
              if (image != null){
                  Glide.with(context).asBitmap().load(image).into(holder.art);
              }else{
                  Glide.with(context).load(R.drawable.music_basic).into(holder.art);
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


    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView fileName;
        ImageView art;
        public ViewHolder2(@NonNull View itemView) {
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

    void updateList(ArrayList<DTOMusic> musicArrayList){
        musicFiles = new ArrayList<>();
        musicFiles.addAll(musicArrayList);
        notifyDataSetChanged();
    }
}
