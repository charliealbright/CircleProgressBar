package com.charliealbright.circleprogressbarexample;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.SeekBar;

import com.charliealbright.circleprogressbar.CircleProgressBar;

public class MainActivity extends AppCompatActivity {

    private CircleProgressBar mCircleProgressBar;
    private SeekBar mSeekBar;
    private Button mButton;

    private ObjectAnimator mObjectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircleProgressBar = (CircleProgressBar)findViewById(R.id.circleprogressbar);

        mObjectAnimator = new ObjectAnimator();
        mObjectAnimator.setInterpolator(new DecelerateInterpolator());
        mObjectAnimator.setTarget(mCircleProgressBar);
        mObjectAnimator.setPropertyName("progress");
        mObjectAnimator.setDuration(3000);

        mSeekBar = (SeekBar)findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mObjectAnimator.isRunning()) {
                    mObjectAnimator.cancel();
                }
                mCircleProgressBar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mButton = (Button)findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mObjectAnimator.isRunning()) {
                    mObjectAnimator.setIntValues(mCircleProgressBar.getProgress(), 800);
                    mObjectAnimator.start();
                }
            }
        });
    }
}
