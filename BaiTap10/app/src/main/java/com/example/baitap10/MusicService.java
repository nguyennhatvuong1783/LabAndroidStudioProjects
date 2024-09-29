package com.example.baitap10;

import static com.example.baitap10.PlayMusicActivity.listMusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<DTOMusic> musicsFile = new ArrayList<>();
    Uri uri;
    int position=-1;
    ActionPlaying actionPlaying;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind","Method");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition",-1);
        String actionName = intent.getStringExtra("ActionName");
        if (myPosition!=-1){
            playMusic(myPosition);
        }
        if (actionName!=null){
            switch (actionName){
                case "playPause":
                    if (actionPlaying!=null){
                        actionPlaying.playPauseClick();
                    }
                    break;
                case "next":
                    if (actionPlaying!=null){
                        actionPlaying.nextClick();
                    }
                    break;
                case "previous":
                    if (actionPlaying!=null){
                        actionPlaying.prevClick();
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    private void playMusic(int Startposition) {
        musicsFile =  listMusic;
        position = Startposition;
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicsFile!=null){
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        }else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (actionPlaying!=null){
          actionPlaying.nextClick();
        }
        createMediaPlayer(position);
        mediaPlayer.start();
        onCompleted();

    }


    public class MyBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }

    void start(){
        mediaPlayer.start();
    }

    Boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    void realease(){
        mediaPlayer.release();
    }

    void stop(){
        mediaPlayer.stop();
    }

    int getduration(){
        return mediaPlayer.getDuration();
    }

    int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    void seekTo(int position){
        mediaPlayer.seekTo(position);
    }

    void createMediaPlayer(int position){
        uri = Uri.parse(musicsFile.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(),uri);
    }

    void pause(){
        mediaPlayer.pause();
    }

    void onCompleted(){
        mediaPlayer.setOnCompletionListener(this);
    }

    void setCallBack(ActionPlaying actionPlaying){
        this.actionPlaying=actionPlaying;
    }

}
