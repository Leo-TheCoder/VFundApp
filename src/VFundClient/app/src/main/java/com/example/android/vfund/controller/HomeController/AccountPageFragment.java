package com.example.android.vfund.controller.HomeController;

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
import com.example.android.vfund.controller.HomeController.Adapter.EventAdapter;
import com.example.android.vfund.controller.HomeController.Adapter.EventBriefAdapter;

public class AccountPageFragment extends Fragment {

    private static EventBriefAdapter mEventAdapter;
    private RecyclerView eventAccountPageRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private final String KEY_STATE_ACCOUNT_PAGE_RECYCLERVIEW = "accountPageRecyclerView";
    private Parcelable mState;

    public AccountPageFragment() {
        // Required empty public constructor
    }

    public AccountPageFragment(EventBriefAdapter eventBriefAdapter) {
        mEventAdapter = eventBriefAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventAccountPageRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEvent_Account);
        eventAccountPageRecyclerView.setAdapter(mEventAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        eventAccountPageRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_STATE_ACCOUNT_PAGE_RECYCLERVIEW, mState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            mState = savedInstanceState.getParcelable(KEY_STATE_ACCOUNT_PAGE_RECYCLERVIEW);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Retrieve list state and list/item positions
        if(savedInstanceState != null)
            mState = savedInstanceState.getParcelable(KEY_STATE_ACCOUNT_PAGE_RECYCLERVIEW);
    }
}
