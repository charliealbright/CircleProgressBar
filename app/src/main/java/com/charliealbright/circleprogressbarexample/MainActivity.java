package com.charliealbright.circleprogressbarexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.charliealbright.circleprogressbar.CircleProgressBar;

public class MainActivity extends AppCompatActivity {

    private CircleProgressBar mCircleProgressBar;
    private AppCompatSeekBar mSeekBar;
    private TextSwitcher mTextSwitcher;
    private Button mNextButton;

    private boolean userActionTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircleProgressBar = (CircleProgressBar)findViewById(R.id.circleprogressbar);
        mCircleProgressBar.setMin(1000);
        mCircleProgressBar.setMax(0);

        mSeekBar = (AppCompatSeekBar)findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCircleProgressBar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (userActionTaken == false) {
                    mTextSwitcher.setText(getText(R.string.confirmation));
                    mNextButton.setEnabled(true);
                    userActionTaken = true;
                }
            }
        });

        mTextSwitcher = (TextSwitcher)findViewById(R.id.text_view);
        mTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        mTextSwitcher.addView(makeTextView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTextSwitcher.addView(makeTextView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTextSwitcher.setText(getText(R.string.screen1_text));

        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AnimationActivity.class);
                startActivity(intent);
            }
        });

        getSharedPreferences("fileName", MODE_PRIVATE).edit().clear().apply();
    }

    private TextView makeTextView() {
        TextView textView = new TextView(this);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        return textView;
    }
}
