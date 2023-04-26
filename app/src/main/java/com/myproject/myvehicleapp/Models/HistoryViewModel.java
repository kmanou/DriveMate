package com.myproject.myvehicleapp.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myproject.myvehicleapp.Models.HistoryModel;

import java.util.List;

public class HistoryViewModel extends ViewModel {
    private MutableLiveData<List<HistoryModel>> collectionsLiveData;

    public HistoryViewModel() {
        collectionsLiveData = new MutableLiveData<>();
    }

    public LiveData<List<HistoryModel>> getCollectionsLiveData() {
        return collectionsLiveData;
    }

    public void setCollections(List<HistoryModel> collections) {
        collectionsLiveData.setValue(collections);
    }
}
