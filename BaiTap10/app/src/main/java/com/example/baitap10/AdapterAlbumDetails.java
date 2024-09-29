package com.example.baitap10;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterAlbumDetails extends RecyclerView.Adapter<AdapterAlbumDetails.MyHolder> {
    private Context context;
    static ArrayList<DTOMusic> albumnFiles;
    View view;

    public AdapterAlbumDetails(Context context, ArrayList<DTOMusic> albumnFiles){
        this.context = context;
        this.albumnFiles = albumnFiles;
    }


    @NonNull
    @Override
    public AdapterAlbumDetails.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.music_items, parent, false);

        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        int index = position;
        holder.albumn_name.setText(albumnFiles.get(position).getTitle());
        byte[] image = new byte[0];
        try {
            image = getAlbumnArt(albumnFiles.get(position).getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (image != null){
            Glide.with(context).asBitmap().load(image).into(holder.albumn_image);
        }else{
            Glide.with(context).load(R.drawable.music_basic).into(holder.albumn_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PlayMusicActivity.class);
                intent.putExtra("sender","albumnDetails");
                intent.putExtra("position",index);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return albumnFiles.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView albumn_image;
        TextView albumn_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            albumn_image = itemView.findViewById(R.id.music_img);
            albumn_name = itemView.findViewById(R.id.musicFileName);
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
