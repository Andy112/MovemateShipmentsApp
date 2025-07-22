package com.simplemova.activity.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import com.simplemova.R;
import com.simplemova.activity.extra.BaseActivity;
import com.simplemova.data.DummyDataGenerator;
import com.simplemova.model.Parcel;
import com.simplemova.ui.adapter.SearchAdapter;
import com.simplemova.ui.widget.CustomRecyclerView;
import com.simplemova.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends BaseActivity {

    private SearchAdapter mSearchAdapter;
    private CustomRecyclerView mRvSearch;
    private AppCompatEditText mEtSearch;
    private final List<Parcel> mAllParcels = DummyDataGenerator.getAllParcels();
    private final List<Parcel> mFilteredParcels = new ArrayList<>();
    private String mSearchValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(android.view.Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_search);
        setOnApplyWindowInsetsListener();
        initWidgets();
        initSearchAdapter();
        initSearchEditTextListener();
        registerSharedElementTransitionListener(this::refreshAdapter);
        showSoftInputKeyboard();
    }

    private void initWidgets() {
        mRvSearch = findViewById(R.id.rv_search);
        mEtSearch = findViewById(R.id.et_search);
    }

    private void initSearchAdapter() {
        mSearchAdapter = new SearchAdapter();
        mRvSearch.createRvAnimated(mSearchAdapter, false);
    }

    private void initSearchEditTextListener() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterOnSearch(s.toString().toLowerCase(Locale.ROOT));
            }
        });
    }

    private void showSoftInputKeyboard() {
        mEtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void refreshAdapter() {
        mEtSearch.postDelayed(() -> filterOnSearch(mSearchValue), 100);
    }

    private void filterOnSearch(String searchValue) {
        mSearchValue = searchValue;
        mFilteredParcels.clear();
        if (mSearchValue == null || mSearchValue.isEmpty()) {
            mFilteredParcels.addAll(mAllParcels);
        } else {
            for (Parcel parcel : mAllParcels) {
                if (Util.performSimpleSearch(parcel.getSearchKeyWords(), mSearchValue))
                    mFilteredParcels.add(parcel);
            }
        }
        mSearchAdapter.refreshAdapter(mFilteredParcels);
    }
}
