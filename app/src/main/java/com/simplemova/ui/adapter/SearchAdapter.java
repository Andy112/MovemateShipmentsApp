package com.simplemova.ui.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.simplemova.util.WidgetUtil.RECYCLER_VIEW_ANIM_DURATION;
import static com.simplemova.util.WidgetUtil.RECYCLER_VIEW_STAGGER_DELAY;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.simplemova.R;
import com.simplemova.model.Parcel;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Parcel> mParcels;
    private int mLastPosition;

    public void refreshAdapter(List<Parcel> parcels) {
        mParcels = parcels;
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return mParcels != null ? mParcels.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView mItemNameTv, mItemDetailsTv;
        private final View mDivider;

        public ViewHolder(@NonNull View v) {
            super(v);
            mItemNameTv = v.findViewById(R.id.item_name_tv);
            mItemDetailsTv = v.findViewById(R.id.item_details_tv);
            mDivider = v.findViewById(R.id.divider);
            v.findViewById(R.id.item_layout).setOnClickListener(e -> {});
        }

        public void setData() {
            Parcel parcel = mParcels.get(getAdapterPosition());
            mItemNameTv.setText(parcel.getName());
            mItemDetailsTv.setText(parcel.getDetails());
            mDivider.setVisibility(getAdapterPosition() < getItemCount()-1 ? VISIBLE : GONE);
            animateItemView(itemView, getAdapterPosition());
        }
    }

    private void animateItemView(View v, int position) {
        if (position > mLastPosition) {
            v.setAlpha(0f);
            v.setTranslationY(100f);
            animate(v, position);
            mLastPosition = position;
        } else {
            v.setAlpha(1f);
            v.setTranslationY(0f);
        }
    }

    private void animate(View v, int position) {
        v.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(RECYCLER_VIEW_ANIM_DURATION)
                .setStartDelay(position * RECYCLER_VIEW_STAGGER_DELAY)
                .start();
    }
}