package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static TextView txt_score,txt_best_score,txt_score_over;
    static RelativeLayout game_over;
    static ImageView playBtn;
    GameView gameView;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        AppConstants.SCREEN_WIDTH = displayMetrics.widthPixels;
        AppConstants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        setContentView(R.layout.activity_main);
        txt_score = findViewById(R.id.txt_score);
        txt_best_score = findViewById(R.id.txt_best_score);
        txt_score_over = findViewById(R.id.txt_score_over);
        game_over = findViewById(R.id.game_over);
        gameView = findViewById(R.id.game_view);
        playBtn = findViewById(R.id.playBtn);

        playBtn.setOnClickListener(v -> {
            gameView.setStart(true);
            txt_score.setVisibility(View.VISIBLE);
            playBtn.setVisibility(View.INVISIBLE);
        });
        game_over.setOnClickListener(v -> {
            playBtn.setVisibility(View.VISIBLE);
            game_over.setVisibility(View.INVISIBLE);
            gameView.setStart(false);
            gameView.reset();
        });

        mediaPlayer = MediaPlayer.create(this,R.raw.song);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }
}