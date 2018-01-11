package com.github.conanchen.gedit.ui.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.conanchen.gedit.GeditApplication;
import com.github.conanchen.gedit.R;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
