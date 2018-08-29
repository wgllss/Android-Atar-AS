package com.atar.other.activitys.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.atar.other.R;
import com.atar.other.activitys.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.txt_onclick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(thisContext, SecondActivity.class));
            }
        });
    }
}
