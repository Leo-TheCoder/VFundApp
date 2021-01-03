package com.example.android.vfund.controller.EventDetailController;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.EventDetailController.Adapter.DonateAdapter;

public class DonateFragment extends Fragment {

    private static DonateAdapter mDonateAdapter;
    private RecyclerView donatePageRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private final String KEY_STATE_DONATE_PAGE_RECYCLERVIEW = "donatePageRecyclerView";
    private Parcelable mState;

    public DonateFragment() {
        // Required empty public constructor
    }

    public DonateFragment(DonateAdapter adapter) {
        mDonateAdapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recyclerview_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donatePageRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEvent);
        donatePageRecyclerView.setAdapter(mDonateAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        donatePageRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_STATE_DONATE_PAGE_RECYCLERVIEW, mState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            mState = savedInstanceState.getParcelable(KEY_STATE_DONATE_PAGE_RECYCLERVIEW);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Retrieve list state and list/item positions
        if(savedInstanceState != null)
            mState = savedInstanceState.getParcelable(KEY_STATE_DONATE_PAGE_RECYCLERVIEW);
    }
}
