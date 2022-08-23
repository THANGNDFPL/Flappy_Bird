package com.example.flappybird;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class GameView extends View {
    private Bird bird;
    private Handler handler;
    private Runnable runnable;
    private ArrayList<Pipe> arrPipe;
    private int sumPipe,distance;
    private int score,bestScore = 0;
    private boolean start;
    private Context context;
    private int soundJump;
    private float volume;
    private boolean loadedsound;
    private SoundPool soundPool;
    public GameView(Context context, AttributeSet atrrs){
        super(context,atrrs);
        this.context = context;
        score = 0;
        SharedPreferences sp = context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
        if (sp != null){
            bestScore = sp.getInt("bestscore",0);
        }
        start = false;
        initBird();
        initPipe();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        if (Build.VERSION.SDK_INT >= 21){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool = builder.build();
        }else{
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadedsound = true;
            }
        });

        soundJump = this.soundPool.load(context,R.raw.jump_02,1);
    }

    public void initBird(){
        bird = new Bird();
        bird.setWidth(100*AppConstants.SCREEN_WIDTH/1080);
        bird.setHeight(100*AppConstants.SCREEN_HEIGHT/1920);
        bird.setX(100*AppConstants.SCREEN_WIDTH/1080);
        bird.setY(AppConstants.SCREEN_HEIGHT/2 - bird.getHeight()/2);
        ArrayList<Bitmap> arrbm = new ArrayList<>();
        arrbm.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird_frame1));
        arrbm.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird_frame2));
        arrbm.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird_frame3));
        arrbm.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird_frame4));
        bird.setArrbm(arrbm);
    }

    public void initPipe(){
        sumPipe = 6;
        distance = 300*AppConstants.SCREEN_HEIGHT/1920;
        arrPipe = new ArrayList<>();
        for (int i = 0;i < sumPipe;i++){
            if (i<sumPipe/2){
                this.arrPipe.add(new Pipe(AppConstants.SCREEN_WIDTH+i*((AppConstants.SCREEN_WIDTH+200*AppConstants.SCREEN_WIDTH/1080)/(sumPipe/2)),
                        0,200*AppConstants.SCREEN_WIDTH/1000,AppConstants.SCREEN_HEIGHT/2));
                this.arrPipe.get(this.arrPipe.size()-1).setBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.pipe1));
                this.arrPipe.get(this.arrPipe.size()-1).RandomY();
            }else{
                this.arrPipe.add(new Pipe(this.arrPipe.get(i-sumPipe/2).getX(),
                        this.arrPipe.get(i-sumPipe/2).getY()+this.arrPipe.get(i-sumPipe/2).getHeight() + this.distance,
                        200*AppConstants.SCREEN_WIDTH/1080,
                        AppConstants.SCREEN_HEIGHT/2));
                this.arrPipe.get(this.arrPipe.size()-1).setBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.pipe2));
            }
        }
    }

    public void draw (Canvas canvas){
        super.draw(canvas);
        if (start){
            bird.draw(canvas);
            for (int i = 0;i < sumPipe;i++){

                if (bird.getRect().intersect(arrPipe.get(i).getRect())
                        || bird.getY()-bird.getHeight()<0
                        || bird.getY()>AppConstants.SCREEN_HEIGHT){
                    Pipe.speed = 0;
                    MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_best_score.setText("Best Score: " + bestScore);
                    MainActivity.txt_score.setVisibility(INVISIBLE);
                    MainActivity.game_over.setVisibility(VISIBLE);
                }

                if (this.bird.getX()+this.bird.getWidth()>arrPipe.get(i).getX()+arrPipe.get(i).getWidth()/2
                        && this.bird.getX()+this.bird.getWidth()<=arrPipe.get(i).getX()+arrPipe.get(i).getWidth()/2+Pipe.speed
                        && i< sumPipe/2){
                    score++;
                    if (score>bestScore){
                        bestScore = score;
                        SharedPreferences sp = context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("bestscore",bestScore);
                        editor.apply();
                    }
                    MainActivity.txt_score.setText(""+score);
                }
                if (this.arrPipe.get(i).getX()<-arrPipe.get(i).getWidth()){
                    this.arrPipe.get(i).setX(AppConstants.SCREEN_WIDTH);
                    if (i < sumPipe/2){
                        arrPipe.get(i).RandomY();
                    }else {
                        arrPipe.get(i).setY(this.arrPipe.get(i-sumPipe/2).getY()
                                +this.arrPipe.get(i-sumPipe/2).getHeight()
                                +this.distance);
                    }
                }
                this.arrPipe.get(i).draw(canvas);
            }
        }else {
            if (bird.getY()>AppConstants.SCREEN_HEIGHT/2){
                bird.setDrop(-15*AppConstants.SCREEN_HEIGHT/1920);
            }
            bird.draw(canvas);
        }
        handler.postDelayed(runnable,10);
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            bird.setDrop(-15);
            if (loadedsound){
                int streamId = this.soundPool.play(this.soundJump,(float) 0.5,(float) 0.5,1,0,1f);
            }
        }
        return true;
    }

    public void reset(){
        MainActivity.txt_score.setText("0");
        score = 0;
        initBird();
        initPipe();
    }
}
