package com.csun_comp380_15884.group3.synthesizerapp.envelopeGraph;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.csun_comp380_15884.group3.synthesizerapp.R;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by marvin on 10/6/16.
 */

class Item{
    Item() {

    }

    public String mLabel;
    public Shader mShader;
    public float mStartAngle;
    public float mEndAngle;


}

public class EnvelopeGraph extends View {
    private  Paint mPaint;
    private Rect mRect;
    private RectF mRectF;
    private float mPointerX;
    private float mPointerY;
    private float mPointerSize;

    public EnvelopeGraph(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        getDrawingRect(mRect);
        mRectF.set(mRect);
        // Apply padding.
        mRectF.left += getPaddingLeft();
        mRectF.right -= getPaddingRight();
        mRectF.top += getPaddingTop();
        mRectF.bottom -= getPaddingBottom();

        // Make it square.
        if (mRectF.height() > mRectF.width()) {
            float center = mRectF.centerY();
            mRectF.top = center - mRectF.width() / 2;
            mRectF.bottom = center + mRectF.width() / 2;
        } else {
            float center = mRectF.centerX();
            mRectF.left = center - mRectF.height() / 2;
            mRectF.right = center + mRectF.height() / 2;
        }


        mPointerX = mRectF.centerX();
        mPointerY = mRectF.centerY();
        mPointerSize = mRectF.width()*.5f;

        //canvas.drawCircle(mPointerX, mPointerY, mPointerSize, mPaint);
        for(float i = 0.0f ; i < mRectF.centerX(); i+=1.0f) {
            canvas.drawCircle(i,mRectF.centerY()*2.0f-i,2,mPaint);

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);

        mRectF = new RectF();
        mRect = new Rect();

        mPointerX = 200;
        mPointerY = 100;
        mPointerSize = 100;

    }
}
