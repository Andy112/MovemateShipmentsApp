package com.simplemova.ui.widget;

import static com.simplemova.activity.extra.SimpleMovaApp.getAppContext;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.simplemova.R;
import com.simplemova.util.Util;
import com.simplemova.util.WidgetUtil;

import java.util.Objects;

public class CustomRecyclerView extends RecyclerView {

    private SwipeRefreshLayout mSwipeRefresh;
    private Adapter<?> adapter;
    private boolean isInit = true;
    private ItemDecoration itemDecoration;
    private boolean hasItems, mUseLoadingView;
    private View mLoadingView, mParentView;
    private AppCompatImageView mLoadingImageView;
    private AppCompatTextView mLoadingMsgTitleTv, mLoadingMsgTv;

    public CustomRecyclerView(Context context) {
        super(context);
        onConstruct();
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onConstruct();
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onConstruct();
    }

    public CustomRecyclerView onConstruct() {
        mLoadingView = findViewByID(R.id.loading_view_ll);
        mLoadingImageView = findViewByID(R.id.loading_image_view);
        mLoadingMsgTitleTv = findViewByID(R.id.loading_message_title_tv);
        mLoadingMsgTv = findViewByID(R.id.loading_message_tv);
        return this;
    }

    public CustomRecyclerView onConstruct2() {
        mLoadingView = findViewByID(R.id.loading_view_ll);
        mLoadingImageView = findViewByID(R.id.loading_image_view);
        mLoadingMsgTitleTv = findViewByID(R.id.loading_message_title_tv);
        mLoadingMsgTv = findViewByID(R.id.loading_message_tv);
        return this;
    }

    public CustomRecyclerView setParentView(View v) {
        mParentView = v;
        return this;
    }

    public CustomRecyclerView setLoadingViewBackground(int color) {
        if (mLoadingView != null) {
            mLoadingView.setBackgroundColor(WidgetUtil.getColor(color));
        }
        return this;
    }

    public CustomRecyclerView setMessageIcon(int icon) {
        if (mLoadingImageView != null)
            mLoadingImageView.setImageResource(icon);
        return this;
    }

    public CustomRecyclerView setMessageIcon(int icon, int color) {
        if (mLoadingImageView != null) {
            mLoadingImageView.setImageResource(icon);
            mLoadingImageView.setImageTintList(ColorStateList.valueOf(WidgetUtil.getColor(color)));
        }
        return this;
    }

    public CustomRecyclerView useLoadingView(String emptyMsg) {
        onConstruct();
        if (mLoadingView != null) {
            mUseLoadingView = true;
            mLoadingView.setVisibility(VISIBLE);
            mLoadingMsgTv.setText(emptyMsg);
        }
        return this;
    }

    public CustomRecyclerView useLoadingView(boolean use) {
        onConstruct();
        mUseLoadingView = use;
        mLoadingView.setVisibility(use ? VISIBLE : GONE);
        return this;
    }

    private void showHideLoadingView(boolean show) {
        if (mUseLoadingView && mLoadingView != null) mLoadingView.setVisibility(show ? VISIBLE : GONE);
    }

    public CustomRecyclerView setMessage(String message) {
        if (mLoadingMsgTv != null) mLoadingMsgTv.setText(message);
        return this;
    }

    public void resetMessage() {
        if (mLoadingMsgTv != null) mLoadingMsgTv.setText(getContext().getString(R.string.no_data));
    }

    public SwipeRefreshLayout getSwipeRefresh() {
        if (mSwipeRefresh == null) initSwipeToRefresh();
        return mSwipeRefresh;
    }

    public void initSwipeToRefresh() {
        mSwipeRefresh = findViewByID(R.id.swipe_to_refresh);
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setEnabled(false);
            mSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(getAppContext(), R.color.colorAccent));
//        mSwipeRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getAppContext(),
//                R.color.colorBackgroundSemi2));
        }
    }

    private final AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            if (this.adapter == null) {
                adapter.registerAdapterDataObserver(mObserver);
                this.adapter = adapter;
                initSwipeToRefresh();
                adapter.notifyDataSetChanged();
            }
        }
        mObserver.onChanged();
    }

    private void toggleViews() {
        showHideLoadingView(adapter == null || adapter.getItemCount() <= 0);
        if (isInit) {
            showHideLoadingView(false);
            isInit = false;
            return;
        }
    }

    private <T extends View> T findViewByID(int viewId) {
        try {
            if (mParentView != null) return mParentView.findViewById(viewId);
            return ((AppCompatActivity) getContext()).findViewById(viewId);
        } catch (Exception e) {
            return null;
        }
    }

    public void addItemDecoration(boolean isGrid, boolean useThickDivider) {
        if (isGrid) itemDecoration = new SpacesItemDecoration(0);
        else {
            itemDecoration = new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL);
            ((DividerItemDecoration)itemDecoration)
                    .setDrawable(Objects.requireNonNull(AppCompatResources.getDrawable(this.getContext(),
                            useThickDivider ? R.drawable.divider_color_large : R.drawable.divider_color)));
        }
        addItemDecoration(itemDecoration);
    }

    public void removeItemDecoration() {
        removeItemDecoration(itemDecoration);
    }

    public void createRv(Adapter<?> adapter, boolean useDivider) {
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        setLayoutManager(llm);
        if (useDivider) addItemDecoration(false, false);
        setAdapter(adapter);
    }

    public void createRv(Adapter<?> adapter, boolean useDivider, boolean useThickDivider) {
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        setLayoutManager(llm);
        if (useDivider || useThickDivider) addItemDecoration(false, useThickDivider);
        setAdapter(adapter);
    }

    public void createRv(Adapter<?> adapter, boolean useDivider, boolean useThickDivider, int add, int remove) {
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        setLayoutManager(llm);
        if (add > 0 || remove > 0) addAnimation(add, remove);
        if (useDivider || useThickDivider) addItemDecoration(false, useThickDivider);
        setAdapter(adapter);
    }

    public void createRv(Adapter<?> adapter, boolean useDivider, int add, int remove) {
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        setLayoutManager(llm);
        if (add > 0 || remove > 0) addAnimation(add, remove);
        if (useDivider) addItemDecoration(false, false);
        setAdapter(adapter);
    }

    public void createRvAnimated(Adapter<?> adapter, boolean useDivider) {
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        setLayoutManager(llm);
        addAnimation(500, 500);
        if (useDivider) addItemDecoration(false, false);
        setAdapter(adapter);
    }

    public void createRv(Adapter<?> adapter, int add, int remove) {
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        if (add > 0 || remove > 0) addAnimation(add, remove);
        setLayoutManager(llm);
        addItemDecoration(false, false);
        setAdapter(adapter);
    }

    public void createDynamicRvGridLayout(Adapter<?> adapter, int spanCount) {
        onCreateDynamicGridLayout(adapter, 0, spanCount);
    }

    public StaggeredGridLayoutManager createRvStaggeredLayout(Adapter<?> adapter, int spanCount, int add, int remove) {
        return onCreateStaggeredGridLayout(adapter, spanCount, add, remove, null);
    }

    public void createRvGridLayout(Adapter<?> adapter, int spanCount) {
        onCreateGridLayout(adapter, spanCount, 0, 0, null);
    }

    public GridLayoutManager createRvGridLayout(Adapter<?> adapter, int spanCount, int add, int remove) {
        return onCreateGridLayout(adapter, spanCount, add, remove, null);
    }

    public void createRvGridLayout(Adapter<?> adapter, int spanCount, int add, int remove, String dataTitle) {
        onCreateGridLayout(adapter, spanCount, add, remove, dataTitle);
    }

    public void createRvHorizontalLayout(Adapter<?> adapter) {
        createRvHorizontalLayout(adapter, 0, 0);
    }

    public void createRvHorizontalLayout(Adapter<?> adapter,
                                                int add, int remove) {
        setLayoutManager(new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        if (add > 0 || remove > 0) {
            DefaultItemAnimator animator = new DefaultItemAnimator();
            animator.setAddDuration(add);
            animator.setRemoveDuration(remove);
            setItemAnimator(animator);
        }
        setAdapter(adapter);
    }

    private StaggeredGridLayoutManager onCreateStaggeredGridLayout(Adapter<?> adapter, int spanCount, int add, int remove, String dataTitle) {
//        mDataTitle = dataTitle;
        StaggeredGridLayoutManager gm = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        addItemDecoration(true, false);
        if (add > 0 || remove > 0) addAnimation(add, remove);
        setLayoutManager(gm);
        setAdapter(adapter);
        return gm;
    }

    private GridLayoutManager onCreateGridLayout(Adapter<?> adapter, int spanCount, int add, int remove, String dataTitle) {
//        mDataTitle = dataTitle;
        GridLayoutManager gm = new GridLayoutManager(this.getContext(), spanCount);
        addItemDecoration(true, false);
        if (add > 0 || remove > 0) addAnimation(add, remove);
        setLayoutManager(gm);
        setAdapter(adapter);
        return gm;
    }

    private void onCreateDynamicGridLayout(Adapter<?> adapter, int index, int spanCount) {
        GridLayoutManager gm = new GridLayoutManager(this.getContext(), spanCount);
        addItemDecoration(true, false);
        setLayoutManager(gm);
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == index ? spanCount : 1;
            }
        });
        setAdapter(adapter);
    }

    private void addAnimation(int add, int remove) {
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(add);
        animator.setRemoveDuration(remove);
        setItemAnimator(animator);
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;
        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.top = space;
            outRect.right = space;
            outRect.bottom = space;
        }
    }
}