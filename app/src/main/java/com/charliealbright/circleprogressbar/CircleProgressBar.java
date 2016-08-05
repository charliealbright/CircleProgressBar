package com.charliealbright.circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by charlie.albright on 8/4/16.
 */
public class CircleProgressBar extends View {

    // VALUES
    private int mProgress = 0;
    private int mMin = 0;
    private int mMax = 100;

    // DIMENS
    private float mStrokeWidth = 4f;

    // COLORS
    private int mProgressColor = Color.DKGRAY;
    private int mTrackColor = Color.LTGRAY;

    // ALPHAS
    private float mProgressAlpha = -1.0f;
    private float mTrackAlpha = -1.0f;

    // STYLES
    private CapStyle mProgressCapStyle = CapStyle.ROUND; // Defaults to Paint.Cap.ROUND

    // Local fields
    private int mStartAngle = -90; // Vertical
    private RectF mRectF;
    private Paint mTrackPaint;
    private Paint mProgressPaint;

    public enum CapStyle {
        BUTT,
        ROUND,
    }

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupStyle(context, attrs);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupStyle(context, attrs);
    }

    private void setupStyle(Context context, AttributeSet attributeSet) {
        mRectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CircleProgressBar,
                0, 0);
        //Reading values from the XML layout
        try {
            mProgress = typedArray.getInt(R.styleable.CircleProgressBar_progress, mProgress);
            mMin = typedArray.getInt(R.styleable.CircleProgressBar_min, mMin);
            mMax = typedArray.getInt(R.styleable.CircleProgressBar_max, mMax);

            if (mProgress > mMax) {
                throw new ProgressOutOfBoundsException("The progress can not be set higher than the max value. Did you forget to set a custom max value?");
            }
            if (mProgress < mMin) {
                throw new ProgressOutOfBoundsException("The progress can not be set lower than the min value. Did you forget to set a custom min value?");
            }

            mStrokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_trackWidth, mStrokeWidth);

            mProgressColor = typedArray.getInt(R.styleable.CircleProgressBar_progressColor, mProgressColor);
            mTrackColor = typedArray.getInt(R.styleable.CircleProgressBar_trackColor, mTrackColor);

            mProgressAlpha = typedArray.getFloat(R.styleable.CircleProgressBar_progressAlpha, mProgressAlpha);
            mTrackAlpha = typedArray.getFloat(R.styleable.CircleProgressBar_trackAlpha, mTrackAlpha);

            mProgressCapStyle = CapStyle.values()[typedArray.getInt(R.styleable.CircleProgressBar_progressCapStyle, mProgressCapStyle.ordinal())];
        } finally {
            typedArray.recycle();
        }

        mTrackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrackPaint.setColor(mTrackColor);
        if (mTrackAlpha != -1.0f) {
            // Set custom track alpha
            mTrackPaint.setAlpha((int) Math.ceil(mTrackAlpha * 255));
        }
        mTrackPaint.setStyle(Paint.Style.STROKE);
        mTrackPaint.setStrokeWidth(mStrokeWidth);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mProgressColor);
        if (mProgressAlpha != -1.0f) {
            // Set custom progress alpha
            mProgressPaint.setAlpha((int) Math.ceil(mProgressAlpha * 255));
        }
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.values()[mProgressCapStyle.ordinal()]);
        mProgressPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        mRectF.set(0 + mStrokeWidth / 2, 0 + mStrokeWidth / 2, min - mStrokeWidth / 2, min - mStrokeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(mRectF, mTrackPaint);
        float angle = 360 * mProgress / mMax;
        canvas.drawArc(mRectF, mStartAngle, angle, false, mProgressPaint);

    }

    public void setProgress(int progress) {
        if (progress > mMax) {
            throw new ProgressOutOfBoundsException("The progress can not be set higher than the max value. Did you forget to set a custom max value?");
        } else if (progress < mMin) {
            throw new ProgressOutOfBoundsException("The progress can not be set lower than the min value. Did you forget to set a custom min value?");
        } else {
            this.mProgress = progress;
            invalidate();
        }
    }
}
