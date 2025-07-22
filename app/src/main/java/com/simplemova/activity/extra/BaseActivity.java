package com.simplemova.activity.extra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textview.MaterialTextView;
import com.simplemova.R;

public class BaseActivity extends AppCompatActivity {

    protected final int REQUEST_CODE_SPEECH_INPUT = 123;
    private static final long ENTER_DURATION = 500;
    private static final long EXIT_DURATION = 400;
    protected MaterialTextView mTvAppbarTitle;
    protected AppBarLayout mAppBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mAppBarView = findViewById(R.id.app_bar_view);
        setOnApplyWindowInsetsListener();
        setupAppbarTitle(null);
        setupBackButton();
    }

    public void setContentView(int layoutResID, String title) {
        setContentView(layoutResID);
        setupAppbarTitle(title);
    }

    protected void setOnApplyWindowInsetsListener() {
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_root), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void setupAppbarTitle(String title) {
        mTvAppbarTitle = findViewById(R.id.app_bar_title);
        if (mTvAppbarTitle != null && title != null && !title.isEmpty()) {
            mTvAppbarTitle.setText(title);
        }
    }

    private void setupBackButton() {
        View backBtn = findViewById(R.id.btn_back);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
//            animateFromLeft(backBtn);
        }
    }

    protected void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }

    private void animateFromLeft(View v) {
        v.setAlpha(0f);
        v.setTranslationX(-1000f);
        v.animate()
                .translationX(0)
                .alpha(1f)
                .setDuration(ENTER_DURATION)
                .setStartDelay(0)
                .start();
    }
}
