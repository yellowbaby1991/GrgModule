package com.grg.face.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 用来绘制矩形框的SurfaceView
 */
public class FrameDraw1 extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    protected SurfaceHolder sh;
    private int mWidth;
    private int mHeight;

    public FrameDraw1(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        sh = getHolder();
        sh.addCallback(this);
        sh.setFormat(PixelFormat.TRANSPARENT);
        setRotation(0);
        setZOrderOnTop(true);

    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int w, int h) {
        // TODO Auto-generated method stub
        mWidth = w;
        mHeight = h;
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        new Thread(this).start();//启动新的线程
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    public void clearDraw() {
        Canvas canvas = sh.lockCanvas();
        if (canvas != null) {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
            //canvas.drawColor(Color.BLUE);
        }
        sh.unlockCanvasAndPost(canvas);
    }

    public void drawGraphics(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setStyle(Style.FILL);//设置为实心
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);//设置抗锯齿
        this.draw(canvas, mPaint);
    }

    @Override
    public void run() {
        while (true) {
            Canvas canvas = sh.lockCanvas(null);//SurfaceHolder锁定并获得canvas对象
            this.drawGraphics(canvas);
            sh.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    class Rect implements Runnable {//矩形类
        int x;
        int y;
        int speed = 10;

        public Rect(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                y += speed;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    int i = 100;
    int y = 100;
    public void draw(Canvas canvas, Paint p) {
        Rect rect = new Rect(150, 150);
        canvas.drawCircle(i++, y++, 50, p);//画圆
    }

    public synchronized void drawBoundingBox(int[] pts, int lineWidth, int color) {
     /*   i = pts[0];
        y = pts[1];*/
       /* Canvas canvas = sh.lockCanva  s();
        Paint paint = new Paint();
        paint.setStrokeWidth(lineWidth);
        paint.setColor(color);
        //Rect rect1 = new Rect(0,0,400,220);
        //canvas.drawRect(rect1, paint);
        //canvas.rotate(180,canvas.getWidth(),canvas.getHeight());
        //canvas.translate(0, 100);
        //canvas.drawRect(rect1, paint);
       *//* Matrix matrix = new Matrix();
        float[] values = {1f, 0.0f, 0.0f, 0.0f, -1f, 0.0f, 0.0f, 0.0f, 1.0f};//沿着Y轴对称 https://blog.csdn.net/SJF0115/article/details/7264036
        matrix.setValues(values);
        canvas.setMatrix(matrix);
        canvas.translate(0, -canvas.getHeight());//往上平移*//*

        *//*Matrix matrix = new Matrix();
        float[] values = {1f, 0.0f, 0.0f, 0.0f, -1f, 0.0f, 0.0f, 0.0f, 1.0f};//沿着Y轴对称 https://blog.csdn.net/SJF0115/article/details/7264036
        matrix.setValues(values);
        canvas.setMatrix(matrix);
        canvas.translate(0, -canvas.getHeight());*//*
        canvas.drawLine(pts[0], pts[1], pts[2], pts[3], paint);
        canvas.drawLine(pts[2], pts[3], pts[4], pts[5], paint);
        canvas.drawLine(pts[4], pts[5], pts[6], pts[7], paint);
        canvas.drawLine(pts[6], pts[7], pts[0], pts[1], paint);
        sh.unlockCanvasAndPost(canvas);*/
    }

    public void drawBoundingBox(RectF rectF, int lineWidth, int color) {
        Canvas canvas = sh.lockCanvas();
        Paint paint = new Paint();
        paint.setStrokeWidth(lineWidth);
        paint.setColor(color);
        //Rect rect1 = new Rect(0,0,400,220);
        //canvas.drawRect(rect1, paint);
        //canvas.rotate(180,canvas.getWidth(),canvas.getHeight());
        //canvas.translate(0, 100);
        //canvas.drawRect(rect1, paint);
        Matrix matrix = new Matrix();
        float[] values = {1f, 0.0f, 0.0f, 0.0f, -1f, 0.0f, 0.0f, 0.0f, 1.0f};//沿着Y轴对称 https://blog.csdn.net/SJF0115/article/details/7264036
        matrix.setValues(values);
        canvas.setMatrix(matrix);
        canvas.translate(0, -canvas.getHeight());//往上平移
        canvas.drawRect(rectF,paint);
        sh.unlockCanvasAndPost(canvas);
    }


    public void drawLine() {
        Canvas canvas = sh.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.RED);
        p.setStyle(Style.STROKE);
        //canvas.drawPoint(100.0f, 100.0f, p);
        canvas.drawLine(0, 110, 500, 110, p);
        canvas.drawCircle(110, 110, 10.0f, p);
        sh.unlockCanvasAndPost(canvas);

    }

}