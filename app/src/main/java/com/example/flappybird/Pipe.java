package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Pipe extends BaseObject{
    public static int speed;
    public Pipe(float x, float y,int width,int height){
        super(x,y,width,height);
        speed = 10*AppConstants.SCREEN_WIDTH/1080;
    }

    public void draw(Canvas canvas){
        this.x -= speed;
        canvas.drawBitmap(this.bitmap,this.x,this.y,null);
    }

    public void RandomY(){
        Random random = new Random();
        this.y = random.nextInt((0+this.height/4)+1)-this.height/4;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createScaledBitmap(bitmap,width,height,true);
    }
}
