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
public class FrameDraw extends SurfaceView implements SurfaceHolder.Callback {

    protected SurfaceHolder sh;
    private int mWidth;
    private int mHeight;

    public FrameDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub  
        sh = getHolder();
        sh.addCallback(this);
        sh.setFormat(PixelFormat.TRANSPARENT);
        //setRotation(0);
        setZOrderOnTop(true);
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int w, int h) {
        // TODO Auto-generated method stub  
        mWidth = w;
        mHeight = h;
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub  

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

    public void drawBoundingBox(int[] pts, int lineWidth, int color) {
        Canvas canvas = sh.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
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
        canvas.drawLine(pts[0], pts[1], pts[2], pts[3], paint);
        canvas.drawLine(pts[2], pts[3], pts[4], pts[5], paint);
        canvas.drawLine(pts[4], pts[5], pts[6], pts[7], paint);
        canvas.drawLine(pts[6], pts[7], pts[0], pts[1], paint);
        sh.unlockCanvasAndPost(canvas);
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