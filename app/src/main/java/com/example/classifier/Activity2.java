package com.example.classifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.net.Uri.parse;

public class Activity2 extends AppCompatActivity {




    public Activity2() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent i = getIntent();
        String final_detected_emotion = i.getExtras().getString("final_detected_emotion");
        Timer timer;
        final ArrayList<Integer> playlist;
        int i = 0;

        playlist = new ArrayList<>();
        playlist.add(R.raw.honey);
        playlist.add(R.raw.anger);
        playlist.add(R.raw.contempt);
        playlist.add(R.raw.disgust);
        playlist.add(R.raw.fear);
        playlist.add(R.raw.happy);
        playlist.add(R.raw.sadness);
        playlist.add(R.raw.surprise);
        timer = new Timer();

        final MediaPlayer mediaPlayer;
        Button play = findViewById(R.id.play);
        Button pause = findViewById(R.id.pause);
        Button jumpToMainActivity = findViewById(R.id.jumpToMainActivity);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        jumpToMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, MainActivity.class);
                mediaPlayer.pause();
                startActivity(intent);
            }
        });
    }


    private int playsong(String final_detected_emotion, ArrayList<Integer> playlist) {
        int i;
        switch (final_detected_emotion) {
            case "anger":
                i = 1;
                break;
            case "contempt":
                i = 2;
                break;
            case "disgust":
                i = 3;
                break;
            case "fear":
                i = 4;
                break;
            case "happy":
                i = 5;
                break;
            case "sadness":
                i = 6;
                break;
            case "surprise":
                i = 7;
                break;
        }
        final int i1 = i;
        return i1;
    }
}
