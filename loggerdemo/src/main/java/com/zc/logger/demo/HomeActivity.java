package com.zc.logger.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.zc.logger.LogManager;

/**
 * Created by zzc on 16/2/28.
 */
public class HomeActivity extends Activity {
    public static final String TAG = "MainActivity";

    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        L.e(TAG, "onCreate");
        mText = (TextView) findViewById(R.id.home_text);
        mText.setText("Log file path is: " + LogManager.getInstance().getFilePath());
        L.i(TAG, "Log file path is: " + LogManager.getInstance().getFilePath());
    }
}
