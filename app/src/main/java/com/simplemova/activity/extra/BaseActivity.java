package com.simplemova.activity.extra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textview.MaterialTextView;
import com.simplemova.R;
import com.simplemova.iface.ISharedElementTransitionListener;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
            backBtn.setOnClickListener(v -> super.onBackPressed());
//            animateBackBtnFromLeftToRight(backBtn);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    protected void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }

    protected void registerSharedElementTransitionListener(ISharedElementTransitionListener listener) {
        Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
        if (sharedElementEnterTransition != null) {
            sharedElementEnterTransition.addListener(new Transition.TransitionListener() {
                public void onTransitionStart(Transition transition) {}
                public void onTransitionPause(Transition transition) {}
                public void onTransitionResume(Transition transition) {}
                public void onTransitionEnd(Transition transition) {
                    transition.removeListener(this);
                    listener.onTransitionEnded();
                }
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                    listener.onTransitionEnded();
                }
            });
        } else listener.onTransitionEnded();
    }

    private void animateBackBtnFromLeftToRight(View v) {
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
