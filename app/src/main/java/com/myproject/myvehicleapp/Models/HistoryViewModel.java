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

    /**
     * Retrieves the LiveData object containing the list of history collections.
     * @return The LiveData object.
     */
    public LiveData<List<HistoryModel>> getCollectionsLiveData() {
        return collectionsLiveData;
    }

    /**
     * Sets the list of history collections and updates the LiveData object.
     * @param collections The list of history collections.
     */
    public void setCollections(List<HistoryModel> collections) {
        collectionsLiveData.setValue(collections);
    }
}

