package com.simplemova.util;

import static com.simplemova.activity.extra.SimpleMovaApp.getAppContext;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.simplemova.R;

public class WidgetUtil {

    public static final long RECYCLER_VIEW_STAGGER_DELAY = 100;
    public static final long RECYCLER_VIEW_ANIM_DURATION = 600;

    public static int getColor(int resource) {
        if (resource == 0) resource = R.color.grey;
        return ContextCompat.getColor(getAppContext(), resource);
    }

    public static Drawable getDrawable(int resource) {
        return ContextCompat.getDrawable(getAppContext(), resource);
    }

    public static String getStatus(int res) {
        return getString(res);
    }

    public static String getString(int res) {
        return getAppContext().getString(res);
    }

    public static int getStatusColor(String status) {
        int colorRes = R.color.status_pending;
        if (getStatus(R.string.completed).equals(status)) colorRes = R.color.status_completed;
        else if (getStatus(R.string.in_progress).equals(status)) colorRes = R.color.status_in_progress;
        else if (getStatus(R.string.loading).equals(status)) colorRes = R.color.status_loading;
        else if (getStatus(R.string.cancelled).equals(status)) colorRes = R.color.status_cancelled;
        else if (getStatus(R.string.pending).equals(status)) colorRes = R.color.status_pending;
        return getColor(colorRes);
    }
}
