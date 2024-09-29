package com.example.bt05;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    ArrayList<Music> listMusic;
    MediaPlayer mediaPlayer;
    int posIsPlay;

    public Adapter(Context context, ArrayList<Music> listMusic) {
        this.context = context;
        this.listMusic = listMusic;
        this.posIsPlay = -1;
        this.mediaPlayer = new MediaPlayer();
    }

    public void UpdateTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        listMusic.get(posIsPlay).setBtnPlay(R.drawable.play_25);
                        Adapter.this.notifyItemChanged(posIsPlay);
                    }
                });
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameMusic.setText((String) listMusic.get(position).getName());
        holder.locationMusic.setText((String) listMusic.get(position).getLocation());
        holder.btnPlay.setImageResource((int) listMusic.get(position).getBtnPlay());

        String url = (String) listMusic.get(position).getLocation();
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posIsPlay != position) {
                    if(mediaPlayer.isPlaying()) {
                        listMusic.get(posIsPlay).setBtnPlay(R.drawable.play_25);
                        Adapter.this.notifyItemChanged(posIsPlay);
                    }
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    posIsPlay = position;
                    mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
                }else if(posIsPlay == -1) {
                    posIsPlay = position;
                    mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
                }

                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    holder.btnPlay.setImageResource(R.drawable.play_25);
                    listMusic.get(position).setBtnPlay(R.drawable.play_25);
                }else {
                    mediaPlayer.start();
                    holder.btnPlay.setImageResource(R.drawable.pause_25);
                    listMusic.get(position).setBtnPlay(R.drawable.pause_25);
                }
                UpdateTime();
            }
        });

        holder.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posIsPlay == position){
                    if(mediaPlayer.isPlaying()) {
                        holder.btnPlay.setImageResource(R.drawable.play_25);
                        listMusic.get(position).setBtnPlay(R.drawable.play_25);
                    }
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    posIsPlay = -1;
                    mediaPlayer = new MediaPlayer();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMusic == null ? 0 : listMusic.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameMusic, locationMusic;
        ImageButton btnPlay, btnStop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameMusic = (TextView) itemView.findViewById(R.id.nameMusic);
            locationMusic = (TextView) itemView.findViewById(R.id.locationMusic);
            btnPlay = (ImageButton) itemView.findViewById(R.id.btnPlay);
            btnStop = (ImageButton) itemView.findViewById(R.id.btnStop);
        }
    }
}
