package com.example.baitap10;

import static com.example.baitap10.AdapterAlbumDetails.albumnFiles;
import static com.example.baitap10.AdapterMusic.musicFiles;
import static com.example.baitap10.Application.ACTION_NEXT;
import static com.example.baitap10.Application.ACTION_PLAY;
import static com.example.baitap10.Application.ACTION_PREVIOUS;
import static com.example.baitap10.Application.CHANNEL_ID_1;
import static com.example.baitap10.Application.CHANNEL_ID_2;
import static com.example.baitap10.MusicFragment.repeatFlag;
import static com.example.baitap10.MusicFragment.shufferFlag;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import android.Manifest;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMusicActivity extends AppCompatActivity implements ServiceConnection, ActionPlaying{
    TextView songName, songArtist, durationPlayed, durationTotal;
    ImageView buttonBack, buttonshuff, buttonPrev, buttonNext, buttonRepeat;
    CircleImageView imageArt;
    FloatingActionButton buttonPlayPause;
    SeekBar seekBar;
    int position = -1;
    static ArrayList<DTOMusic> listMusic = new ArrayList<>();
    static Uri uri;
    //static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    private ObjectAnimator objectAnimator;
    MusicService musicService;
    MediaSessionCompat mediaSessionCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(),"My Audio");
        init();
        RelativeLayout relativeLayout = findViewById(R.id.Container);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        getIntentMethod();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService!=null && fromUser){
                    musicService.seekTo(progress*1000);
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
                if (musicService != null){
                    int currentPosition = musicService.getCurrentPosition()/1000;
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
                if (musicService.isPlaying()){
                    musicService.stop();
                   finish();
                }else {
                    finish();
                }
            }
        });

        objectAnimator = ObjectAnimator.ofFloat(imageArt,"rotation",0f,360f);
        objectAnimator.setDuration(10000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,this, BIND_AUTO_CREATE);
        playThreadButton();
        prevThreadButton();
        nextThreadButton();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
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

    public void nextClick() {
        if (musicService.isPlaying()){
            musicService.stop();
            musicService.realease();
            if (shufferFlag && !repeatFlag){
                position = getRandom(listMusic.size()-1);
            } else if (!shufferFlag && !repeatFlag) {
                position = (position+1)%(listMusic.size());
            }
            uri = Uri.parse(listMusic.get(position).getPath());
           musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(listMusic.get(position).getTitle());
            songArtist.setText(listMusic.get(position).getArtist());

            seekBar.setMax(musicService.getduration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService!= null){
                        int currentPosition = musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.onCompleted();
            showNotification(R.drawable.baseline_pause);
            buttonPlayPause.setImageResource(R.drawable.baseline_pause);
            musicService.start();
        }
        else {
            musicService.stop();
            musicService.realease();
            if (shufferFlag && !repeatFlag){
                position = getRandom(listMusic.size()-1);
            } else if (!shufferFlag && !repeatFlag) {
                position = (position+1)%(listMusic.size());
            }
            uri = Uri.parse(listMusic.get(position).getPath());
             musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(listMusic.get(position).getTitle());
            songArtist.setText(listMusic.get(position).getArtist());

            seekBar.setMax(musicService.getduration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.onCompleted();
            showNotification(R.drawable.baseline_play);
            buttonPlayPause.setImageResource(R.drawable.baseline_play);
        }

    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i+1);
    }

    public void prevClick() {
        if (musicService.isPlaying()){
            musicService.stop();
            musicService.realease();
            if (shufferFlag && !repeatFlag){
                position = getRandom(listMusic.size()-1);
            } else if (!shufferFlag && !repeatFlag) {
                position = (position-1) <0 ?(listMusic.size()-1) : (position-1);
            }
            uri = Uri.parse(listMusic.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(listMusic.get(position).getTitle());
            songArtist.setText(listMusic.get(position).getArtist());

            seekBar.setMax(musicService.getduration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.onCompleted();
            showNotification(R.drawable.baseline_pause);
            buttonPlayPause.setImageResource(R.drawable.baseline_pause);
            musicService.start();
        }
        else {
            musicService.stop();
            musicService.realease();
            if (shufferFlag && !repeatFlag){
                position = getRandom(listMusic.size()-1);
            } else if (!shufferFlag && !repeatFlag) {
                position = (position-1) <0 ?(listMusic.size()-1) : (position-1);
            }
            uri = Uri.parse(listMusic.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(listMusic.get(position).getTitle());
            songArtist.setText(listMusic.get(position).getArtist());

            seekBar.setMax(musicService.getduration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.onCompleted();
            showNotification(R.drawable.baseline_play);
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

    public void playPauseClick() {
        if (musicService.isPlaying()){
            buttonPlayPause.setImageResource(R.drawable.baseline_play);
            showNotification(R.drawable.baseline_play);
            musicService.pause();
            seekBar.setMax(musicService.getduration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition()/1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
        else{
            buttonPlayPause.setImageResource(R.drawable.baseline_pause);
            showNotification(R.drawable.baseline_pause);
            musicService.start();
            seekBar.setMax(musicService.getduration()/1000);
            PlayMusicActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition()/1000;
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
        String sender = getIntent().getStringExtra("sender");
        if (sender != null && sender.equals("albumnDetails")){
            listMusic = albumnFiles;
        }else{
            listMusic = musicFiles;
        }
        if (listMusic != null){
            buttonPlayPause.setImageResource(R.drawable.baseline_pause);
            uri = Uri.parse(listMusic.get(position).getPath());
        }
        showNotification(R.drawable.baseline_pause);
        Intent intent = new Intent(this,MusicService.class);
        intent.putExtra("servicePosition", position);
        startService(intent);

    }

    public void init(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        songName = (TextView) findViewById(R.id.songName);
        songArtist = (TextView) findViewById(R.id.artist);
        durationPlayed = (TextView) findViewById(R.id.durationPlayed);
        durationTotal = (TextView) findViewById(R.id.totalDuration);
        buttonBack = (ImageView) findViewById(R.id.btnBack);
        buttonshuff = (ImageView) findViewById(R.id.btnShuffle);
        buttonPrev = (ImageView) findViewById(R.id.btnSkipPrevious);
        buttonNext = (ImageView) findViewById(R.id.btnSkipNext);
        buttonRepeat = (ImageView) findViewById(R.id.btnRepeat);
        imageArt =  findViewById(R.id.artMusic);
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
            Glide.with(this).asBitmap().load(R.drawable.music_basic).into(imageArt);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallBack(this);
        seekBar.setMax(musicService.getduration()/1000);
        metaData(uri);
        songName.setText(listMusic.get(position).getTitle());
        songArtist.setText(listMusic.get(position).getArtist());
        musicService.onCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }

    void showNotification(int playPauseBtn){
        Intent intent = new Intent(this, PlayMusicActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent prevIntent = new Intent(this,NotificationReceiver.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this,0, prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this,NotificationReceiver.class)
                .setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this,0, pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this,NotificationReceiver.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this,0, nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        byte[] picture = null;
        Bitmap thump = null;
        try {
            picture = getAlbumnArt(listMusic.get(position).getPath());
            if (picture!=null){
                thump = BitmapFactory.decodeByteArray(picture,0,picture.length);
            }else {
                thump = BitmapFactory.decodeResource(getResources(),R.drawable.music_basic);
            }
            Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID_1)
                    .setSmallIcon(playPauseBtn)
                    .setLargeIcon(thump)
                    .setContentTitle(listMusic.get(position).getTitle())
                    .setContentText(listMusic.get(position).getArtist())
                    .addAction(R.drawable.baseline_skip_previous,"Previous",prevPending)
                    .addAction(playPauseBtn,"Pause",pausePending)
                    .addAction(R.drawable.baseline_skip_next,"Next",nextPending)
//                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
//                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setFullScreenIntent(contentIntent,true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOnlyAlertOnce(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0,notification);
        } catch (IOException e) {
            throw new RuntimeException(e);
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