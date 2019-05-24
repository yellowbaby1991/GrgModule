package com.grg.face;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 *人脸框绘制View
 * 
 *@author zjxin2  on 2016-11-4
 *@version  
 *
 */
public class FaceView extends View {
	private static final String TAG = "FaceView";
	private Paint mLinePaint;
	private List<RectF> mRectList = new ArrayList<RectF>();
	private Matrix mMatrix = new Matrix();
	// private RectF mRect = new RectF();
	private Drawable mFaceIndicator = null;

	public FaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initPaint();
		//mFaceIndicator = getResources().getDrawable(R.drawable.face_frame);
	}

	public void setFaces(RectF rectf) {
		mRectList.add(rectf);
		invalidate();
	}

	public void clearFaces() {
		mRectList.clear();
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mRectList.size() > 0) {
			Util.prepareMatrix(mMatrix, 0, 0, getWidth(), getHeight());
			canvas.save();
			mMatrix.postRotate(0); // Matrix.postRotate默认是顺时针
			// canvas.rotate(-0); //Canvas.rotate()默认是逆时针
			for (int i = 0; i < mRectList.size(); i++) {
				RectF rectf = mRectList.get(i);
				// GrgLog.i(TAG, "onDraw : rectf (" + rectf.left + ", " +
				// rectf.top + ", " + rectf.right + ", " + rectf.bottom + ")");
				// mMatrix.mapRect(rectf);
				//mFaceIndicator.setBounds(Math.round(rectf.left), Math.round(rectf.top), Math.round(rectf.right),Math.round(rectf.bottom));
				// mFaceIndicator.setBounds(Math.round(rectf.left -
				// CamManager.W_H_PLUS), Math.round(rectf.top -
				// CamManager.W_H_PLUS - 10),
				// Math.round(rectf.right + CamManager.W_H_PLUS),
				// Math.round(rectf.bottom + CamManager.W_H_PLUS - 10));
				//mFaceIndicator.draw(canvas);
				canvas.drawRect(rectf, mLinePaint);
			}
			canvas.restore();
		}

		super.onDraw(canvas);
	}

	private void initPaint() {
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// int color = Color.rgb(0, 150, 255);
		int color = Color.rgb(98, 212, 68);
		mLinePaint.setColor(Color.WHITE);
		//mLinePaint.setColor(color);
		mLinePaint.setStyle(Style.STROKE);
		mLinePaint.setStrokeWidth(2f);
		mLinePaint.setAlpha(180);
	}
}
