package com.simplemova.ui.adapter;

import static com.simplemova.util.WidgetUtil.RECYCLER_VIEW_ANIM_DURATION;
import static com.simplemova.util.WidgetUtil.RECYCLER_VIEW_STAGGER_DELAY;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.simplemova.R;
import com.simplemova.model.Shipment;
import com.simplemova.util.WidgetUtil;

import java.util.List;

public class ShipmentHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mLastPosition = -1;
    private final int HEADER_TITLE_VIEW = 0;
    private final int ITEM_VIEW = 1;
    private final int EXTRA_VIEW_COUNT = 1;

    private List<Shipment> mShipments;
    private boolean mHasRunHeaderTitleAnimationOnce;

    public void refreshAdapter(List<Shipment> shipments) {
        mShipments = shipments;
        mLastPosition = -1;
        mHasRunHeaderTitleAnimationOnce = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER_TITLE_VIEW : ITEM_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater lif = LayoutInflater.from(parent.getContext());
        if (viewType == HEADER_TITLE_VIEW)
            return new HeaderTitleViewHolder(lif.inflate(R.layout.custom_shipment_header_title, parent, false));
        return new ViewHolder(lif.inflate(R.layout.custom_shipment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderTitleViewHolder)
            ((HeaderTitleViewHolder) holder).setData();
        else if (holder instanceof ViewHolder)
            ((ViewHolder) holder).setData(getRegularItemPosition(position));
    }

    @Override
    public int getItemCount() {
        return mShipments != null ? mShipments.size() + EXTRA_VIEW_COUNT : EXTRA_VIEW_COUNT;
    }

    public int getRegularItemPosition(int position) {
        return Math.max(0, position - EXTRA_VIEW_COUNT);
    }

    public class HeaderTitleViewHolder extends RecyclerView.ViewHolder {

        public HeaderTitleViewHolder(@NonNull View v) {
            super(v);
        }

        public void setData() {
            if (!mHasRunHeaderTitleAnimationOnce) {
                animate(itemView, getAdapterPosition());
                mHasRunHeaderTitleAnimationOnce = true;
            } else {
                itemView.setAlpha(1f);
                itemView.setTranslationY(0f);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView mTvStatus;

        public ViewHolder(@NonNull View v) {
            super(v);
            mTvStatus = v.findViewById(R.id.tv_status);
            v.findViewById(R.id.item_layout).setOnClickListener(e -> {});
        }

        public void setData(int position) {
            Shipment shipment = mShipments.get(position);
            mTvStatus.setText(shipment.getStatus().toLowerCase());
            mTvStatus.setCompoundDrawablesWithIntrinsicBounds(WidgetUtil
                    .getDrawable(shipment.getIconRes()), null, null, null);

            int statusColor = WidgetUtil.getStatusColor(shipment.getStatus());
            mTvStatus.setTextColor(statusColor);
            TextViewCompat.setCompoundDrawableTintList(mTvStatus, ColorStateList.valueOf(statusColor));
            animateItemView(itemView, position);
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
