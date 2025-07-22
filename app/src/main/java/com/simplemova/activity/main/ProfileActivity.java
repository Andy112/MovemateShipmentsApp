package com.simplemova.activity.main;

import android.os.Bundle;

import com.simplemova.R;
import com.simplemova.activity.extra.BaseActivity;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setOnApplyWindowInsetsListener();
    }
}
