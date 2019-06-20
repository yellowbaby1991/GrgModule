package com.grg.face.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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

    public FrameDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub  
        sh = getHolder();
        sh.addCallback(this);
        sh.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int w, int h) {
        // TODO Auto-generated method stub  

    }

    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub  

    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub  
        clearDraw();
    }

    public void clearDraw() {
        Canvas canvas = sh.lockCanvas();
        if (canvas != null) {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
        }
        sh.unlockCanvasAndPost(canvas);
    }

    public void drawBoundingBox(RectF rectF, int lineWidth, int color) {
        Canvas canvas = sh.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setStrokeWidth(lineWidth);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);//设置空心
     /*   Matrix matrix = new Matrix();
        float[] values = {-1f, 0.0f, 0.0f, 0.0f, 1f, 0.0f, 0.0f, 0.0f, 1.0f};//沿着Y轴对称 https://blog.csdn.net/SJF0115/article/details/7264036
        matrix.setValues(values);
        canvas.setMatrix(matrix);
        canvas.translate(-canvas.getWidth(), 0);//往左平移*/
       /* canvas.drawLine(pts[0], pts[1], pts[2], pts[3], paint);
        canvas.drawLine(pts[2], pts[3], pts[4], pts[5], paint);
        canvas.drawLine(pts[4], pts[5], pts[6], pts[7], paint);
        canvas.drawLine(pts[6], pts[7], pts[0], pts[1], paint);*/
        canvas.drawRect(rectF, paint);
        sh.unlockCanvasAndPost(canvas);
    }

}