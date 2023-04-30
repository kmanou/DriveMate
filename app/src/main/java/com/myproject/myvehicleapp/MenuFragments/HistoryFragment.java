package com.myproject.myvehicleapp.MenuFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.myproject.myvehicleapp.Adapters.HistoryAdapter;
import com.myproject.myvehicleapp.Models.HistoryViewModel;
import com.myproject.myvehicleapp.LoginActivities.LoginActivity;
import com.myproject.myvehicleapp.Models.HistoryModel;
import com.myproject.myvehicleapp.Models.ExpenseModel;
import com.myproject.myvehicleapp.Models.RefuelingModel;
import com.myproject.myvehicleapp.Models.ServiceModel;
import com.myproject.myvehicleapp.R;
import com.myproject.myvehicleapp.Utlities.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class HistoryFragment extends Fragment {

    private RecyclerView collectionRecyclerView;
    private HistoryAdapter historyAdapter;
    private HistoryViewModel historyViewModel;
    private Toolbar mainToolbarCollection;

    private static final String TAG = "CollectionFragment";

    private final Map<String, String> subCollectionTimestampField = new HashMap<String, String>() {{
        put("my_expense", "expenseTimeStamp");
        put("my_service", "serviceTimeStamp");
        put("my_refueling", "refuelingTimestamp");
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mainToolbarCollection = view.findViewById(R.id.mainToolbarCollection);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mainToolbarCollection);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("History");
        Tools.setSystemBarColor(getActivity(), R.color.red_800);

        // Initialize the ViewModel
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        collectionRecyclerView = view.findViewById(R.id.collection_recycler_view);

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is signed in, set up the recycler view
            setupRecyclerView();
        } else {
            // User is not signed in, redirect to login activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

        fetchSubCollectionsForUserAndSortByTimestamp(auth.getCurrentUser().getUid());

        return view;
    }

    void setupRecyclerView() {
        collectionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyAdapter = new HistoryAdapter(getActivity(), historyViewModel.getCollectionsLiveData());
        collectionRecyclerView.setAdapter(historyAdapter);
    }

    private void fetchSubCollectionsForUserAndSortByTimestamp(String userId) {
        List<String> subCollections = Arrays.asList("my_expense", "my_service", "my_refueling");
        AtomicInteger subCollectionsFetched = new AtomicInteger(0);

        for (String collectionName : subCollections) {
            String timestampField = subCollectionTimestampField.get(collectionName);
            FirebaseFirestore.getInstance().collection("vehicles").document(userId).collection(collectionName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    HistoryModel historyModel = new HistoryModel();
                                    historyModel.setId(document.getId());
                                    historyModel.setCollectionType(collectionName);

                                    // Set the timestamp here
                                    Timestamp timestamp = document.getTimestamp(timestampField);
                                    historyModel.setCollectionTimestamp(timestamp);

                                    if (collectionName.equals("my_refueling")) {
                                        RefuelingModel refuelingModel = document.toObject(RefuelingModel.class);
                                        historyModel.setRefuelingModel(refuelingModel);
                                    } else if (collectionName.equals("my_expense")) {
                                        ExpenseModel expenseModel = document.toObject(ExpenseModel.class);
                                        historyModel.setExpenseModel(expenseModel);
                                    } else if (collectionName.equals("my_service")) {
                                        ServiceModel serviceModel = document.toObject(ServiceModel.class);
                                        historyModel.setServiceModel(serviceModel);
                                    }
                                    List<HistoryModel> currentCollections = historyViewModel.getCollectionsLiveData().getValue();
                                    if (currentCollections == null) {
                                        currentCollections = new ArrayList<>();
                                    }
                                    currentCollections.add(historyModel);
                                    historyViewModel.setCollections(currentCollections);
                                }

                                if (subCollectionsFetched.incrementAndGet() == subCollections.size()) {
                                    List<HistoryModel> currentCollections = historyViewModel.getCollectionsLiveData().getValue();
                                    if (currentCollections == null) {
                                        currentCollections = new ArrayList<>();
                                    }

                                    Collections.sort(currentCollections, new Comparator<HistoryModel>() {
                                        @Override
                                        public int compare(HistoryModel o1, HistoryModel o2) {
                                            Timestamp ts1 = o1.getCollectionTimestamp();
                                            Timestamp ts2 = o2.getCollectionTimestamp();

                                            if (ts1 == null && ts2 == null) {
                                                return 0;
                                            } else if (ts1 == null) {
                                                return 1;
                                            } else if (ts2 == null) {
                                                return -1;
                                            } else {
                                                return ts2.compareTo(ts1);
                                            }
                                        }
                                    });

                                    // Update the LiveData with the new collections list
                                    historyViewModel.setCollections(currentCollections);

                                    // Observe the LiveData and update the RecyclerView adapter when the data changes
                                    historyViewModel.getCollectionsLiveData().observe(getViewLifecycleOwner(), new Observer<List<HistoryModel>>() {
                                        @Override
                                        public void onChanged(List<HistoryModel> historyModels) {
                                            historyAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchSubCollectionsForUserAndSortByTimestamp(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
}