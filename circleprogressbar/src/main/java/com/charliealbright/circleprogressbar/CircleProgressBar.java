package com.charliealbright.circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by charlie.albright on 8/4/16.
 */
public class CircleProgressBar extends View {

    private static final float DEFAULT_TRACK_WIDTH_DP = 5f;

    // VALUES
    private int mProgress = 0;
    private int mMin = 0;
    private int mMax = 100;

    // DIMENS
    private DisplayMetrics mDisplayMetrics;
    private float mStrokeWidth;

    // COLORS
    private int mProgressColor = Color.DKGRAY;
    private int mTrackColor = Color.LTGRAY;

    // ALPHAS
    private float mProgressAlpha = -1.0f;
    private float mTrackAlpha = -1.0f;

    // STYLES
    private CapStyle mProgressCapStyle = CapStyle.ROUND; // Defaults to Paint.Cap.ROUND
    private StartPosition mStartPosition = StartPosition.TOP; // Defaults to start at top position

    // Local fields
    private int mStartAngle = -90; // Vertical
    private RectF mRectF;
    private Paint mTrackPaint;
    private Paint mProgressPaint;

    public enum CapStyle {
        FLAT,
        ROUND
    }

    public enum StartPosition {
        TOP(270),
        BOTTOM(90),
        LEFT(180),
        RIGHT(0);

        private int value;

        StartPosition(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
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
        mDisplayMetrics = context.getResources().getDisplayMetrics();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CircleProgressBar,
                0, 0);
        //Reading values from the XML layout
        try {
            mProgress = typedArray.getInt(R.styleable.CircleProgressBar_progress, mProgress);
            mMin = typedArray.getInt(R.styleable.CircleProgressBar_min, mMin);
            mMax = typedArray.getInt(R.styleable.CircleProgressBar_max, mMax);

            // TODO: min/max validation

            if (mProgress > mMax) {
                throw new OutOfBoundsException("The progress can not be set higher than the max value. Did you forget to set a custom max value?");
            }
            if (mProgress < mMin) {
                throw new OutOfBoundsException("The progress can not be set lower than the min value. Did you forget to set a custom min value?");
            }

            mStrokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_trackWidth, dpToPx(DEFAULT_TRACK_WIDTH_DP));

            mProgressColor = typedArray.getInt(R.styleable.CircleProgressBar_progressColor, mProgressColor);
            mTrackColor = typedArray.getInt(R.styleable.CircleProgressBar_trackColor, mTrackColor);

            mProgressAlpha = typedArray.getFloat(R.styleable.CircleProgressBar_progressAlpha, mProgressAlpha);
            mTrackAlpha = typedArray.getFloat(R.styleable.CircleProgressBar_trackAlpha, mTrackAlpha);

            if (!(mProgressAlpha == -1.0f || (mProgressAlpha >= 0.0f && mProgressAlpha <= 1.0f))) {
                throw new OutOfBoundsException("The progress alpha has been set to an unsupported value. Please ensure the alpha is between 0.0 and 1.0.");
            }

            if (!(mTrackAlpha == -1.0f || (mTrackAlpha >= 0.0f && mTrackAlpha <= 1.0f))) {
                throw new OutOfBoundsException("The track alpha has been set to an unsupported value. Please ensure the alpha is between 0.0 and 1.0.");
            }

            mProgressCapStyle = CapStyle.values()[typedArray.getInt(R.styleable.CircleProgressBar_progressCapStyle, mProgressCapStyle.ordinal())];
            int startAngle = typedArray.getInt(R.styleable.CircleProgressBar_startingPosition, mStartPosition.getValue());
            for (StartPosition startPosition : StartPosition.values()) {
                if (startAngle == startPosition.getValue()) {
                    mStartPosition = startPosition;
                }
            }

        } finally {
            typedArray.recycle();
        }

        refreshStyle();
    }

    private void refreshStyle() {
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

        refreshStyle();

        canvas.drawOval(mRectF, mTrackPaint);
        float angle = 360 * mProgress / mMax;
        canvas.drawArc(mRectF, mStartPosition.getValue(), angle, false, mProgressPaint);

    }

    // #########################
    // ## GETTERS AND SETTERS ##
    // #########################

    public void setProgress(int progress) {
        if (progress > mMax) {
            throw new OutOfBoundsException("The progress can not be set higher than the max value. Did you forget to set a custom max value?");
        } else if (progress < mMin) {
            throw new OutOfBoundsException("The progress can not be set lower than the min value. Did you forget to set a custom min value?");
        } else {
            this.mProgress = progress;
            invalidate();
        }
    }

    public int getProgress() {
        return mProgress;
    }

    public void setMin(int min) {
        // TODO: validation of input
        mMin = min;
        invalidate();
    }

    public int getMin() {
        return mMin;
    }

    public void setMax(int max) {
        // TODO: validation of input
        mMax = max;
        invalidate();
    }

    public int getMax() {
        return mMax;
    }

    public void setTrackWidth(float widthInDips) {
        mStrokeWidth = dpToPx(widthInDips);
        invalidate();
    }

    public float getTrackWidth() {
        return pxToDp(mStrokeWidth);
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
        invalidate();
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setTrackColor(int color) {
        mTrackColor = color;
        invalidate();
    }

    public int getTrackColor() {
        return mTrackColor;
    }

    public void setProgressAlpha(float alpha) {
        if (alpha >= 0.0f && alpha <= 1.0f) {
            mProgressAlpha = alpha;
            invalidate();
        } else {
            throw new OutOfBoundsException("The progress alpha has been set to an unsupported value. Please ensure the alpha is between 0.0 and 1.0.");
        }
    }

    public float getProgressAlpha() {
        return mProgressAlpha;
    }

    public void setTrackAlpha(float alpha) {
        if (alpha >= 0.0f && alpha <= 1.0f) {
            mTrackAlpha = alpha;
            invalidate();
        } else {
            throw new OutOfBoundsException("The track alpha has been set to an unsupported value. Please ensure the alpha is between 0.0 and 1.0.");
        }
    }

    public float getTrackAlpha() {
        return mTrackAlpha;
    }

    public void setProgressCapStyle(CapStyle capStyle) {
        mProgressCapStyle = capStyle;
        invalidate();
    }

    public CapStyle getProgressCapStyle() {
        return mProgressCapStyle;
    }

    public void setStartPosition(StartPosition startPosition) {
        mStartPosition = startPosition;
    }

    public StartPosition getStartPosition() {
        return mStartPosition;
    }

    // #######################
    // ## UTILITY FUNCTIONS ##
    // #######################

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mDisplayMetrics);
    }

    private float pxToDp(float px) {
        return (float)Math.ceil(px / mDisplayMetrics.density);
    }
}
