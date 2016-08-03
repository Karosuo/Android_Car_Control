package com.example.karosuo.gyrocontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.graphics.drawable.ShapeDrawable;

/**
 * Created by karosuo on 31/07/16.
 */
public class DrawingPanelView extends View {

    private ShapeDrawable mDrawable;
    private float[] currentPoint;
    private float[] endPoint;
    private Paint myPaint;

    public DrawingPanelView(Context context) {
        super(context);

        this.myPaint = new Paint();
        myPaint.setColor(Color.CYAN);
        myPaint.setStrokeWidth(5);
        this.currentPoint = new float[]{100,550};
        this.endPoint = new float[]{100,500};
        paintForward(this.endPoint);
    }

    protected void onDraw(Canvas canvas){
        //mDrawable.draw(canvas);
        canvas.drawLine(this.currentPoint[0],this.currentPoint[1],this.endPoint[0],this.endPoint[1],myPaint);
    }

    public void paintForward(float[] endPoint){
        /*this.endPoint[1] += endPoint[1];
        this.endPoint[0] += (endPoint[0] * (-1.0));
        */
        this.currentPoint = this.endPoint;
        this.endPoint[1]--;
        this.invalidate();
        //this.endPoint[1] -= 500;
        //this.invalidate();
        //this.currentPoint = this.endPoint;
    }



}
