package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Bird extends BaseObject{
    private int count, vFlap, idCurrentBitmap;
    private float drop;


    private ArrayList<Bitmap> arrbm = new ArrayList<>();

    public Bird() {
        this.count = 0;
        this.vFlap = 5;
        this.idCurrentBitmap = 0;
        this.drop = 0;
    }

    public void draw(Canvas canvas){
        drop();
        canvas.drawBitmap(this.getBitmap(),this.x,this.y,null);
    }

    private void drop() {
        this.drop += 0.6;
        this.y += this.drop;
    }

    public ArrayList<Bitmap> getArrbm() {
        return arrbm;
    }

    public void setArrbm(ArrayList<Bitmap> arrbm) {
        this.arrbm = arrbm;
        for (int i = 0; i < arrbm.size();i++){
            this.arrbm.set(i,Bitmap.createScaledBitmap(this.arrbm.get(i),this.width,this.height,true));
        }
    }

    @Override
    public Bitmap getBitmap() {
        count++;
        if (this.count == this.vFlap){
            for (int i = 0;i< arrbm.size();i++){
                if (i == arrbm.size()-1){
                    this.idCurrentBitmap = 0;
                    break;
                }else if (this.idCurrentBitmap == i){
                    idCurrentBitmap = i+1;
                    break;
                }
            }
            count = 0;
        }
        if (this.drop<0){
            Matrix matrix = new Matrix();
            matrix.postRotate(-25);
            return Bitmap.createBitmap(arrbm.get(idCurrentBitmap),0,0,arrbm.get(idCurrentBitmap).getWidth(),arrbm.get(idCurrentBitmap).getHeight(),matrix,true);
        }else if (drop>=0){
            Matrix matrix = new Matrix();
            if (drop<70){
                matrix.postRotate(-25+drop*2);
            }else {
                matrix.postRotate(45);
            }
            return Bitmap.createBitmap(arrbm.get(idCurrentBitmap),0,0,arrbm.get(idCurrentBitmap).getWidth(),arrbm.get(idCurrentBitmap).getHeight(),matrix,true);
        }
        return this.arrbm.get(idCurrentBitmap);
    }

    public void setDrop(float drop) {
        this.drop = drop;
    }

    public float getDrop() {
        return drop;
    }
}
