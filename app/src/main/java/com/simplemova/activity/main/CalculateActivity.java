package com.simplemova.activity.main;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.simplemova.R;
import com.simplemova.activity.extra.BaseActivity;

public class CalculateActivity extends BaseActivity {

    private AppCompatEditText mEtSenderLocation, mEtReceiverLocation, mEtApproxWeight;
    private View mFormLayout;
    private ChipGroup mChipGroupCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        setOnApplyWindowInsetsListener();
        initWidgets();
        super.registerSharedElementTransitionListener(() -> {
            animateChipsFromRightToLeft();
            animateFormFromBottomToTop();
        });
    }

    private void initWidgets() {
        mFormLayout = findViewById(R.id.layout_form);
        mChipGroupCategories = findViewById(R.id.chip_group_categories);
        findViewById(R.id.calculate_btn).setOnClickListener(v ->
                startActivity(ConfirmationActivity.class));
        mEtSenderLocation = setUpIncludedLayout(R.id.sender_location_view, R.drawable.parcel_out_24px, "Sender location");
        mEtReceiverLocation = setUpIncludedLayout(R.id.receiver_location_view, R.drawable.parcel_in_24px, "Receiver location");
        mEtApproxWeight = setUpIncludedLayout(R.id.approx_weight_view, R.drawable.scale_24px, "Approx weight");
    }

    private AppCompatEditText setUpIncludedLayout(int viewId, int iconRes, String hint) {
        View v = findViewById(viewId);
        ((AppCompatImageView) v.findViewById(R.id.iv_icon)).setImageResource(iconRes);
        AppCompatEditText aet = v.findViewById(R.id.text_value);
        aet.setHint(hint);
        return aet;
    }

    private void animateChipsFromRightToLeft() {
        mChipGroupCategories.postDelayed(() -> {
            final long ANIM_DURATION = 600;
            final long STAGGER_DELAY = 5;

            for (int i = 0; i < mChipGroupCategories.getChildCount(); i++) {
                View child = mChipGroupCategories.getChildAt(i);
                if (child instanceof Chip) {
                    Chip chip = (Chip) child;
                    chip.animate()
                            .alpha(1f)
                            .translationX(0f)
                            .setDuration(ANIM_DURATION)
                            .setStartDelay(i * STAGGER_DELAY)
                            .start();
                }
            }
        }, 500);
    }

    private void animateFormFromBottomToTop() {
        if (mFormLayout.getAlpha() < 1) {
            mFormLayout.animate()
                    .translationY(0)
                    .alpha(1f)
                    .setDuration(600)
                    .setStartDelay(0)
                    .start();
        }
    }
}
