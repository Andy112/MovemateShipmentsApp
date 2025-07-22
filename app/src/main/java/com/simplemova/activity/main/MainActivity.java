package com.simplemova.activity.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.util.Pair;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.simplemova.R;
import com.simplemova.activity.extra.BaseActivity;
import com.simplemova.data.DummyDataGenerator;
import com.simplemova.ui.adapter.AvailableVehiclesAdapter;
import com.simplemova.ui.widget.CustomRecyclerView;
import com.simplemova.util.WidgetUtil;

public class MainActivity extends BaseActivity {

    private boolean isExiting = false;
    private static final long LAUNCH_DELAY_MS = 300;
    private static final long ENTER_DURATION = 500;
    private static final long EXIT_DURATION = 400;
    private static final long BUFFER_DELAY_BEFORE_CONTENT = 150;
    private static final long STAGGER_DELAY = 100;
    private final String[] mTabTitles = {
            WidgetUtil.getString(R.string.home),
            WidgetUtil.getString(R.string.calculate),
            WidgetUtil.getString(R.string.shipment),
            WidgetUtil.getString(R.string.profile)};
    private final int[] mTabIcons = {
            R.drawable.home_24px,
            R.drawable.calculate_24px,
            R.drawable.history_24px,
            R.drawable.person_24px
    };
    private final Class<?>[] mTabActivities = {
            MainActivity.class,
            CalculateActivity.class,
            ShipmentActivity.class,
            ProfileActivity.class
    };
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private AvailableVehiclesAdapter mAvailableVehiclesAdapter;
    private CustomRecyclerView mAvailableVehiclesRv;
    private MaterialCardView mCardSearchBar;
    private TabLayout mTabLayout;
    private View mContentLayoutContainer;
    private MaterialCardView mBottomTabContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        initTabLayout();
        initAvailableVehiclesAdapter();
        setBodyContainerBottomPadding();
        runAnimateEnter();
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
    }

    private void initWidgets() {
        mAvailableVehiclesRv = findViewById(R.id.available_vehicles_rv);
        mTabLayout = findViewById(R.id.tabs);
        mContentLayoutContainer = findViewById(R.id.content_layout_container);
        mBottomTabContainer = findViewById(R.id.bottom_tab_container);
        mCardSearchBar = findViewById(R.id.card_search_bar);
        findViewById(R.id.layout_search_bar).setOnClickListener(v -> gotoSearchActivity());
        findViewById(R.id.btn_add_stop).setOnClickListener(v -> {});

        setupIncludedLayout(R.id.sender_info, "Sender", "Atlanta 5243");
        setupIncludedLayout(R.id.time_info, "Time", "2 days-3 days");
        setupIncludedLayout(R.id.receiver_info, "Receiver", "Chicago 6342");
        setupIncludedLayout(R.id.status_info, "Status", "Waiting to collect");
    }

    private void setupIncludedLayout(int id, String label, String value) {
        View v = findViewById(id);
        ((MaterialTextView) v.findViewById(R.id.text_label)).setText(label);
        ((MaterialTextView) v.findViewById(R.id.text_value)).setText(value);
    }

    private void initTabLayout() {
        for (int i = 0; i < mTabTitles.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            mTabLayout.addTab(tab);
            View customTabView = LayoutInflater.from(this).inflate(R.layout.custom_bottom_nav_tab_item, mTabLayout, false);
            AppCompatImageView tabIcon = customTabView.findViewById(R.id.tab_icon);
            MaterialTextView tabText = customTabView.findViewById(R.id.tab_title_text);
            tabIcon.setImageResource(mTabIcons[i]);
            tabText.setText(mTabTitles[i]);
            tab.setCustomView(customTabView);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                final Class<?> targetActivity = mTabActivities[tab.getPosition()];
                if (MainActivity.class != targetActivity) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                            MainActivity.this, mAppBarView, getString(R.string.app_bar_transition));
                    mHandler.postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this, targetActivity);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent, options.toBundle());
                    }, LAUNCH_DELAY_MS);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void initAvailableVehiclesAdapter() {
        mAvailableVehiclesAdapter = new AvailableVehiclesAdapter();
        mAvailableVehiclesRv.createRvHorizontalLayout(mAvailableVehiclesAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(() -> mTabLayout.getTabAt(0).select(), LAUNCH_DELAY_MS);
        mAvailableVehiclesAdapter.refreshAdapter(DummyDataGenerator.getAvailableVehicles());
    }

    private void setBodyContainerBottomPadding() {
        View bottomContainer = findViewById(R.id.bottom_tab_container);
        View bodyContainer = findViewById(R.id.nested_scroll_view_container);

        bottomContainer.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    int previousHeight = 0;
                    @Override
                    public void onGlobalLayout() {
                        int currentHeight = bottomContainer.getHeight();
                        if (currentHeight != previousHeight) {
                            previousHeight = currentHeight;
                            bodyContainer.setPadding(
                                    bodyContainer.getPaddingLeft(),
                                    bodyContainer.getPaddingTop(),
                                    bodyContainer.getPaddingRight(),
                                    currentHeight
                            );
                        }
                    }
                });
    }

    private void runAnimateEnter() {
        mAppBarView.post(new Runnable() {
            @Override
            public void run() {
                if (mAppBarView.getMeasuredHeight() == 0 || mBottomTabContainer.getMeasuredHeight() == 0) {
                    mAppBarView.post(this);
                    return;
                }
                mAppBarView.setTranslationY(-mAppBarView.getMeasuredHeight());
                mBottomTabContainer.setTranslationY(mBottomTabContainer.getMeasuredHeight());
//                mContentLayoutContainer.setTranslationX(mContentLayoutContainer.getMeasuredWidth());
                mContentLayoutContainer.setAlpha(0);
                animateEnter();
            }
        });
    }

    private void gotoSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                         MainActivity.this, mCardSearchBar, "search_bar_transition");
                 startActivity(intent, options.toBundle());
    }

    private void animateEnter() {
        // Animate AppBar: Slide down and fade in
//        appbar.animate()
//                .translationY(0)
//                .alpha(1f)
//                .setDuration(ENTER_DURATION)
//                .setStartDelay(STAGGER_DELAY)
//                .start();

        mAppBarView.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(ENTER_DURATION)
                .setStartDelay(0) // Start immediately
                .start();

        long contentStartDelay = ENTER_DURATION + 150;
        // Animate Content (Middle Section): Fade in
        mContentLayoutContainer.animate()
                .alpha(1f)
                .setDuration(ENTER_DURATION)
                .setStartDelay(contentStartDelay)
                .start();

        mBottomTabContainer.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(ENTER_DURATION)
                .setStartDelay(0) // Start immediately
                .start();
    }

    private void animateExitAndFinish() {
        if (isExiting) return; // Prevent multiple calls
        isExiting = true;

        // Animate AppBar: Slide up and fade out
        mAppBarView.animate()
                .translationY(-mAppBarView.getHeight()) // Slide up beyond top
                .alpha(0f)
                .setDuration(EXIT_DURATION)
                .setStartDelay(STAGGER_DELAY * 2) // Start later for staggered exit
                .start();

        // Animate Content (Middle Section): Fade out
//        contentLayoutContainer.animate()
//                .alpha(0f)
//                .setDuration(EXIT_DURATION)
//                .setStartDelay(STAGGER_DELAY) // Start earlier
//                .start();

        mContentLayoutContainer.animate()
                .translationX(-mContentLayoutContainer.getWidth())
                .alpha(0f)
                .setDuration(EXIT_DURATION)
                .setStartDelay(BUFFER_DELAY_BEFORE_CONTENT)
                .start();

        mContentLayoutContainer.animate()
                .translationX(mContentLayoutContainer.getWidth())
                .alpha(0f)
                .setDuration(EXIT_DURATION)
                .setStartDelay(BUFFER_DELAY_BEFORE_CONTENT)
                .start();

        mBottomTabContainer.animate()
                .translationY(mBottomTabContainer.getHeight())
                .alpha(0f)
                .setDuration(EXIT_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        MainActivity.super.finish();

                    }
                })
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppBarView.animate().cancel();
        mContentLayoutContainer.animate().cancel();
        mBottomTabContainer.animate().cancel();
    }
}