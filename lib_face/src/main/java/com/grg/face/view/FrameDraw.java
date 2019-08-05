package com.grg.face.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;

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
    }

    public void clearDraw() {
        try {
            Canvas canvas = sh.lockCanvas();
            if (canvas != null) {
                Paint paint = new Paint();
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawPaint(paint);
            }
            sh.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void drawBoundingBox(RectF rectF, int lineWidth, int color) {
        drawBoundingBox(rectF, lineWidth, color, 0);
    }

    public void drawBoundingBox(RectF rectF, int lineWidth, int color, int rotate) {
        try {
            Canvas canvas = sh.lockCanvas();
            canvas.rotate(rotate, canvas.getWidth() / 2, canvas.getHeight() / 2);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            Paint paint = new Paint();
            paint.setStrokeWidth(lineWidth);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);//设置空心
            canvas.drawRect(rectF, paint);
            sh.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //用来播放GIF
    private Movie movie;
    // GIF缩放系数
    private float zoom = 1;

    private int gifWidth;

    //绘制GIF
    public void drawGif(RectF rectF, String path, int rotate) {
        if (movie == null) {
            initGif(path);
        }
        try {
            Canvas canvas = sh.lockCanvas();
            canvas.rotate(rotate, canvas.getWidth() / 2, canvas.getHeight() / 2);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.save();
            //计算缩放比例
            if (rectF.left > rectF.right) {
                zoom = (rectF.left - rectF.right) / gifWidth;
            } else {
                zoom = (rectF.right - rectF.left) / gifWidth;
            }
            canvas.scale(zoom, zoom);
            //设置画布
            if (rectF.left > rectF.right) {
                movie.draw(canvas, rectF.right / zoom, rectF.top / zoom);
            } else {
                movie.draw(canvas, rectF.left / zoom, rectF.top / zoom);
            }

            //逐帧绘制图片
            //这里使用时间戳 与总帧数 求余操作，这样 随着时间的推移计算出该播放哪一帧
            movie.setTime((int) (System.currentTimeMillis() % movie.duration()));
            // 恢复之前保存的状态
            canvas.restore();
            sh.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //绘制GIF
    public void drawGif(RectF rectF, String path) {
        drawGif(rectF, path, 0);
    }

    //加载gif
    private void initGif(String path) {
        InputStream open = null;
        try {
            open = getContext().getAssets().open(path);
            // 使用影片对象 处理gif图片
            movie = Movie.decodeStream(open);
            gifWidth = movie.width();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}