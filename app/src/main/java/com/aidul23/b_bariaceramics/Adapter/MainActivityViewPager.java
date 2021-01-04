package com.aidul23.b_bariaceramics.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.aidul23.b_bariaceramics.Fragment.SubCatFragment;

import java.util.List;

public class MainActivityViewPager extends FragmentStatePagerAdapter {

    private final List<String> categoryTitles;

    public MainActivityViewPager(@NonNull FragmentManager fm, List<String> categoryTitles) {
        super(fm);
        this.categoryTitles = categoryTitles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("title", categoryTitles.get(position));
        SubCatFragment fragment = new SubCatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return categoryTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryTitles.get(position);
    }
}
