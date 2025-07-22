package com.simplemova.activity.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.simplemova.R;
import com.simplemova.activity.extra.BaseActivity;

import java.util.Random;

public class ConfirmationActivity extends BaseActivity {

    private ConstraintLayout mConfirmationLayoutRoot;
    private AppCompatImageView mIvConfirmationIcon, mIvLogoImage;
    private LinearLayout mLayoutAmountDetails;
    private MaterialButton mBtnBackToHome;
    private MaterialTextView mTvAmountValue;
    private boolean mIsExiting = false;
    private static final long ENTER_DURATION = 500;
    private static final long EXIT_DURATION = 1000;
    private static final long INITIAL_DELAY = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        setOnApplyWindowInsetsListener();

        initWidgets();
        mIvConfirmationIcon.setScaleX(0f);
        mIvConfirmationIcon.setScaleY(0f);
        mIvConfirmationIcon.setAlpha(0f);
//        mIvConfirmationIcon.post(this::animateEnter);

        mLayoutAmountDetails.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mLayoutAmountDetails.getViewTreeObserver().removeOnPreDrawListener(this);

                mLayoutAmountDetails.setTranslationY(mLayoutAmountDetails.getHeight());
                mLayoutAmountDetails.setAlpha(0f);
                startConfirmationAnimations();
                return true;
            }
        });


        mBtnBackToHome.setOnClickListener(v -> animateExitAndFinish());
    }

    private void initWidgets() {
        mIvLogoImage = findViewById(R.id.iv_logo_image);
        mConfirmationLayoutRoot = findViewById(R.id.layout_root);
        mIvConfirmationIcon = findViewById(R.id.iv_confirmation_icon);
        mLayoutAmountDetails = findViewById(R.id.layout_amount_details);
        mBtnBackToHome = findViewById(R.id.btn_back_to_home);
        mTvAmountValue = findViewById(R.id.tv_amount_value);
    }

    private void startConfirmationAnimations() {
        animateAmountValue();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mIvConfirmationIcon, "scaleX", 0f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mIvConfirmationIcon, "scaleY", 0f, 1f);
        ObjectAnimator alphaIconAnimator = ObjectAnimator.ofFloat(mIvConfirmationIcon, "alpha", 0f, 1f);

        ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(mLayoutAmountDetails, "translationY", (float) mLayoutAmountDetails.getHeight() / 2, 0f);
        ObjectAnimator alphaLayoutAnimator = ObjectAnimator.ofFloat(mLayoutAmountDetails, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();

        AnimatorSet iconAnimation = new AnimatorSet();
        iconAnimation.playTogether(scaleXAnimator, scaleYAnimator, alphaIconAnimator);
        iconAnimation.setDuration(1000);

        AnimatorSet layoutAnimation = new AnimatorSet();
        layoutAnimation.playTogether(translateAnimator, alphaLayoutAnimator);
        layoutAnimation.setDuration(600).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });

        animatorSet.playSequentially(iconAnimation, layoutAnimation);
        animatorSet.start();
    }

    private void animateEnter() {
        animateAmountValue();
        mIvConfirmationIcon.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(ENTER_DURATION)
                .setStartDelay(INITIAL_DELAY)
                .start();

        mLayoutAmountDetails.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(ENTER_DURATION)
                .setStartDelay(INITIAL_DELAY)
                .start();
    }

    private void animateExitAndFinish() {
        if (mIsExiting) return;
        mIsExiting = true;
        //slide up logo
        mIvLogoImage.animate()
                .translationY(-mIvLogoImage.getHeight())
                .alpha(0f)
                .setDuration(500)
                .start();

        //fade out confirmation box icon
        mIvConfirmationIcon.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(EXIT_DURATION)
                .start();

        //slide down amount details layout
        mLayoutAmountDetails.animate()
                .translationY(mLayoutAmountDetails.getHeight())
                .alpha(0f)
                .setDuration(EXIT_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        ConfirmationActivity.super.finish();
                    }
                })
                .start();
    }

//    @SuppressLint("MissingSuperCall")
//    @Override
//    public void onBackPressed() {
//        if (!mIsExiting) {
//            animateExitAndFinish();
//        }
//    }

    private void animateAmountValue() {
        final double amount = new Random().nextInt(8999) + 1000;
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(4000);

        animator.addUpdateListener(animation -> {
            int currentPercentage = (int) animation.getAnimatedValue();

            double currentValue = amount * (currentPercentage / 100.0);
            mTvAmountValue.setText(String.format("$%.0f USD", currentValue));
        });
        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIvConfirmationIcon != null) {
            mIvConfirmationIcon.animate().cancel();
        }
    }
}
