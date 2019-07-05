package com.grg.main.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.grg.main.R;

public class BasisView extends android.support.v7.widget.AppCompatImageView {

    private int mTop;

    private int mCurIndex;

    public BasisView(Context context) {
        this(context, null);
    }

    public BasisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100, 0);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                mCurIndex++;
                switch (mCurIndex % 3) {
                    case 0:
                        BasisView.this.setImageResource(R.drawable.scan_circle);
                        break;
                    case 1:
                        BasisView.this.setImageResource(R.drawable.scan_circle1);
                        break;
                    case 2:
                        BasisView.this.setImageResource(R.drawable.scan_circle2);
                        break;
                }
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                BasisView.this.setY(mTop - value);
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mTop = top;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
/*
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        Rect rect = new Rect(0,0,400,220);
        canvas.drawRect(rect,paint);*/


    }


}
