package com.aidul23.b_bariaceramics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aidul23.b_bariaceramics.Adapter.CommonAdapter;
import com.aidul23.b_bariaceramics.Adapter.MainActivityViewPager;
import com.aidul23.b_bariaceramics.Interface.SubCatLoadCallback;
import com.aidul23.b_bariaceramics.Task.FirebaseQueryTask;
import com.aidul23.b_bariaceramics.Utility.StringUtility;
import com.aidul23.b_bariaceramics.ViewModel.MainActivityViewModel;
import com.aidul23.b_bariaceramics.databinding.ActivityMainBinding;
import com.github.tntkhang.fullscreenimageview.library.FullScreenImageViewActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel activityViewModel;

    private ActivityMainBinding binding;

    private BottomSheetBehavior bottomSheetBehavior;

    private FrameLayout bottomSheet;
    private AnimatedRecyclerView productList;
    private ProgressBar productListProgressBar;
    private TextView bottomSheetHeadline;
    private CommonAdapter adapter;
    private final Observer<String> selectedCat = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if (s != null) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setHideable(true);
                    productListProgressBar.setVisibility(View.VISIBLE);
                    bottomSheetHeadline.setText(s);

                    new FirebaseQueryTask(new SubCatLoadCallback() {
                        @Override
                        public void onSubCatLoadCallback(List<String> subCats) {
                            productListProgressBar.setVisibility(View.GONE);
                            productList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                            adapter = new CommonAdapter(subCats, "product", MainActivity.this, "");
                            productList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            productList.scheduleLayoutAnimation();

                            adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent fullImageIntent = new Intent(MainActivity.this, FullScreenImageViewActivity.class);
                                    fullImageIntent.putExtra(FullScreenImageViewActivity.URI_LIST_DATA, new ArrayList<String>(subCats));
                                    fullImageIntent.putExtra(FullScreenImageViewActivity.IMAGE_FULL_SCREEN_CURRENT_POS, position);
                                    startActivity(fullImageIntent);
                                }
                            });
                        }
                    }, s, "product").execute();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomSheet = findViewById(R.id.bottomSheet);
        productListProgressBar = findViewById(R.id.productListProgressBar);
        bottomSheetHeadline = findViewById(R.id.bottomSheetHeadline);
        productList = findViewById(R.id.productRecyclerView);
        productList.setNestedScrollingEnabled(false);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        if (activityViewModel == null) {
            activityViewModel = ViewModelProviders.of(MainActivity.this).get(MainActivityViewModel.class);
        }

        setupViewPager();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!activityViewModel.getSubCatSelectedItem().hasActiveObservers()) {
            activityViewModel.getSubCatSelectedItem().observe(this, selectedCat);
        }

        changeTabTitles();
    }

    private void changeTabTitles() {
        binding.tabLayout.getTabAt(0).setText(getResources().getString(R.string.wash_basin));
        binding.tabLayout.getTabAt(1).setText(getResources().getString(R.string.high_commode));
        binding.tabLayout.getTabAt(2).setText(getResources().getString(R.string.low_commode));
        binding.tabLayout.getTabAt(3).setText(getResources().getString(R.string.metal));
    }

    private void setupViewPager() {
        MainActivityViewPager adapter = new MainActivityViewPager(getSupportFragmentManager(), StringUtility.getTitles());
        binding.viewpager.setAdapter(adapter);
        binding.viewpager.setOffscreenPageLimit(0);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    @Override
    public void onBackPressed() {

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            if (binding.viewpager.getCurrentItem() == 0) {
                super.onBackPressed();
            } else if (binding.viewpager.getCurrentItem() == 2) {
                binding.viewpager.setCurrentItem(1);
            } else if (binding.viewpager.getCurrentItem() == 1) {
                binding.viewpager.setCurrentItem(0);
            } else if (binding.viewpager.getCurrentItem() == 3) {
                binding.viewpager.setCurrentItem(2);
            }
        }
    }
}