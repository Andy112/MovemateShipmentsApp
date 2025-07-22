package com.simplemova.activity.main;

import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.simplemova.R;
import com.simplemova.activity.extra.BaseActivity;
import com.simplemova.data.ShipmentDummyDataManager;
import com.simplemova.model.Shipment;
import com.simplemova.model.ShipmentStatus;
import com.simplemova.ui.adapter.ShipmentHistoryAdapter;
import com.simplemova.ui.widget.CustomRecyclerView;

import java.util.List;

public class ShipmentActivity extends BaseActivity {

    private ShipmentHistoryAdapter mShipmentHistoryAdapter;
    private CustomRecyclerView mShipmentRv;
    private TabLayout mTabLayout;

    private final ShipmentDummyDataManager mDataMgr = new ShipmentDummyDataManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setEnterTransition(new Fade());
        getWindow().setReturnTransition(new Fade());

        setContentView(R.layout.activity_shipment);
        setOnApplyWindowInsetsListener();
        initWidgets();
        initTabLayout();
        initShipmentHistoryAdapter();
    }

    private void initWidgets() {
        mShipmentRv = findViewById(R.id.shipments_rv);
        mTabLayout = findViewById(R.id.tabs);
    }

    private void initShipmentHistoryAdapter() {
        mShipmentHistoryAdapter = new ShipmentHistoryAdapter();
        mShipmentRv.createRvAnimated(mShipmentHistoryAdapter, false);
        refreshAdapter(getString(R.string.all));
    }

    private void initTabLayout() {
        for (int i = 0; i < mDataMgr.getShipmentStatuses().size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.custom_shipment_history_tab_item, mTabLayout, false);

            TextView tvTitle = v.findViewById(R.id.tab_title_text);
            TextView tvBadge = v.findViewById(R.id.tab_badge);

            ShipmentStatus status = mDataMgr.getShipmentStatuses().get(i);
            tvTitle.setText(status.title);
            tvBadge.setText(status.getShipmentCountStr());
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setCustomView(v);
            mTabLayout.addTab(tab);
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabUnselected(TabLayout.Tab tab) {}
            public void onTabReselected(TabLayout.Tab tab) {}
            public void onTabSelected(TabLayout.Tab tab) {
                MaterialTextView tv = tab.getCustomView().findViewById(R.id.tab_title_text);
                refreshAdapter(tv.getText().toString());
            }
        });
        mTabLayout.getTabAt(0).select();
    }

    private void refreshAdapter(String status) {
        List<Shipment> shipments = mDataMgr.sortAndGetShipmentsByStatus(status);
        mShipmentHistoryAdapter.refreshAdapter(shipments);
    }
}
