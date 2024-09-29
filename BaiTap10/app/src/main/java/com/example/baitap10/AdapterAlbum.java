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

public class AdapterAlbum extends RecyclerView.Adapter<AdapterAlbum.MyViewHolder> {
    private Context context;
    private ArrayList<DTOMusic> albumnFiles = new ArrayList<>();
    View view;

    public AdapterAlbum(Context context, ArrayList<DTOMusic> albumnFiles){
        this.context = context;
        this.albumnFiles = albumnFiles;
    }


    @NonNull
    @Override
    public AdapterAlbum.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.album_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAlbum.MyViewHolder holder, int position) {
        int index = position;
        holder.albumn_name.setText(albumnFiles.get(position).getAlbumn());
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
                Intent intent = new Intent(context, AlbumDetailActivity.class);
                intent.putExtra("albumName",albumnFiles.get(index).getAlbumn());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumnFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView albumn_image;
        TextView albumn_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            albumn_image = itemView.findViewById(R.id.albumImage);
            albumn_name = itemView.findViewById(R.id.albumName);
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
