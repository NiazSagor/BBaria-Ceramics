package com.aidul23.b_bariaceramics.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainActivityViewModel extends AndroidViewModel {

    MutableLiveData<Integer> categorySelectedIndex = new MutableLiveData<>();

    MutableLiveData<String> subCatSelectedItem = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getCategorySelectedIndex() {
        return categorySelectedIndex;
    }

    public MutableLiveData<String> getSubCatSelectedItem() {
        return subCatSelectedItem;
    }
}
