package com.example.musicapp;

import static com.example.musicapp.MainActivity.musicFiles;
import static com.example.musicapp.MainActivity.repeatFlag;
import static com.example.musicapp.MainActivity.shufferFlag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class PlayMusicActivity extends AppCompatActivity {
    TextView songName, songArtist, durationPlayed, durationTotal;
    ImageView buttonBack, buttonMenu, buttonshuff, buttonPrev, buttonNext, buttonRepeat, imageArt;
    FloatingActionButton buttonPlayPause;
    SeekBar seekBar;
    int position = -1;
    static  ArrayList<MusicFiles> listMusic = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        init();
        getIntentMethod();
        songName.setText(listMusic.get(position).getTitle());
        songArtist.setText(listMusic.get(position).getArtist());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayMusicActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if (mediaPlayer != null){
                  int currentPosition = mediaPlayer.getCurrentPosition()/1000;
                  seekBar.setProgress(currentPosition);
                  durationPlayed.setText(formatTime(currentPosition));
              }
              handler.postDelayed(this,1000);
            }
        });
        buttonshuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shufferFlag){
                    shufferFlag = false;
                    buttonshuff.setImageResource(R.drawable.baseline_shuffle_off);
                }
                else {
                    shufferFlag = true;
                    buttonshuff.setImageResource(R.drawable.baseline_shuffle_on);
                }
            }
        });
        buttonRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatFlag){
                    repeatFlag = false;
                    buttonRepeat.setImageResource(R.drawable.baseline_repeat_off);
                }
                else {
                    repeatFlag = true;
                    buttonRepeat.setImageResource(R.drawable.baseline_repeat_on);
                }
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    finish();
                }else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadButton();
        prevThreadButton();
        nextThreadButton();
        super.onResume();
    }

    private void nextThreadButton() {
        nextThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                buttonNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextClick();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextClick() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shufferFlag && !repeatFlag){
                position = getRandom(listMusic.size()-1);
            } else if (!shufferFlag && !repeatFlag) {
                position = (position+1)%(listMusic.size());
            }
            uri = Uri.parse(listMusic.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText(listMusic.get(position).getTitle());
            songArtist.setText(listMusic.get(position).getArtist());

            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            buttonPlayPause.setImageResource(R.drawable.baseline_pause);
            mediaPlayer.start();
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shufferFlag && !repeatFlag){
                position = getRandom(listMusic.size()-1);
            } else if (!shufferFlag && !repeatFlag) {
                position = (position+1)%(listMusic.size());
            }
            uri = Uri.parse(listMusic.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText(listMusic.get(position).getTitle());
            songArtist.setText(listMusic.get(position).getArtist());

            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            buttonPlayPause.setImageResource(R.drawable.baseline_play);
        }

    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i+1);
    }

    private void prevClick() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shufferFlag && !repeatFlag){
                position = getRandom(listMusic.size()-1);
            } else if (!shufferFlag && !repeatFlag) {
                position = (position-1) <0 ?(listMusic.size()-1) : (position-1);
            }
            uri = Uri.parse(listMusic.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText(listMusic.get(position).getTitle());
            songArtist.setText(listMusic.get(position).getArtist());

            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            buttonPlayPause.setImageResource(R.drawable.baseline_pause);
            mediaPlayer.start();
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shufferFlag && !repeatFlag){
                position = getRandom(listMusic.size()-1);
            } else if (!shufferFlag && !repeatFlag) {
                position = (position-1) <0 ?(listMusic.size()-1) : (position-1);
            }
            uri = Uri.parse(listMusic.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            songName.setText(listMusic.get(position).getTitle());
            songArtist.setText(listMusic.get(position).getArtist());

            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            buttonPlayPause.setImageResource(R.drawable.baseline_play);
        }
    }

    private void prevThreadButton() {
        prevThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                buttonPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevClick();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void playThreadButton() {
        playThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                buttonPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseClick();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseClick() {
        if (mediaPlayer.isPlaying()){
            buttonPlayPause.setImageResource(R.drawable.baseline_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
        else{
            buttonPlayPause.setImageResource(R.drawable.baseline_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    private String formatTime(int currentPosition){
        String totalOut = "";
        String totalNew = "";
        String second = String.valueOf(currentPosition%60);
        String minute = String.valueOf(currentPosition/60);
        totalOut = minute+":"+second;
        totalNew = minute+":"+"0"+second;
        if (second.length()==1){
            return totalNew;
        }
        return totalOut;
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        listMusic = musicFiles;
        if (listMusic != null){
            buttonPlayPause.setImageResource(R.drawable.baseline_pause);
            uri = Uri.parse(listMusic.get(position).getPath());
        }

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();

        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        metaData(uri);
    }

    public void init(){
        songName = (TextView) findViewById(R.id.songName);
        songArtist = (TextView) findViewById(R.id.artist);
        durationPlayed = (TextView) findViewById(R.id.durationPlayed);
        durationTotal = (TextView) findViewById(R.id.totalDuration);
        buttonBack = (ImageView) findViewById(R.id.btnBack);
        buttonMenu = (ImageView) findViewById(R.id.btnMenu);
        buttonshuff = (ImageView) findViewById(R.id.btnShuffle);
        buttonPrev = (ImageView) findViewById(R.id.btnSkipPrevious);
        buttonNext = (ImageView) findViewById(R.id.btnSkipNext);
        buttonRepeat = (ImageView) findViewById(R.id.btnRepeat);
        imageArt = (ImageView) findViewById(R.id.art);
        buttonPlayPause = (FloatingActionButton) findViewById(R.id.btnPlayPause);
        seekBar = (SeekBar) findViewById(R.id.seekBar0fMusic);
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationtotal = Integer.parseInt(listMusic.get(position).getDuration())/1000;
        durationTotal.setText(formatTime(durationtotal));
        byte[] arrArt = retriever.getEmbeddedPicture();
        if (arrArt != null){
            Glide.with(this).asBitmap().load(arrArt).into(imageArt);
        }
        else{
            Glide.with(this).asBitmap().load(R.drawable.music).into(imageArt);
        }
    }
}