package com.charliealbright.circleprogressbarexample;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.charliealbright.circleprogressbar.CircleProgressBar;

import java.net.URI;

public class MultiActivity extends AppCompatActivity {

    private static final int ANIM_DURATION = 3000;

    private CircleProgressBar mCircleProgressBarCarbs;
    private CircleProgressBar mCircleProgressBarProteins;
    private CircleProgressBar mCircleProgressBarFats;

    private ObjectAnimator mCarbsAnimator;
    private ObjectAnimator mProteinsAnimator;
    private ObjectAnimator mFatsAnimator;

    private Button mGithubButton;
    private Button mExitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);


        mCircleProgressBarCarbs = (CircleProgressBar)findViewById(R.id.multi_circleprogressbar_carb);
        mCircleProgressBarProteins = (CircleProgressBar)findViewById(R.id.multi_circleprogressbar_protein);
        mCircleProgressBarFats = (CircleProgressBar)findViewById(R.id.multi_circleprogressbar_fat);

        mGithubButton = (Button)findViewById(R.id.multi_button_github);
        mGithubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/charliealbright/CircleProgressBar"));
                startActivity(browserIntent);
            }
        });

        mExitButton = (Button)findViewById(R.id.multi_button_exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        mCarbsAnimator = new ObjectAnimator();
        mCarbsAnimator.setTarget(mCircleProgressBarCarbs);
        mCarbsAnimator.setPropertyName("progress");
        mCarbsAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        mCarbsAnimator.setDuration(ANIM_DURATION);
        mCarbsAnimator.setIntValues(0, 780);

        mProteinsAnimator = new ObjectAnimator();
        mProteinsAnimator.setTarget(mCircleProgressBarProteins);
        mProteinsAnimator.setPropertyName("progress");
        mProteinsAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        mProteinsAnimator.setDuration(ANIM_DURATION);
        mProteinsAnimator.setIntValues(0, 550);

        mFatsAnimator = new ObjectAnimator();
        mFatsAnimator.setTarget(mCircleProgressBarFats);
        mFatsAnimator.setPropertyName("progress");
        mFatsAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        mFatsAnimator.setDuration(ANIM_DURATION);
        mFatsAnimator.setIntValues(0, 870);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCarbsAnimator.start();
                mProteinsAnimator.start();
                mFatsAnimator.start();
            }
        }, 250);
    }
}
