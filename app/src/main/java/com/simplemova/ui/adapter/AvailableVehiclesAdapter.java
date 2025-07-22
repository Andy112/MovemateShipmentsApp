package com.simplemova.ui.adapter;

import static com.simplemova.util.WidgetUtil.RECYCLER_VIEW_ANIM_DURATION;
import static com.simplemova.util.WidgetUtil.RECYCLER_VIEW_STAGGER_DELAY;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.simplemova.R;
import com.simplemova.model.Vehicle;

import java.util.List;

public class AvailableVehiclesAdapter extends RecyclerView.Adapter<AvailableVehiclesAdapter.ViewHolder> {

    public List<Vehicle> mVehicles;
    private int mLastPosition;

    public void refreshAdapter(List<Vehicle> vehicles) {
        mVehicles = vehicles;
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_vehicle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return mVehicles != null ? mVehicles.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView mTvVehicleName, mTvVehicleType;
        private final AppCompatImageView mIvVehicleImage;

        public ViewHolder(@NonNull View v) {
            super(v);
            mTvVehicleName = v.findViewById(R.id.tv_vehicle_name);
            mTvVehicleType = v.findViewById(R.id.tv_vehicle_type);
            mIvVehicleImage = v.findViewById(R.id.iv_vehicle_image);
            v.findViewById(R.id.item_layout).setOnClickListener(e -> {});
        }

        public void setData() {
            Vehicle vehicle = mVehicles.get(getAdapterPosition());
            mTvVehicleName.setText(vehicle.getName());
            mTvVehicleType.setText(vehicle.getType());
            mIvVehicleImage.setImageResource(vehicle.getImageRes());
            animateItemView(itemView, getAdapterPosition());
        }
    }

    private void animateItemView(View v, int position) {
        if (position > mLastPosition) {
            v.setAlpha(0f);
            v.setTranslationX(200f);
            animate(v, position);
            mLastPosition = position;
        } else {
            v.setAlpha(1f);
            v.setTranslationX(0f);
        }
    }

    private void animate(View v, int position) {
        v.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(RECYCLER_VIEW_ANIM_DURATION)
                .setStartDelay(position * RECYCLER_VIEW_STAGGER_DELAY)
                .start();
    }
}
