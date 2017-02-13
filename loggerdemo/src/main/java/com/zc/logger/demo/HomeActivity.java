package com.zc.logger.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zc.logger.LogManager;

/**
 * Created by zzc on 16/2/28.
 */
public class HomeActivity extends Activity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        L.e(TAG, "onCreate");
        TextView mText = (TextView) findViewById(R.id.home_text);
        mText.setText("Log file path is: " + LogManager.getInstance().getFilePath());
        L.i(TAG, "Log file path is: " + LogManager.getInstance().getFilePath());
        View anrView = findViewById(R.id.create_anr);
        anrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        View crashView = findViewById(R.id.create_crash);
        crashView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new IllegalStateException("hahahahhahaha");
            }
        });
    }
}
