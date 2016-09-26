package com.charliealbright.circleprogressbarexample;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.charliealbright.circleprogressbar.CircleProgressBar;

public class AnimationActivity extends AppCompatActivity {

    private CircleProgressBar mCircleProgressBar;
    private Button mAnimateButton;
    private Button mNextButton;
    private TextSwitcher mTextSwitcher;

    private ObjectAnimator mObjectAnimator;

    private boolean userActionTaken = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        mObjectAnimator = new ObjectAnimator();

        mCircleProgressBar = (CircleProgressBar)findViewById(R.id.animation_circleprogressbar);

        mAnimateButton = (Button)findViewById(R.id.animation_button);
        mAnimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mObjectAnimator.isRunning() && !mObjectAnimator.isStarted()) {
                    mObjectAnimator.setTarget(mCircleProgressBar);
                    mObjectAnimator.setDuration(3000);
                    mObjectAnimator.setPropertyName("progress");
                    mObjectAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
                    mObjectAnimator.setIntValues(0, 1000);
                    mObjectAnimator.start();
                }
                if (!userActionTaken) {
                    mTextSwitcher.setText(getText(R.string.confirmation));
                    mNextButton.setEnabled(true);
                    userActionTaken = true;
                }
            }
        });

        mNextButton = (Button)findViewById(R.id.animation_nextbutton);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Make 3rd example?
            }
        });

        mTextSwitcher = (TextSwitcher)findViewById(R.id.animation_textview);
        mTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        mTextSwitcher.addView(makeTextView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTextSwitcher.addView(makeTextView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTextSwitcher.setText(getText(R.string.screen2_text));
    }

    private TextView makeTextView() {
        TextView textView = new TextView(this);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        return textView;
    }
}
