package com.csun_comp380_15884.group3.synthesizerapp.envelopeGraph;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.csun_comp380_15884.group3.synthesizerapp.R;

import java.lang.ref.SoftReference;
import java.math.MathContext;
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

    private RectF mInnerRectF;

    private float mPointerX;
    private float mPointerY;
    private float mPointerSize;

    private float mTotalTime;
    private float mXa, mYa, mSa, mXd, mSd, mYs, mXr, mSr;

    private float mScale;

    private float mValueAtTouchA;


    private float mIncrement;

    private float mXAtTouch;
    private float mYAtTouch;


    private Path path;

    float [] points;

    boolean aXYSelected;
    boolean dXYSelected;
    boolean rXSelected;



    public EnvelopeGraph(Context context, AttributeSet attrs) {
        super(context, attrs);



        mXa = 100;
        mYa = 100;

        mXd = 100;
        mYs = 100;

        mXr = 100;

        aXYSelected = false;

        path = new Path();


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


        mInnerRectF.set(mRectF);


        mSa = 10.f;
        mSd = 1.f;
        mSr = 1.0f;

        mTotalTime = mXa+mXd+mXr;

        canvas.translate(0,mRectF.height());
        canvas.scale(1,-1);

        if(points == null)
        {
            points = new float[(int)mRectF.width()*8];
        }

        float Y1 = 0.0f;
        float Y2 = 0.0f;
        int counter = 0;
        for(float i = 0; i < mXa ; i+=mIncrement) {
            Y1 = (float) Math.pow(i / mXa, mSa) * mYa;
            Y2 = (float) Math.pow((i + mIncrement) / mXa, mSa) * mYa;

            if(Y1 >= mYa)
            {
                Y1 = mYa;
            }
            if(Y2 >= mYa)
            {
                Y2 = mYa;
            }
            points[counter] = i;
            points[counter+1] = Y1;
            points[counter+2] = i+mIncrement;
            points[counter+3] = Y2;
            //canvas.drawLine(i,Y1,i+mIncrement,Y2,mPaint);
            counter+=4;
        }



        canvas.drawRect(mRectF,mPaint);

        canvas.drawCircle(mXa,mYa,mPointerSize,mPaint);


        for(float i = 0; i < mXd ; i += mIncrement)
        {
            if(mYa < mYs)
            {
                Y1 = mYa + (float)Math.pow(i/mXd,mSd)*(mYs-mYa);
                Y2 = mYa + (float)Math.pow((i+mIncrement)/mXd,mSd)*(mYs-mYa);
                if(Y1 >= mYs)
                {
                    Y1 = mYs;
                }
                if(Y2 >= mYs)
                {
                    Y2 = mYs;
                }
            }
            else if(mYs < mYa)
            {
                Y1 = mYa - (float)Math.pow(i/mXd,mSd)*(mYa-mYs);
                Y2 = mYa - (float)Math.pow((i+mIncrement)/mXd,mSd)*(mYa-mYs);

                if(Y1 <= mYs)
                {
                    Y1 = mYs;
                }
                if(Y2 <= mYs)
                {
                    Y2 = mYs;
                }
            }
            else{
                Y1 = mYs;
                Y2 = mYs;
            }




            //canvas.drawLine(i+mXa,Y1,i+mXa+mIncrement,Y2,mPaint);
            points[counter] = i+mXa;
            points[counter+1] = Y1;
            points[counter+2] = i+mIncrement+mXa;
            points[counter+3] = Y2;
            counter+=4;

        }

        canvas.drawCircle(mXa+mXd,mYs,mPointerSize,mPaint);


        for(float i = 0; i < mXr ; i += mIncrement)
        {

            Y1 = mYs - (float)Math.pow(i/mXr,mSd)*mYs;
            Y2 = mYs - (float)Math.pow((i+mIncrement)/mXr,mSd)*mYs;


            if(Y1 <= 0.0f)
            {
                Y1 = 0.0f;
            }
            if(Y2 <= 0.0f)
            {
                Y2 = 0.0f;
            }
            //canvas.drawLine(i+mXa+mXd,Y1,i+mXa+mXd+mIncrement,Y2,mPaint);
            points[counter] = i+mXa+mXd;
            points[counter+1] = Y1;
            points[counter+2] = i+mIncrement+mXa+mXd;
            points[counter+3] = Y2;
            counter+=4;

        }

        canvas.drawCircle(mXa+mXd+mXr,0,mPointerSize,mPaint);

        canvas.drawLines(points,0,counter,mPaint);




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float valueX = 0.0f;
        float valueY = 0.0f;
        float epsilon = mPointerSize*3;
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                // Just record the current finger position.
                mXAtTouch = event.getX();
                mYAtTouch = event.getY();

                if((valueX = Math.abs(mXAtTouch-mXa)) < epsilon && (valueY = Math.abs(mYAtTouch-(mRectF.height()-mYa)))<epsilon)
                {
                    aXYSelected = true;
                }
                else if((valueX = Math.abs(mXAtTouch-mXa-mXd)) < epsilon && (valueY = Math.abs(mYAtTouch-(mRectF.height()-mYs)))<epsilon)
                {
                    dXYSelected = true;
                }
                else if((valueX = Math.abs(mXAtTouch-mXa-mXd-mXr)) < epsilon)
                {
                    rXSelected = true;
                }

                getDrawingRect(mRect);
                break;
            }

            case MotionEvent.ACTION_MOVE: {

                epsilon/=2;
                if(aXYSelected)
                {
                    mXa = event.getX();
                    mYa = mRectF.height() - event.getY();
                    if(mXa <= mIncrement)
                    {
                        mXa = mIncrement;
                    }
                    else if(mXa > mRectF.width()-mXd-mXr-mPointerSize)
                    {
                        mXa = mRectF.width()-mXd-mXr-mPointerSize;
                    }
                    if(mYa > mRectF.height())
                    {
                        mYa = mRectF.height();
                    }
                    else if (mYa <= 0)
                    {
                        mYa = 0;
                    }
                }
                else if(dXYSelected)
                {
                    mXd = event.getX();
                    mYs = mRectF.height() - event.getY();

                    if(mXd <= mIncrement)
                    {
                        mXd = mIncrement;
                    }
                    else if(mXd > mRectF.width()-mXa-mXr-mPointerSize)
                    {
                        mXd =mRectF.width()-mXa-mXr-mPointerSize;
                    }
                    if(mYs > mRectF.height())
                    {
                        mYs = mRectF.height();
                    }
                    else if (mYs <= 0)
                    {
                        mYs = 0;
                    }
                }
                else if(rXSelected)
                {
                    mXr = event.getX();
                    if(mXr <= mIncrement)
                    {
                        mXr = mIncrement;
                    }
                    else if(mXr > mRectF.width()-mXa-mXd-mPointerSize)
                    {
                        mXr = mRectF.width()-mXa-mXd-mPointerSize;
                    }
                }

                /*
                float xyDelta = event.getX() - event.getY() - xyAtTouch_;
                knobValue_ = valueAtTouch_ + sensitivity_ * xyDelta;
                knobValue_ = Math.min(knobValue_, 1.0f);
                knobValue_ = Math.max(knobValue_, 0.0f);


                // Notify listener and redraw.
                if (listener_ != null) {
                    listener_.onKnobChanged(this,getValue());
                }
                */
                invalidate();

                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                aXYSelected = false;
                dXYSelected = false;
                rXSelected = false;
            /*{
                if (listenerUp_ != null) {
                    listenerUp_.onKnobChanged(this,getValue());
                }
                break;
            }
            */
                break;
        }
        return true;
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
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2);


        mRectF = new RectF();
        mRect = new Rect();
        mInnerRectF =new RectF();

        mPointerX = 200;
        mPointerY = 100;
        mPointerSize = 10;

        mIncrement = 4.0f;


    }
}
