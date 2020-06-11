package com.example.classifier;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.net.Uri.parse;

public class Activity2 extends AppCompatActivity {




    public Activity2() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent i = getIntent();
        String final_detected_emotion = i.getExtras().getString("final_detected_emotion");

        final ArrayList<Integer> playlist;


        playlist = new ArrayList<>();
        playlist.add(R.raw.honey);
        playlist.add(R.raw.anger);
        playlist.add(R.raw.contempt);
        playlist.add(R.raw.disgust);
        playlist.add(R.raw.fear);
        playlist.add(R.raw.happy);
        playlist.add(R.raw.sadness);
        playlist.add(R.raw.surprise);


        MediaPlayer mediaPlayer = null;
        Button play = findViewById(R.id.play);
        Button pause = findViewById(R.id.pause);
        Button jumpToMainActivity = findViewById(R.id.jumpToMainActivity);

        switch (final_detected_emotion){
            case "anger":
                mediaPlayer = MediaPlayer.create(this,playlist.get(1));
                break;
            case "contempt":
                mediaPlayer = MediaPlayer.create(this,playlist.get(2));
                break;
            case "disgust":
                mediaPlayer = MediaPlayer.create(this,playlist.get(3));
                break;
            case "fear":
                mediaPlayer = MediaPlayer.create(this,playlist.get(4));
                break;
            case "happy":
                mediaPlayer = MediaPlayer.create(this,playlist.get(5));
                break;
            case "sadness":
                mediaPlayer = MediaPlayer.create(this,playlist.get(6));
                break;
            case "surprise":
                mediaPlayer = MediaPlayer.create(this,playlist.get(7));
                break;
            default :
                mediaPlayer = MediaPlayer.create(this,playlist.get(0));
        }
        final MediaPlayer finalMediaPlayer = mediaPlayer;

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalMediaPlayer.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalMediaPlayer.pause();
            }
        });


        jumpToMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, MainActivity.class);
                finalMediaPlayer.pause();
                finalMediaPlayer.release();
                startActivity(intent);
            }
        });
    }

}
